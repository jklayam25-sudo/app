package lumi.insert.app.core.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import lumi.insert.app.core.entity.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, UUID>{
    Optional<Employee> findByUsername(String username);

    boolean existsByUsername(String username);
}   
