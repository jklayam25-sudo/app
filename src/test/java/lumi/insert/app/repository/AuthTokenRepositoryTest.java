package lumi.insert.app.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import jakarta.transaction.Transactional;
import lumi.insert.app.entity.AuthToken;
import lumi.insert.app.entity.Employee;

@DataJpaTest
@Transactional
public class AuthTokenRepositoryTest {
    
    @Autowired
    AuthTokenRepository authTokenRepository;

    @Autowired
    EmployeeRepository employeeRepository;


    @Test
    @DisplayName("Should return saved entity when repository save success")
    void saveAuthToken_validEntity_shouldReturnSavedEntity(){
        Employee employee = Employee.builder()
        .username("TESTEMPLOYE")
        .fullname("TESTEMPLOYE")
        .password("TESTEMPLOYE")
        .joinDate(LocalDateTime.now())
        .build();

        Employee savedEmployee = employeeRepository.save(employee);
        
        AuthToken authToken = AuthToken.builder()
        .refreshToken("someRefreshToken")
        .employee(savedEmployee)
        .expiredAt(LocalDateTime.now().plusDays(1))
        .build();

        AuthToken savedAuthToken = authTokenRepository.saveAndFlush(authToken);
        assertEquals(savedEmployee.getId(), savedAuthToken.getEmployee().getId());
        assertNotNull(savedAuthToken.getCreatedAt());
    }

    @Test
    @DisplayName("Should return saved entity when entity found")
    void findByRefreshToken_foundEntity_shouldReturnSavedEntity(){
        Employee employee = Employee.builder()
        .username("TESTEMPLOYE")
        .fullname("TESTEMPLOYE")
        .password("TESTEMPLOYE")
        .joinDate(LocalDateTime.now())
        .build();

        Employee savedEmployee = employeeRepository.save(employee);
        
        AuthToken authToken = AuthToken.builder()
        .refreshToken("someRefreshToken")
        .employee(savedEmployee)
        .expiredAt(LocalDateTime.now().plusDays(1))
        .build();

        authTokenRepository.saveAndFlush(authToken);
        Optional<AuthToken> byRefreshToken = authTokenRepository.findByRefreshToken("someRefreshToken");

        assertTrue(byRefreshToken.isPresent());
    }

    @Test
    @DisplayName("Should return empty when entity not found")
    void findByRefreshToken_notFound_shouldReturnSavedEntity(){
        Optional<AuthToken> byRefreshToken = authTokenRepository.findByRefreshToken("someRefreshToken");

        assertTrue(byRefreshToken.isEmpty());
    }

    @Test
    @DisplayName("Should delete entity")
    void deleteByRefreshToken_foundEntity_shouldReturnSavedEntity(){
        Employee employee = Employee.builder()
        .username("TESTEMPLOYE")
        .fullname("TESTEMPLOYE")
        .password("TESTEMPLOYE")
        .joinDate(LocalDateTime.now())
        .build();

        Employee savedEmployee = employeeRepository.save(employee);
        
        AuthToken authToken = AuthToken.builder()
        .refreshToken("someRefreshToken")
        .employee(savedEmployee)
        .expiredAt(LocalDateTime.now().plusDays(1))
        .build();

        authTokenRepository.saveAndFlush(authToken);
        authTokenRepository.deleteByRefreshToken("someRefreshToken");

        Optional<AuthToken> byRefreshToken = authTokenRepository.findByRefreshToken("someRefreshToken");
        assertTrue(byRefreshToken.isEmpty());
    }

}
