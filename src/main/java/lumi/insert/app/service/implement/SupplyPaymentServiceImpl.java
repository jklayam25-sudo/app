package lumi.insert.app.service.implement;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.github.f4b6a3.uuid.UuidCreator;

import jakarta.transaction.Transactional;
import lumi.insert.app.dto.request.PaginationRequest;
import lumi.insert.app.dto.request.SupplyPaymentCreateRequest;
import lumi.insert.app.dto.request.SupplyPaymentGetByFilter;
import lumi.insert.app.dto.response.SupplyPaymentResponse; 
import lumi.insert.app.entity.Supplier;
import lumi.insert.app.entity.Supply;
import lumi.insert.app.entity.SupplyPayment; 
import lumi.insert.app.entity.nondatabase.SupplyStatus;
import lumi.insert.app.exception.ForbiddenRequestException;
import lumi.insert.app.exception.NotFoundEntityException;
import lumi.insert.app.exception.TransactionValidationException;
import lumi.insert.app.repository.SupplyPaymentRepository;
import lumi.insert.app.repository.SupplyRepository;
import lumi.insert.app.service.SupplyPaymentService;
import lumi.insert.app.utils.generator.JpaSpecGenerator;
import lumi.insert.app.utils.mapper.AllSupplyMapper;

@Service
@Transactional
public class SupplyPaymentServiceImpl implements SupplyPaymentService{

    @Autowired
    SupplyPaymentRepository supplyPaymentRepository;

    @Autowired
    SupplyRepository supplyRepository;

    @Autowired
    JpaSpecGenerator jpaSpecGenerator;

    @Autowired
    AllSupplyMapper allSupplyMapper;


    @Override
    public SupplyPaymentResponse createSupplyPayment(UUID supplyId, SupplyPaymentCreateRequest request) {
        Supply supply = supplyRepository.findById(supplyId)
            .orElseThrow(() -> new NotFoundEntityException("Supply with ID " + supplyId + " was not found"));

        if(supply.getStatus() != SupplyStatus.UNPAID) throw new ForbiddenRequestException("Unable to set payment because supply status is not UNPAID, check carefully");

        SupplyPayment supplyPayment = SupplyPayment.builder()
            .id(UuidCreator.getTimeOrderedEpochFast())
            .supply(supply)
            .paymentFrom(request.getPaymentFrom())
            .paymentTo(request.getPaymentTo())
            .totalPayment(request.getTotalPayment())
            .build();

        supply.setTotalUnpaid(supply.getTotalUnpaid() - request.getTotalPayment());
        supply.setTotalPaid(supply.getTotalPaid() + request.getTotalPayment());

        if(supply.getTotalUnpaid() < 0) throw new TransactionValidationException("Payment exceeds the remaining transaction debts with ID " + supplyId + ", enter an exact amount to proceed");

        Supplier supplier = supply.getSupplier();
        supplier.setTotalUnpaid(supplier.getTotalUnpaid() - request.getTotalPayment());
        supplier.setTotalPaid(supplier.getTotalPaid() + request.getTotalPayment());
        // Upcoming: integrate with email notification
        if(supply.getTotalUnpaid() == 0) supply.setStatus(SupplyStatus.COMPLETE);
        SupplyPayment savedSupplyPayment = supplyPaymentRepository.save(supplyPayment);
        SupplyPaymentResponse supplyPaymentResponse = allSupplyMapper.createSupplyPaymentResponseDto(savedSupplyPayment);

        return supplyPaymentResponse;
    }

    @Override
    public Slice<SupplyPaymentResponse> getSupplyPaymentsBySupplyId(UUID supplyId, PaginationRequest request) {
        Pageable pageable = jpaSpecGenerator.pageable(request);

        Slice<SupplyPayment> payments = supplyPaymentRepository.findAllBySupplyId(supplyId, pageable); 
        return payments.map(allSupplyMapper::createSupplyPaymentResponseDto);
    }

    @Override
    public SupplyPaymentResponse getSupplyPayment(UUID id) {
        SupplyPayment supplyPayment = supplyPaymentRepository.findById(id)
            .orElseThrow(() -> new NotFoundEntityException("null"));

        return allSupplyMapper.createSupplyPaymentResponseDto(supplyPayment);
    }

    @Override
    public Slice<SupplyPaymentResponse> getSupplyPaymentsByRequests(SupplyPaymentGetByFilter request) {
        Pageable pageable = jpaSpecGenerator.pageable(request);

        Specification<SupplyPayment> supplyPaymentSpecification = jpaSpecGenerator.supplyPaymentSpecification(request);

        Slice<SupplyPayment> payments = supplyPaymentRepository.findAll(supplyPaymentSpecification, pageable);
        return payments.map(allSupplyMapper::createSupplyPaymentResponseDto);
    }

    @Override
    public SupplyPaymentResponse refundSupplyPayment(UUID supplyId, SupplyPaymentCreateRequest request) {
        Supply supply = supplyRepository.findById(supplyId)
            .orElseThrow(() -> new NotFoundEntityException("Supply with ID " + supplyId + " was not found"));

        if(supply.getStatus() == SupplyStatus.UNPAID) throw new ForbiddenRequestException("Unable to set payment because supply status is UNPAID / NOT DONE YET, check carefully");

        SupplyPayment supplyPayment = SupplyPayment.builder()
            .id(UuidCreator.getTimeOrderedEpochFast())
            .supply(supply)
            .paymentFrom(request.getPaymentFrom())
            .paymentTo(request.getPaymentTo())
            .totalPayment(request.getTotalPayment())
            .isForRefund(true)
            .build();

        supply.setTotalUnrefunded(supply.getTotalUnrefunded() - request.getTotalPayment());
        supply.setTotalRefunded(supply.getTotalRefunded() + request.getTotalPayment());

        if(supply.getTotalUnrefunded() < 0) throw new TransactionValidationException("Payment exceeds the remaining transaction debts with ID " + supplyId + ", enter an exact amount to proceed");

        Supplier supplier = supply.getSupplier();
        supplier.setTotalUnrefunded(supplier.getTotalUnrefunded() - request.getTotalPayment());
        supplier.setTotalRefunded(supplier.getTotalRefunded() + request.getTotalPayment());
        // Upcoming: integrate with email notification 
        SupplyPayment savedSupplyPayment = supplyPaymentRepository.save(supplyPayment);
        SupplyPaymentResponse supplyPaymentResponse = allSupplyMapper.createSupplyPaymentResponseDto(savedSupplyPayment);

        return supplyPaymentResponse;
    }
    
}
