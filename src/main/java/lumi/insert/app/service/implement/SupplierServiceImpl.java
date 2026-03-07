package lumi.insert.app.service.implement;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.github.f4b6a3.uuid.UuidCreator;

import jakarta.transaction.Transactional;
import lumi.insert.app.dto.request.SupplierCreateRequest;
import lumi.insert.app.dto.request.SupplierGetByFilter;
import lumi.insert.app.dto.request.SupplierGetNameRequest;
import lumi.insert.app.dto.request.SupplierUpdateRequest;
import lumi.insert.app.dto.response.SupplierDetailResponse;
import lumi.insert.app.dto.response.SupplierNameResponse; 

import lumi.insert.app.entity.Supplier;
import lumi.insert.app.entity.nondatabase.SliceIndex;
import lumi.insert.app.exception.DuplicateEntityException;
import lumi.insert.app.exception.NotFoundEntityException;
import lumi.insert.app.repository.SupplierRepository;
import lumi.insert.app.service.SupplierService;
import lumi.insert.app.utils.generator.JpaSpecGenerator;
import lumi.insert.app.utils.mapper.SupplierMapper;

@Service
@Transactional
public class SupplierServiceImpl implements SupplierService{

    @Autowired
    SupplierRepository supplierRepository;

    @Autowired
    SupplierMapper supplierMapper;

    @Autowired
    JpaSpecGenerator jpaSpecGenerator;

    @Override
    public SupplierDetailResponse createSupplier(SupplierCreateRequest request) {
        if(supplierRepository.existsByName(request.getName())) throw new DuplicateEntityException("Supplier with name " + request.getName() + " already exists");

        Supplier supplier = Supplier.builder()
            .id(UuidCreator.getTimeOrderedEpochFast())
            .name(request.getName())
            .email(request.getEmail())
            .contact(request.getContact()) 
            .build();

        Supplier savedSupplier = supplierRepository.save(supplier);

        return supplierMapper.createDtoDetailResponseFromSupplier(savedSupplier);
    }

    @Override
    public SupplierDetailResponse getSupplier(UUID id) {
        Supplier supplier = supplierRepository.findById(id)
            .orElseThrow(() -> new NotFoundEntityException("Supplier with id " + id + " is not found"));

        return supplierMapper.createDtoDetailResponseFromSupplier(supplier);
    }

    @Override
    public Slice<SupplierDetailResponse> getSuppliers(SupplierGetByFilter request) {
        Pageable pageable = jpaSpecGenerator.pageable(request);

        Specification<Supplier> supplierSpecification = jpaSpecGenerator.supplierSpecification(request);

        Slice<Supplier> suppliers = supplierRepository.findAll(supplierSpecification, pageable);
        return suppliers.map(supplierMapper::createDtoDetailResponseFromSupplier);
    }

    @Override
    public SliceIndex<SupplierNameResponse> searchSupplierNames(SupplierGetNameRequest request) {
        if(request.getLastId() == null) request.setLastId(new UUID(0, 0));
        Pageable pageable = PageRequest.of(0, request.getSize()).withSort(Sort.by("id").ascending());
        
        Slice<SupplierNameResponse> suppliersName = supplierRepository.getByNameContainingIgnoreCaseAndIdAfter(request.getName(), request.getLastId(), pageable);;
        return new SliceIndex<SupplierNameResponse>(suppliersName);
    }

    @Override
    public SupplierDetailResponse updateSupplier(UUID id, SupplierUpdateRequest request) {
        if(request.getName() != null && supplierRepository.existsByName(request.getName())) throw new DuplicateEntityException("Supplier with name " + request.getName() + " already exists");

        Supplier supplier = supplierRepository.findById(id)
            .orElseThrow(() -> new NotFoundEntityException("Supplier with id " + id + " is not found"));

        supplierMapper.updateEntityFromDto(request, supplier);

        return supplierMapper.createDtoDetailResponseFromSupplier(supplier);
    }
    
}
