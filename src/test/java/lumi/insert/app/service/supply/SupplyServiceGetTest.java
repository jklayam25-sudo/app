package lumi.insert.app.service.supply;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.github.f4b6a3.uuid.UuidCreator;

import org.springframework.data.domain.Page;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.domain.Specification;

import lumi.insert.app.core.entity.Supply;
import lumi.insert.app.core.entity.nondatabase.SupplyStatus;
import lumi.insert.app.dto.request.SupplyGetByFilter;
import lumi.insert.app.dto.response.SupplyDetailResponse;
import lumi.insert.app.dto.response.SupplyResponse;
import lumi.insert.app.exception.NotFoundEntityException;

public class SupplyServiceGetTest extends BaseSupplyServiceTest{
    
    @Test
    @DisplayName("Should thrown NotFoundEntity Exception when finding supply result in none at DB")
    public void getSupply_invalidId_thrownNotFoundEntity(){
        UUID randomUUID = UuidCreator.getTimeOrderedEpochFast();
        when(supplyRepositoryMock.findByIdDetail(randomUUID)).thenReturn(Optional.empty());

        assertThrows(NotFoundEntityException.class, () -> supplyServiceMock.getSupply(randomUUID));
    }

    @Test
    @DisplayName("Should return SupplyResponse DTO when finding supply result in succeed")
    public void getSupply_validId_returnSupplyResponse(){ 

        setupSupplyItem.setProduct(setupProduct);
        setupSupply.setSupplyItems(List.of(setupSupplyItem, setupSupplyItem));

        when(supplyRepositoryMock.findByIdDetail(setupSupply.getId())).thenReturn(Optional.of(setupSupply));

        SupplyDetailResponse supply = supplyServiceMock.getSupply(setupSupply.getId());
        assertEquals(setupSupply.getId(), supply.id());
        assertEquals(setupProduct.getId(), supply.supplyItems().getFirst().product().id());
        assertEquals(setupSupplyItem.getId(), supply.supplyItems().getFirst().id());
    }

    @Test
    @DisplayName("Should return Slice of filtered SupplyResponse DTO when finding supply result in succeed")
    public void searchSupplysByRequests_validFilter_returnSliceSupplyResponse(){ 

        Page<Supply> supplies = new PageImpl<>(List.of(setupSupply));
        when(jpaSpecGenerator.pageable(any())).thenReturn(PageRequest.of(0, 10));
        when(jpaSpecGenerator.supplySpecification(any())).thenReturn(Specification.anyOf(List.of()));
        when(supplyRepositoryMock.findAll(ArgumentMatchers.<Specification<Supply>>any(), any(Pageable.class))).thenReturn(supplies);

        Slice<SupplyResponse> result = supplyServiceMock.searchSuppliesByRequests(SupplyGetByFilter.builder().status(SupplyStatus.UNPAID).build());
        assertEquals(1, result.getNumberOfElements());
        assertEquals(setupSupply.getId(), result.getContent().getFirst().id());

        verify(jpaSpecGenerator, times(1)).supplySpecification(argThat(arg -> arg.getStatus() == SupplyStatus.UNPAID && arg.getSupplierId() == null));
    }

    @Test
    @DisplayName("Should return Slice of filtered SupplyResponse DTO when finding supply result in succeed")
    public void searchSupplysByRequests_notFoundAny_returnSliceSupplyResponse(){ 

        Page<Supply> supplies = new PageImpl<>(List.of());
        when(jpaSpecGenerator.pageable(any())).thenReturn(PageRequest.of(0, 10));
        when(jpaSpecGenerator.supplySpecification(any())).thenReturn(Specification.anyOf(List.of()));
        when(supplyRepositoryMock.findAll(ArgumentMatchers.<Specification<Supply>>any(), any(Pageable.class))).thenReturn(supplies);

        Slice<SupplyResponse> result = supplyServiceMock.searchSuppliesByRequests(SupplyGetByFilter.builder().status(SupplyStatus.UNPAID).build());
        assertEquals(0, result.getNumberOfElements());
        assertEquals(List.of(), result.getContent());

        verify(jpaSpecGenerator, times(1)).supplySpecification(argThat(arg -> arg.getStatus() == SupplyStatus.UNPAID && arg.getSupplierId() == null));
    }
}
