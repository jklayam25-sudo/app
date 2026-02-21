package lumi.insert.app.service.implement;
 
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable; 
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lumi.insert.app.dto.request.EmployeeCreateRequest;
import lumi.insert.app.dto.request.EmployeeUpdateRequest;
import lumi.insert.app.dto.request.PaginationRequest;
import lumi.insert.app.dto.response.EmployeeResponse;
import lumi.insert.app.entity.Employee;
import lumi.insert.app.exception.DuplicateEntityException;
import lumi.insert.app.exception.NotFoundEntityException;
import lumi.insert.app.repository.AuthTokenRepository;
import lumi.insert.app.repository.EmployeeRepository;
import lumi.insert.app.service.EmployeeService;
import lumi.insert.app.utils.mapper.EmployeeMapper;

@Service
@Transactional
public class EmployeeServiceImpl implements EmployeeService{

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    AuthTokenRepository authTokenRepository;

    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    @Autowired
    EmployeeMapper employeeMapper;

    @Override
    public EmployeeResponse createEmployee(EmployeeCreateRequest request) {
        if(employeeRepository.existsByUsername(request.getUsername())) throw new DuplicateEntityException("Employee with username " + request.getUsername() + " already exists");

        String encodedPassword = passwordEncoder.encode(request.getPassword());

        Employee employee = Employee.builder()
            .username(request.getUsername())
            .fullname(request.getFullname())
            .password(encodedPassword)
            .build();
        
        Employee savedEmployee = employeeRepository.save(employee);
        return employeeMapper.createDtoResponseFromEmployee(savedEmployee);
    }

    @Override
    public EmployeeResponse getEmployee(UUID id) {
        Employee employee = employeeRepository.findById(id)
            .orElseThrow(() -> new NotFoundEntityException("Employee with ID " + id + " was not found"));

        return employeeMapper.createDtoResponseFromEmployee(employee);
    }

    @Override
    public Slice<EmployeeResponse> getEmployees(PaginationRequest request) {
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), Sort.by("createdAt").descending());
        Slice<Employee> employees = employeeRepository.findAll(pageable);

        return employees.map(employeeMapper::createDtoResponseFromEmployee);
    }

    @Override
    public boolean isExistsEmployeeByUsername(String username) {
        return employeeRepository.existsByUsername(username);
    }

    @Override
    public EmployeeResponse resetEmployeePassword(UUID id, String password) {
        Employee employee = employeeRepository.findById(id)
            .orElseThrow(() -> new NotFoundEntityException("Employee with ID " + id + " was not found"));

        String encodedPassword = passwordEncoder.encode(password);

        employee.setPassword(encodedPassword);
        
        Employee savedEmployee = employeeRepository.save(employee);

        authTokenRepository.deleteByEmployeeId(employee.getId());
        return employeeMapper.createDtoResponseFromEmployee(savedEmployee);
    }

    @Override
    public EmployeeResponse updateEmployee(UUID id, EmployeeUpdateRequest request) {
        Employee employee = employeeRepository.findById(id)
            .orElseThrow(() -> new NotFoundEntityException("Employee with ID " + id + " was not found"));

        if(request.getUsername() != null){
            if(employeeRepository.existsByUsername(request.getUsername())) throw new DuplicateEntityException("Employee with username " + request.getUsername() + " already exists");  
        }

        employeeMapper.updateEmployeeFromDto(request, employee);

        if(!employee.isActive()) authTokenRepository.deleteByEmployeeId(employee.getId());

        return employeeMapper.createDtoResponseFromEmployee(employee);
    }
    
}
