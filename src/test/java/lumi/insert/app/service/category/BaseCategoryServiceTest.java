package lumi.insert.app.service.category;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import jakarta.transaction.Transactional;
import lumi.insert.app.core.repository.CategoryRepository;
import lumi.insert.app.mapper.CategoryMapper;
import lumi.insert.app.service.CategoryService;
import lumi.insert.app.service.implement.CategoryServiceImpl;

@SpringBootTest
@Transactional
@ExtendWith(MockitoExtension.class)
public abstract class BaseCategoryServiceTest {
    @InjectMocks
    CategoryServiceImpl categoryServiceMock;

    @Mock
    CategoryRepository categoryRepositoryMock; 

    @Mock
    CategoryMapper categoryMapper;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    CategoryService categoryService;

}
