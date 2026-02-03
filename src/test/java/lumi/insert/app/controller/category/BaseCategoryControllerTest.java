package lumi.insert.app.controller.category;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import lumi.insert.app.service.CategoryService;
import lumi.insert.app.utils.mapper.CategoryMapper;

@SpringBootTest
@AutoConfigureMockMvc
public abstract class BaseCategoryControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    CategoryService categoryService;

    @Autowired
    CategoryMapper categoryMapper;
}
