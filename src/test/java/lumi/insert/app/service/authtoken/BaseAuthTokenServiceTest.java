package lumi.insert.app.service.authtoken;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock; 
import org.mockito.junit.jupiter.MockitoExtension; 
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import lumi.insert.app.dto.response.AuthTokenResponse;
import lumi.insert.app.dto.response.EmployeeResponse;
import lumi.insert.app.entity.AuthToken;
import lumi.insert.app.entity.Employee;
import lumi.insert.app.repository.AuthTokenRepository;
import lumi.insert.app.repository.EmployeeRepository;
import lumi.insert.app.service.implement.AuthTokenServiceImpl; 
import lumi.insert.app.utils.mapper.AuthMapperImpl; 
import lumi.insert.app.utils.security.JwtUtils;

@ExtendWith(MockitoExtension.class)
public abstract class BaseAuthTokenServiceTest {
    
    @InjectMocks
    AuthTokenServiceImpl authTokenServiceMock;

    @Mock
    EmployeeRepository employeeRepository;

    @Mock
    AuthTokenRepository authTokenRepository;

    @Mock
    BCryptPasswordEncoder passwordEncoder;

    @Mock
    AuthMapperImpl authMapper;

    @Mock
    JwtUtils jwtUtils;

    Employee setupEmployee;

    AuthToken setupAuthToken;

    AuthTokenResponse authTokenResponse;

    @BeforeEach
    void setup(){
        setupEmployee = Employee.builder()
        .id(UUID.randomUUID())
        .username("testUsername")
        .fullname("testFullname")
        .password("SECRET")
        .joinDate(LocalDateTime.of(2020, 10, 10, 10, 10))
        .lastIp("xxx.xxx.xxx.xxx")
        .build();

        setupAuthToken = AuthToken.builder()
        .employee(setupEmployee)
        .expiredAt(LocalDateTime.now().plus(7, ChronoUnit.DAYS))
        .refreshToken(UUID.randomUUID().toString())
        .build();

        authTokenResponse = new AuthTokenResponse("someAccessToken", UUID.randomUUID().toString(), new EmployeeResponse(setupEmployee.getId(), setupEmployee.getUsername(), setupEmployee.getFullname(), setupEmployee.getRole()), LocalDateTime.now().plusDays(7), null);
    }
}
