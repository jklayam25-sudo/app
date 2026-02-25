package lumi.insert.app.repository;

import static org.junit.jupiter.api.Assertions.assertEquals; 

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import jakarta.transaction.Transactional;
import lumi.insert.app.entity.Memo;
import lumi.insert.app.entity.nondatabase.EmployeeRole;

@DataJpaTest
@Transactional
public class MemoRepositoryTest {
    
    @Autowired
    MemoRepository memoRepository;

    @Test
    void getMemo_roleALL_returnALL(){
        Memo cashierMemo = Memo.builder() 
        .title("Cashier")
        .body("Cashier body")
        .role(EmployeeRole.CASHIER)
        .build();

        Memo financeMemo = Memo.builder()
        .title("FINANCE")
        .body("FINANCE body")
        .role(EmployeeRole.FINANCE)
        .build();

        Memo allMemo = Memo.builder()
        .title("ALL")
        .body("ALL body") 
        .build();

        memoRepository.saveAllAndFlush(List.of(cashierMemo, financeMemo, allMemo));

        List<Memo> activeMemosByRoleOrPublic = memoRepository.findActiveMemosByRoleOrPublic(EmployeeRole.FINANCE, LocalDateTime.now().minusHours(1));

        assertEquals(2, activeMemosByRoleOrPublic.size());
        assertEquals(financeMemo.getBody(), activeMemosByRoleOrPublic.getFirst().getBody());
        assertEquals(allMemo.getBody(), activeMemosByRoleOrPublic.getLast().getBody());
    }

    @Test
    void getMemo_isActive_returnALL(){
        Memo cashierMemo = Memo.builder() 
        .title("Cashier")
        .isActive(false)
        .body("Cashier body")
        .role(EmployeeRole.CASHIER)
        .build();


        Memo allMemo = Memo.builder()
        .title("ALL")
        .body("ALL body") 
        .build();

        memoRepository.saveAllAndFlush(List.of(cashierMemo, allMemo));

        List<Memo> activeMemosByRoleOrPublic = memoRepository.findActiveMemosByRoleOrPublic(EmployeeRole.CASHIER, LocalDateTime.now().minusHours(1));

        assertEquals(1, activeMemosByRoleOrPublic.size()); 
        assertEquals(allMemo.getBody(), activeMemosByRoleOrPublic.getLast().getBody());
    }

    @Test
    void updateIsActiveFalse_isActive_returnALL(){
        Memo cashierMemo = Memo.builder() 
        .title("Cashier") 
        .body("Cashier body")
        .role(EmployeeRole.CASHIER)
        .build(); 

        Memo saveAndFlush = memoRepository.saveAndFlush(cashierMemo);;

        int updateIsActiveFalse = memoRepository.updateIsActiveFalse(saveAndFlush.getId());

        assertEquals(1, updateIsActiveFalse);
    }
} 
