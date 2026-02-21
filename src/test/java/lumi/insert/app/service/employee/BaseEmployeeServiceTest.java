package lumi.insert.app.service.employee;

import java.time.LocalDateTime;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import lumi.insert.app.entity.Employee;
import lumi.insert.app.repository.AuthTokenRepository;
import lumi.insert.app.repository.EmployeeRepository; 
import lumi.insert.app.service.implement.EmployeeServiceImpl;
import lumi.insert.app.utils.mapper.EmployeeMapperImpl;

@ExtendWith(MockitoExtension.class)
public abstract class BaseEmployeeServiceTest {
    
    @InjectMocks
    EmployeeServiceImpl employeeServiceMock;

    @Mock
    EmployeeRepository employeeRepositoryMock;

    @Mock
    BCryptPasswordEncoder passwordEncoder;

    @Mock
    AuthTokenRepository authTokenRepositoryMock;

    @Spy
    EmployeeMapperImpl employeeMapperImpl = new EmployeeMapperImpl();

    Employee setupEmployee;
  

    @BeforeEach
    void setup(){
        setupEmployee = Employee.builder()
        .id(UUID.randomUUID())
        .username("testUsername")
        .fullname("testFullname")
        .password("SECRET")
        .joinDate(LocalDateTime.of(2020, 10, 10, 10, 10))
        .lastIp("xxx.xxx.xxx.xxx")
        .isActive(false)
        .build();
    }

}
