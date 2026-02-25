package lumi.insert.app.service.implement;

import java.time.LocalDateTime; 
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import lumi.insert.app.dto.request.MemoCreateRequest;
import lumi.insert.app.dto.request.MemoUpdateRequest;
import lumi.insert.app.dto.response.MemoResponse;
import lumi.insert.app.entity.Employee;
import lumi.insert.app.entity.Memo;
import lumi.insert.app.entity.MemoView;
import lumi.insert.app.entity.nondatabase.EmployeeRole;
import lumi.insert.app.exception.NotFoundEntityException;
import lumi.insert.app.repository.EmployeeRepository;
import lumi.insert.app.repository.MemoRepository;
import lumi.insert.app.repository.MemoViewRepository;
import lumi.insert.app.service.MemoService;
import lumi.insert.app.utils.mapper.MemoMapper;

@Service
@Slf4j
public class MemoServiceImpl implements MemoService{

    @Autowired
    MemoRepository memoRepository;

    @Autowired
    MemoViewRepository memoViewRepository;

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    MemoMapper mapper;

    @Override
    public MemoResponse createMemo(MemoCreateRequest request) {
        Memo memo = Memo.builder()
        .title(request.getTitle())
        .body(request.getBody())
        .build();

        Memo savedMemo = memoRepository.save(memo);

        if(!(request.getImages() == null)){
            // Call method to process the image
        }

        return mapper.createDtoResponseFromMemo(savedMemo);
    }

    @Override
    public MemoResponse updateMemo(Long id, MemoUpdateRequest request) {
        Memo memo = memoRepository.findById(id)
            .orElseThrow(() -> new NotFoundEntityException("Memo with ID " + id + " was not found"));
        
        mapper.updateEntityFromDto(request, memo);

        memoViewRepository.deleteMemoView(id);

        return mapper.createDtoResponseFromMemo(memo);
    }

    @Override
    public MemoResponse archiveMemo(Long id) {
        Memo memo = memoRepository.findById(id)
            .orElseThrow(() -> new NotFoundEntityException("Memo with ID " + id + " was not found"));

        memo.setIsActive(false);
        return mapper.createDtoResponseFromMemo(memo);
    }

    @Override
    public MemoResponse getMemo(Long id) {
        Memo memo = memoRepository.findById(id)
            .orElseThrow(() -> new NotFoundEntityException("Memo with ID " + id + " was not found"));
 
        return mapper.createDtoResponseFromMemo(memo);
    }

    @Override
    public Slice<MemoResponse> getMemos(UUID employeeId, EmployeeRole role, LocalDateTime time) {
        return memoRepository.findActiveMemosByRoleOrPublic(employeeId, role, time);
    }

    @Override
    public Boolean createMemoView(Long id, UUID employeeId) {
        try {
            Memo memo = memoRepository.getReferenceById(id);
            Employee employee = employeeRepository.getReferenceById(employeeId);
            MemoView memoView = new MemoView(memo, employee);
            memoViewRepository.save(memoView);
            return true;
        } catch (Exception e) {
            log.info("{}", e.getLocalizedMessage());
            return false;
        }
    }
    
}
