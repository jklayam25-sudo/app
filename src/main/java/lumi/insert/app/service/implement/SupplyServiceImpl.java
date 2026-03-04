package lumi.insert.app.service.implement;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.github.f4b6a3.uuid.UuidCreator; 

import jakarta.transaction.Transactional;
import lumi.insert.app.dto.request.SupplyCreateRequest;
import lumi.insert.app.dto.request.SupplyGetByFilter;
import lumi.insert.app.dto.request.SupplyItemCreate;
import lumi.insert.app.dto.request.SupplyItemRefundRequest;
import lumi.insert.app.dto.request.SupplyUpdateRequest;
import lumi.insert.app.dto.response.SupplyDetailResponse;
import lumi.insert.app.dto.response.SupplyResponse;
import lumi.insert.app.entity.Product;
import lumi.insert.app.entity.StockCard;
import lumi.insert.app.entity.Supplier;
import lumi.insert.app.entity.Supply;
import lumi.insert.app.entity.SupplyItem; 
import lumi.insert.app.entity.nondatabase.StockMove;
import lumi.insert.app.entity.nondatabase.SupplyStatus;
import lumi.insert.app.exception.ForbiddenRequestException;
import lumi.insert.app.exception.NotFoundEntityException;
import lumi.insert.app.exception.TransactionValidationException;
import lumi.insert.app.repository.ProductRepository;
import lumi.insert.app.repository.StockCardRepository;
import lumi.insert.app.repository.SupplierRepository;
import lumi.insert.app.repository.SupplyItemRepository;
import lumi.insert.app.repository.SupplyRepository;
import lumi.insert.app.service.SupplyService;
import lumi.insert.app.utils.generator.JpaSpecGenerator;
import lumi.insert.app.utils.mapper.AllSupplyMapper;

@Service
@Transactional
public class SupplyServiceImpl implements SupplyService{

    @Autowired
    SupplyRepository supplyRepository;

    @Autowired
    AllSupplyMapper allSupplyMapper;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    SupplierRepository supplierRepository;

    @Autowired
    SupplyItemRepository supplyItemRepository;

    @Autowired
    StockCardRepository stockCardRepository;

    @Autowired
    JpaSpecGenerator jpaSpecGenerator;

    @Override
    public SupplyResponse createSupply(SupplyCreateRequest request) {
        Supplier supplier = supplierRepository.findById(request.getSupplierId())
             .orElseThrow(() -> new NotFoundEntityException("Supplier with id " + request.getSupplierId() + " is not found"));

        List<SupplyItemCreate> items = request.getItems();

        List<Long> listOfProductId = items.stream().map(item -> item.getProductId()).distinct().collect(Collectors.toCollection(ArrayList::new));
        List<Product> listOfProduct = productRepository.findAllById(listOfProductId);

        if(listOfProduct.size() != listOfProductId.size()){
            listOfProductId.removeAll(listOfProduct.stream().map(Product::getId).distinct().toList());
            throw new NotFoundEntityException("Product with id " + listOfProductId.toString() + " not found!");
        } 

        Long subTotal = items.stream().mapToLong(item -> item.getPrice() * item.getQuantity()).sum();

        Supply supply = Supply.builder()
        .invoiceId(request.getInvoiceId())
        .description(request.getDescription())
        .supplier(supplier)
        .totalItems(Long.valueOf(items.size()))
        .subTotal(subTotal)
        .grandTotal(subTotal - request.getTotalDiscount() + request.getTotalFee())
        .totalUnpaid(subTotal - request.getTotalDiscount() + request.getTotalFee())
        .totalFee(request.getTotalFee())
        .totalDiscount(request.getTotalDiscount())
        .build();

        Supply savedSupply = supplyRepository.saveAndFlush(supply);

        Map<Long,Product> mappedProduct = listOfProduct.stream().collect(Collectors.toMap(Product::getId, Function.identity()));

        List<SupplyItem> itemsToAdd = new ArrayList<>();

        List<StockCard> stockCardsToAdd = new ArrayList<>();

        for (SupplyItemCreate item : items) {
            Product product = mappedProduct.get(item.getProductId());
 
            Long oldStock = product.getStockQuantity();

            SupplyItem supplyItem = SupplyItem.builder()
            .id(UuidCreator.getTimeOrderedEpochFast())
            .price(item.getPrice())
            .quantity(item.getQuantity())
            .description(item.getDescription())
            .product(product)
            .supply(savedSupply)
            .build();

            itemsToAdd.add(supplyItem);
            
            product.setBasePrice(((product.getBasePrice() * product.getStockQuantity()) + (supplyItem.getPrice() * supplyItem.getQuantity())) / (product.getStockQuantity() + supplyItem.getQuantity()));
            product.setStockQuantity(product.getStockQuantity() + supplyItem.getQuantity());

            StockCard stockCard = StockCard.builder() 
                .referenceId(supplyItem.getId())
                .product(product)
                .productName(product.getName())
                .quantity(item.getQuantity())
                .oldStock(oldStock)
                .newStock(product.getStockQuantity())
                .type(StockMove.PURCHASE)
                .basePrice(product.getBasePrice())
                .description("Product stock supply(IN)")
                .build();

            stockCardsToAdd.add(stockCard);
        }

        supplyItemRepository.saveAll(itemsToAdd);

        stockCardRepository.saveAll(stockCardsToAdd);

        supplier.addTransaction();
        supplier.setTotalUnpaid(supplier.getTotalUnpaid() + savedSupply.getTotalUnpaid());
        return allSupplyMapper.createSimpleDTO(savedSupply);
    }

