package lumi.insert.app.controller;
 
import java.net.URI;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Slice;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity; 
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.extern.slf4j.Slf4j;
import lumi.insert.app.controller.wrapper.WebResponse;
import lumi.insert.app.dto.request.EmployeeCreateRequest;
import lumi.insert.app.dto.request.EmployeeUpdateRequest;
import lumi.insert.app.dto.request.PaginationRequest;
import lumi.insert.app.dto.response.EmployeeResponse;
import lumi.insert.app.service.EmployeeService;

@RestController 
@Slf4j
public class EmployeeController {
    
    @Autowired
    EmployeeService employeeService;

    @PostMapping(
        path = "/api/employees",
        produces = MediaType.APPLICATION_JSON_VALUE,
        consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE
    )
    ResponseEntity<WebResponse<EmployeeResponse>> createEmployeeAPI(@Valid EmployeeCreateRequest request){
        EmployeeResponse resultFromService = employeeService.createEmployee(request);

        WebResponse<EmployeeResponse> wrappedResult = WebResponse.getWrapper(resultFromService, null);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
        .path("/" + resultFromService.id())
        .buildAndExpand(resultFromService.id())
        .toUri();

        return ResponseEntity.created(location).body(wrappedResult);
    }

    @GetMapping(
        path = "/api/employees/{id}",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<WebResponse<EmployeeResponse>> getEmployeeAPI(@PathVariable(name = "id") UUID id){
        EmployeeResponse resultFromService = employeeService.getEmployee(id);

        WebResponse<EmployeeResponse> wrappedResult = WebResponse.getWrapper(resultFromService, null);

        return ResponseEntity.ok(wrappedResult);
    }

    @GetMapping(
        path = "/api/employees",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<WebResponse<Slice<EmployeeResponse>>> getEmployeesAPI(@Valid @ModelAttribute PaginationRequest request){
        
        Slice<EmployeeResponse> resultFromService = employeeService.getEmployees(request);

        WebResponse<Slice<EmployeeResponse>> wrappedResult = WebResponse.getWrapper(resultFromService, null);
 
        return ResponseEntity.ok(wrappedResult);
    }

    @GetMapping(
        path = "/api/employees/exists",
        produces = MediaType.APPLICATION_JSON_VALUE,
        params = "username"
    )
    ResponseEntity<WebResponse<Boolean>> isUsernameExistsAPI(String username){
        boolean resultFromService = employeeService.isExistsEmployeeByUsername(username);

        WebResponse<Boolean> wrappedResult = WebResponse.getWrapper(resultFromService, null); 
        return ResponseEntity.ok(wrappedResult);
    }

    @PostMapping(
        path = "/api/employees/{id}/reset",
        produces = MediaType.APPLICATION_JSON_VALUE,
        consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE
    )
    ResponseEntity<WebResponse<EmployeeResponse>> resetEmployeePasswordAPI(@PathVariable(name = "id") UUID id,@Valid @RequestParam(name = "password") @Pattern(regexp = "^(?=.*[!@#$%^&*])[a-zA-Z0-9!@#$%^&*]{5,50}$", message = "password has to be 5-50 length and has atleast 1 unique char") String password){
        EmployeeResponse resultFromService = employeeService.resetEmployeePassword(id, password);

        WebResponse<EmployeeResponse> wrappedResult = WebResponse.getWrapper(resultFromService, null);
 
        return ResponseEntity.ok(wrappedResult);
    }

    @PatchMapping(
        path = "/api/employees/{id}",
        consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<WebResponse<EmployeeResponse>> updateEmployeeAPI(@PathVariable(name = "id") UUID id, @Valid EmployeeUpdateRequest request){ 
        EmployeeResponse resultFromService = employeeService.updateEmployee(id, request);

        WebResponse<EmployeeResponse> wrappedResult = WebResponse.getWrapper(resultFromService, null);
 
        return ResponseEntity.ok(wrappedResult);
    }

}
