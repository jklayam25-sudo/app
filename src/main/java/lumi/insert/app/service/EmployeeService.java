package lumi.insert.app.service;

import java.util.UUID;

import org.springframework.data.domain.Slice;

import lumi.insert.app.dto.request.EmployeeCreateRequest;
import lumi.insert.app.dto.request.PaginationRequest;
import lumi.insert.app.dto.response.EmployeeResponse;

public interface EmployeeService {
    
    EmployeeResponse createEmployee(EmployeeCreateRequest request);

    EmployeeResponse getEmployee(UUID id);

    Slice<EmployeeResponse> getEmployees(PaginationRequest request);

    boolean isExistsEmployeeByUsername(String username);

    EmployeeResponse resetEmployeePassword(UUID id, String password);

    EmployeeResponse updateEmployee(UUID id, String password);
}
