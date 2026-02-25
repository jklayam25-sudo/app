package lumi.insert.app.service.memo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import lumi.insert.app.dto.response.MemoResponse;
import lumi.insert.app.entity.Memo;
import lumi.insert.app.entity.nondatabase.EmployeeRole;
import lumi.insert.app.exception.NotFoundEntityException;

public class MemoServiceGetTest extends BaseMemoServiceTest{
    
    @Test
    void getMemo_foundEntity_returnDTO(){
        Memo memo = Memo.builder()
        .id(1L)
        .body("Lorem Ipsum")
        .build();

        when(memoRepository.findById(1L)).thenReturn(Optional.of(memo));

        MemoResponse memo2 = memoService.getMemo(1L);

        assertEquals(memo.getBody(), memo2.body());
    }

    @Test
    void getMemo_notFoundEntity_returnDTO(){ 
        when(memoRepository.findById(1L)).thenReturn(Optional.empty());
 

        assertThrows(NotFoundEntityException.class, () -> memoService.getMemo(1L));
    }

    @Test
    void getMemos_foundEntity_returnSliceDTO(){ 
        MemoResponse memoResponse = MemoResponse.builder()
        .id(1L)
        .body("body")
        .isRead(true)
        .build();

        Slice<MemoResponse> slices = new SliceImpl<>(List.of(memoResponse));
        when(memoRepository.findActiveMemosByRoleOrPublic(any(UUID.class), any(EmployeeRole.class), any())).thenReturn(slices);
    
        Slice<MemoResponse> memos = memoService.getMemos(UUID.randomUUID(), EmployeeRole.CASHIER, LocalDateTime.now());;

        assertEquals(1, memos.getNumberOfElements());

        assertTrue(memos.getContent().getFirst().isRead());
    }

    @Test
    void getMemos_notFoundEntity_returnSliceDTO(){  

        Slice<MemoResponse> slices = new SliceImpl<>(List.of());
        when(memoRepository.findActiveMemosByRoleOrPublic(any(UUID.class), any(EmployeeRole.class), any())).thenReturn(slices);
    
        Slice<MemoResponse> memos = memoService.getMemos(UUID.randomUUID(), EmployeeRole.CASHIER, LocalDateTime.now());;

        assertEquals(0, memos.getNumberOfElements());

        assertEquals(List.of(), memos.getContent());
    }
}
