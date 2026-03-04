package lumi.insert.app.service.supply; 



import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import com.github.f4b6a3.uuid.UuidCreator; 

import lumi.insert.app.entity.Supplier;
import lumi.insert.app.entity.Product;
import lumi.insert.app.entity.Supply;
import lumi.insert.app.entity.SupplyItem; 
import lumi.insert.app.repository.SupplierRepository;
import lumi.insert.app.repository.SupplyItemRepository;
import lumi.insert.app.repository.ProductRepository;
import lumi.insert.app.repository.StockCardRepository;
import lumi.insert.app.repository.SupplyRepository; 
import lumi.insert.app.service.implement.SupplyServiceImpl;
import lumi.insert.app.utils.generator.InvoiceGenerator;
import lumi.insert.app.utils.generator.JpaSpecGenerator;
import lumi.insert.app.utils.mapper.AllSupplyMapper;
import lumi.insert.app.utils.mapper.AllSupplyMapperImpl; 
import lumi.insert.app.utils.mapper.ProductMapperImpl;

@ExtendWith(MockitoExtension.class) 
public abstract class BaseSupplyServiceTest {
    
    @InjectMocks
    SupplyServiceImpl supplyServiceMock;

    @Mock
    SupplyRepository supplyRepositoryMock;

    @Mock
    ProductRepository productRepositoryMock;

    @Mock
    SupplierRepository supplierRepositoryMock;

    @Mock
    StockCardRepository stockCardRepositoryMock;

    @Mock
    SupplyItemRepository supplyItemRepositoryMock;

    @Mock
    JpaSpecGenerator jpaSpecGenerator;

    @Spy
    AllSupplyMapper allSupplyMapper = new AllSupplyMapperImpl();

    @Spy
    InvoiceGenerator invoiceGenerator = new InvoiceGenerator();

    public Supply setupSupply;

    public SupplyItem setupSupplyItem;

    public Product setupProduct;

    public Supplier setupSupplier;

    @BeforeEach
    void setUp(){ 

        setupSupply = Supply.builder()
        .id(UuidCreator.getTimeOrderedEpochFast())
        .build();

        setupSupplyItem = SupplyItem.builder()
        .id(UuidCreator.getTimeOrderedEpochFast())
        .build();

        setupProduct = Product.builder()
        .id(1L)
        .build();

        setupSupplier = Supplier.builder()
        .id(UuidCreator.getTimeOrderedEpochFast())
        .build();

        ReflectionTestUtils.setField(allSupplyMapper, "productMapper", new ProductMapperImpl());
    }
}
