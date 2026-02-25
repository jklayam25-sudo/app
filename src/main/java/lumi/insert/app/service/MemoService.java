package lumi.insert.app.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.domain.Slice;

import lumi.insert.app.dto.request.MemoCreateRequest;
import lumi.insert.app.dto.request.MemoUpdateRequest;
import lumi.insert.app.dto.response.MemoResponse;
import lumi.insert.app.entity.nondatabase.EmployeeRole;

public interface MemoService {
    MemoResponse createMemo(MemoCreateRequest request);

    MemoResponse updateMemo(Long id, MemoUpdateRequest request);

    MemoResponse archiveMemo(Long id);

    MemoResponse getMemo(Long id);

    Slice<MemoResponse> getMemos(UUID employeeId, EmployeeRole role, LocalDateTime time);

    Boolean createMemoView(Long id, UUID employeeId);
}
