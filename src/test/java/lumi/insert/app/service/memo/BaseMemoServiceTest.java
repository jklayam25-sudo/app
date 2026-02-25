package lumi.insert.app.service.memo;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import lumi.insert.app.repository.EmployeeRepository;
import lumi.insert.app.repository.MemoRepository;
import lumi.insert.app.repository.MemoViewRepository; 
import lumi.insert.app.service.implement.MemoServiceImpl;
import lumi.insert.app.utils.mapper.MemoMapper;
import lumi.insert.app.utils.mapper.MemoMapperImpl;

@ExtendWith(MockitoExtension.class)
public abstract class BaseMemoServiceTest {
    
    @InjectMocks
    MemoServiceImpl memoService;

    @Mock
    MemoRepository memoRepository;

    @Mock
    MemoViewRepository memoViewRepository;

    @Mock
    EmployeeRepository employeeRepository;

    @Spy
    MemoMapper memoMapper = new MemoMapperImpl();

}
