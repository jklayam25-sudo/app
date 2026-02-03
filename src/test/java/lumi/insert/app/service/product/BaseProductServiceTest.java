package lumi.insert.app.service.product;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

import jakarta.transaction.Transactional;
import lumi.insert.app.repository.CategoryRepository;
import lumi.insert.app.repository.ProductRepository;
import lumi.insert.app.service.ProductService;
import lumi.insert.app.service.implement.ProductServiceImpl; 
import lumi.insert.app.utils.mapper.CategoryMapperImpl;
import lumi.insert.app.utils.mapper.ProductMapper;
import lumi.insert.app.utils.mapper.ProductMapperImpl;

@SpringBootTest
@Transactional
@ExtendWith(MockitoExtension.class)
public abstract class BaseProductServiceTest {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    ProductService productService;

    @Autowired
    CategoryRepository categoryRepository;

    @InjectMocks
    ProductServiceImpl productServiceMock;

    @Mock
    ProductRepository productRepositoryMock;

    @Mock
    CategoryRepository categoryRepositoryMock;

    @Spy 
    ProductMapper productMapper = new ProductMapperImpl();

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(productMapper, "categoryMapper", new CategoryMapperImpl());
    }

}
