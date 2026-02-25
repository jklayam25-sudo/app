package lumi.insert.app.service.memo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;

import lumi.insert.app.dto.request.MemoUpdateRequest;
import lumi.insert.app.dto.response.MemoResponse;
import lumi.insert.app.entity.Memo;
import lumi.insert.app.exception.NotFoundEntityException;

public class MemoServiceEditTest extends BaseMemoServiceTest{
    
    @Test
    void updateMemo_validRequest_returnUpdatedDTO(){
        Memo memo = Memo.builder()
        .id(1L)
        .body("Lorem Ipsum")
        .build();

        when(memoRepository.findById(1L)).thenReturn(Optional.of(memo));
        
        MemoUpdateRequest request = MemoUpdateRequest.builder()
        .body("revised")
        .build();

        MemoResponse updateMemo = memoService.updateMemo(1L, request);
        assertEquals(request.getBody(), updateMemo.body());
        assertEquals(1L, updateMemo.id());

        verify(memoViewRepository, times(1)).deleteMemoView(1L);
    }

    @Test
    void updateMemo_notFound_throwNotFound(){ 
        when(memoRepository.findById(1L)).thenReturn(Optional.empty());
        
        MemoUpdateRequest request = MemoUpdateRequest.builder()
        .body("revised")
        .build();

        assertThrows(NotFoundEntityException.class, () -> memoService.updateMemo(1L, request)); 
    }

    @Test
    void archiveMemo_validRequest_throwNotFound(){ 
        Memo memo = Memo.builder()
        .id(1L)
        .body("Lorem Ipsum")
        .build();

        when(memoRepository.findById(1L)).thenReturn(Optional.of(memo));

         memoService.archiveMemo(1L);
        verify(memoMapper, times(1)).createDtoResponseFromMemo(argThat(arg -> arg.getIsActive() == false));
    }

    @Test
    void archiveMemo_notFound_throwNotFound(){ 
        when(memoRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundEntityException.class, () -> memoService.archiveMemo(1L)); 
    }
}
