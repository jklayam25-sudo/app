package lumi.insert.app.service.supplier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import lumi.insert.app.dto.request.SupplierCreateRequest;
import lumi.insert.app.dto.response.SupplierDetailResponse;
import lumi.insert.app.entity.Supplier;
import lumi.insert.app.exception.DuplicateEntityException; 

public class SupplierServiceCreateTest extends BaseSupplierServiceTest{
    
    @Test
    @DisplayName("should rerutn Supplier Detail DTO when create entity success")
    void createSupplier_validReq_ReturnDTO(){
        when(supplierRepository.existsByName(anyString())).thenReturn(false);
        when(supplierRepository.save(any(Supplier.class))).then(arg -> arg.getArgument(0));

        SupplierCreateRequest request = SupplierCreateRequest.builder()
        .name(setupSupplier.getName())
        .contact(setupSupplier.getContact()) 
        .build();

        SupplierDetailResponse Supplier = supplierServiceMock.createSupplier(request);
        assertNull(Supplier.email());
        assertEquals(setupSupplier.getEmail(), Supplier.email());
        verify(supplierRepository, times(1)).save(argThat(arg -> arg.getName().equals(setupSupplier.getName())));
    }

    @Test
    @DisplayName("should rerutn Supplier Detail DTO when create entity success")
    void createSupplier_nameDuplicate_ThrowDuplicate(){
        when(supplierRepository.existsByName(anyString())).thenReturn(true); 

        SupplierCreateRequest request = SupplierCreateRequest.builder()
        .name(setupSupplier.getName())
        .contact(setupSupplier.getContact()) 
        .build();

        assertThrows(DuplicateEntityException.class, () -> supplierServiceMock.createSupplier(request)); 
    }
}