    @Override
    public Slice<SupplyResponse> searchSuppliesByRequests(SupplyGetByFilter request) {
        Pageable pageable = jpaSpecGenerator.pageable(request);
     
        Specification<Supply> supplySpecification = jpaSpecGenerator.supplySpecification(request);

        Slice<Supply> supplies = supplyRepository.findAll(supplySpecification, pageable);

        return supplies.map(allSupplyMapper::createSimpleDTO);
    }

    @Override
    public SupplyResponse cancelSupply(UUID id) {
        Supply supply = supplyRepository.findByIdDetail(id)
            .orElseThrow(() -> new NotFoundEntityException("Supply with ID " + id + " was not found"));

        if(supply.getStatus() == SupplyStatus.CANCELLED) throw new ForbiddenRequestException("Unable to cancel supply because Supply Status is CANCELLED");

        List<SupplyItem> supplyItems = supply.getSupplyItems();
        
        Map<Long, Long> listRefunded = supplyItems.stream()
            .filter(item -> item.getQuantity() < 0)
            .collect(Collectors
                .groupingBy(item -> item.getProduct().getId(), Collectors.summingLong(item -> item.getQuantity()))
            );

        List<SupplyItem> reverseToAdd = new ArrayList<>();

        List<StockCard> stockCardToAdd = new ArrayList<>();
        
        for (SupplyItem item : supplyItems) {
            if(item.getQuantity() < 0) { 
                continue;
            }; 

            Product product = item.getProduct();
            Long oldStock = product.getStockQuantity();

            Long alreadyRefundedProduct = listRefunded.get(product.getId());
            alreadyRefundedProduct = alreadyRefundedProduct != null ? alreadyRefundedProduct : 0L;

            SupplyItem reverseItem = SupplyItem.builder()
                .id(UuidCreator.getTimeOrderedEpochFast())
                .price(item.getPrice())
                .quantity(-(item.getQuantity() + alreadyRefundedProduct))
                .description("CANCELLED ")
                .product(item.getProduct())
                .supply(item.getSupply())
                .build();

            reverseToAdd.add(reverseItem);

            if((product.getStockQuantity() - Math.abs(reverseItem.getQuantity())) != 0) {
                product.setBasePrice(((product.getBasePrice() * product.getStockQuantity() - reverseItem.getPrice() * Math.abs(reverseItem.getQuantity())))  / (product.getStockQuantity() - Math.abs(reverseItem.getQuantity())));
            }
            product.setStockQuantity(product.getStockQuantity() + reverseItem.getQuantity());

            stockCardToAdd.add(StockCard.builder() 
                .referenceId(reverseItem.getId())
                .product(product)
                .productName(product.getName())
                .quantity(reverseItem.getQuantity())
                .oldStock(oldStock)
                .newStock(product.getStockQuantity())
                .type(StockMove.SUPPLIER_OUT)
                .basePrice(product.getBasePrice())
                .description("Supply Cancelled, Product refunded. Status: CUSTOMER_OUT(OUT)")
                .build()
            );   
        }
        supplyItemRepository.saveAll(reverseToAdd);
        stockCardRepository.saveAll(stockCardToAdd);
        
        Supplier supplier = supply.getSupplier();
        supplier.setTotalUnpaid(supplier.getTotalUnpaid() - supply.getTotalUnpaid());
        supplier.setTotalPaid(supplier.getTotalPaid() - supply.getTotalPaid());
        supplier.setTotalUnrefunded(supplier.getTotalUnrefunded() + supply.getTotalPaid());

        supply.setStatus(SupplyStatus.CANCELLED);
        supply.setTotalUnrefunded(supply.getTotalPaid() + supply.getTotalUnrefunded());
        supply.setTotalUnpaid(0L);
        supply.setTotalPaid(0L); 

        return allSupplyMapper.createSimpleDTO(supply);
    }

    @Override
    public SupplyDetailResponse getSupply(UUID id) {
        Supply supply = supplyRepository.findByIdDetail(id)
            .orElseThrow(() -> new NotFoundEntityException("Supply with ID " + id + " is not found"));

        return allSupplyMapper.createDetailDTO(supply);
    }

    @Override
    public byte[] getInvoicePdf(UUID id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getInvoicePdf'");
    }

    @Override
    public SupplyResponse updateSupply(UUID id, SupplyUpdateRequest request) {
        Supply supply = supplyRepository.findByIdDetail(id)
            .orElseThrow(() -> new NotFoundEntityException("Supply with ID " + id + " is not found"));
 
        if(supply.getStatus() == SupplyStatus.CANCELLED) throw new ForbiddenRequestException("Unable to cancel supply because Supply Status is CANCELLED");

        allSupplyMapper.updateSupply(request, supply);

        if(request.getTotalDiscount() != 0 || request.getTotalFee() != 0){
            
            Long totalChange = request.getTotalDiscount() - request.getTotalFee();
            Long oldTotalUnpaid = supply.getTotalUnpaid();
            Long oldTotalPaid = supply.getTotalPaid();
            Long oldTotalUnrefunded = supply.getTotalUnrefunded();

            supply.setGrandTotal(supply.getSubTotal() - totalChange);
            
            supply.setTotalUnpaid(oldTotalUnpaid - totalChange);
            Long changeTotalUnpaid = supply.getTotalUnpaid();

            if(changeTotalUnpaid < 0){
                supply.setTotalUnpaid(0L);
                supply.setTotalPaid(oldTotalPaid - Math.abs(changeTotalUnpaid));
                supply.setTotalUnrefunded(oldTotalUnrefunded + Math.abs(changeTotalUnpaid));
           }

           Supplier supplier = supply.getSupplier();
           supplier.setTotalUnpaid(supplier.getTotalUnpaid() + (changeTotalUnpaid - oldTotalUnpaid));
           supplier.setTotalPaid(supplier.getTotalPaid() + (supply.getTotalPaid() - oldTotalPaid));
           supplier.setTotalUnrefunded(supplier.getTotalUnrefunded() + (supply.getTotalUnrefunded() - oldTotalUnrefunded));
        }

        return allSupplyMapper.createSimpleDTO(supply);
    }

    @Override
    public SupplyResponse refundSupplyItem(UUID id, SupplyItemRefundRequest request) { 

        List<SupplyItem> matchItems = supplyItemRepository.findBySupplyIdAndProductId(id, request.getProductId());
        if(matchItems.size() == 0) throw new NotFoundEntityException("");
        long priceFromSupplier = matchItems.getLast().getPrice();
        long ttlQuantiyLeft = matchItems.stream().mapToLong(item -> item.getQuantity()).sum();

        if(ttlQuantiyLeft < request.getQuantity()) throw new TransactionValidationException("");

        Supply supply = matchItems.getFirst().getSupply();

        if(supply.getStatus() == SupplyStatus.CANCELLED) throw new ForbiddenRequestException("Unable to cancel supply because Supply Status is CANCELLED");

        Product product = matchItems.getFirst().getProduct();
        if(product.getStockQuantity() < request.getQuantity()) throw new TransactionValidationException("");

        Long oldStock = product.getStockQuantity();

        if((product.getStockQuantity() - Math.abs(request.getQuantity())) != 0) {
            product.setBasePrice(((product.getBasePrice() * product.getStockQuantity() - priceFromSupplier * Math.abs(request.getQuantity())))  / (product.getStockQuantity() - Math.abs(request.getQuantity())));
        }
 
        Long oldTotalUnpaid = supply.getTotalUnpaid();
        Long oldTotalPaid = supply.getTotalPaid();
        Long oldTotalUnrefunded = supply.getTotalUnrefunded();

        product.setStockQuantity(oldStock - request.getQuantity());
        
        SupplyItem supplyItem = SupplyItem.builder()
                .id(UuidCreator.getTimeOrderedEpochFast())
                .price(priceFromSupplier)
                .quantity(-request.getQuantity())
                .description("REFUNDED ")
                .product(product)
                .supply(supply)
                .build();

        StockCard stockCard = StockCard.builder() 
                .referenceId(supplyItem.getId())
                .product(product)
                .productName(product.getName())
                .quantity(supplyItem.getQuantity())
                .oldStock(oldStock)
                .newStock(product.getStockQuantity())
                .type(StockMove.SUPPLIER_OUT)
                .basePrice(product.getBasePrice())
                .description("Supply Cancelled, Product refunded. Status: SUPPLIER_OUT(OUT)")
                .build();

        Long totalChange = priceFromSupplier * request.getQuantity();

        supply.setTotalUnpaid(supply.getTotalUnpaid() - totalChange);
        Long changeTotalUnpaid = supply.getTotalUnpaid();
        
        if(changeTotalUnpaid < 0){
            supply.setTotalUnpaid(0L);
            supply.setTotalPaid(oldTotalPaid - Math.abs(changeTotalUnpaid));
            supply.setTotalUnrefunded(oldTotalUnrefunded + Math.abs(changeTotalUnpaid));
        }

        Supplier supplier = supply.getSupplier();
        supplier.setTotalUnpaid(supplier.getTotalUnpaid() + (changeTotalUnpaid - oldTotalUnpaid));
        supplier.setTotalPaid(supplier.getTotalPaid() + (supply.getTotalPaid() - oldTotalPaid));
        supplier.setTotalUnrefunded(supplier.getTotalUnrefunded() + (supply.getTotalUnrefunded() - oldTotalUnrefunded));

        stockCardRepository.save(stockCard);
        supplyItemRepository.save(supplyItem);
        return allSupplyMapper.createSimpleDTO(supply);
    }
    
}
