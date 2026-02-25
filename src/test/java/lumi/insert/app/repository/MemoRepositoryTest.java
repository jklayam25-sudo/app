package lumi.insert.app.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.data.domain.Slice;

import jakarta.transaction.Transactional;
import lumi.insert.app.dto.response.MemoResponse; 
import lumi.insert.app.entity.Employee;
import lumi.insert.app.entity.Memo;
import lumi.insert.app.entity.MemoView;
import lumi.insert.app.entity.nondatabase.EmployeeRole;

@DataJpaTest
@Transactional
public class MemoRepositoryTest {
    
    @Autowired
    MemoRepository memoRepository;

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    MemoViewRepository memoViewRepository;

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

        Slice<MemoResponse> activeMemosByRoleOrPublic = memoRepository.findActiveMemosByRoleOrPublic(null, EmployeeRole.FINANCE, LocalDateTime.now().minusHours(1));

        assertEquals(2, activeMemosByRoleOrPublic.getNumberOfElements());
        assertEquals(financeMemo.getBody(), activeMemosByRoleOrPublic.getContent().getFirst().body()); 
        assertEquals(allMemo.getBody(), activeMemosByRoleOrPublic.getContent().getLast().body());
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

        Slice<MemoResponse> activeMemosByRoleOrPublic = memoRepository.findActiveMemosByRoleOrPublic(null, EmployeeRole.CASHIER, LocalDateTime.now().minusHours(1));

        assertEquals(1, activeMemosByRoleOrPublic.getNumberOfElements()); 
        assertEquals(allMemo.getBody(), activeMemosByRoleOrPublic.getContent().getLast().body());
        assertFalse(activeMemosByRoleOrPublic.getContent().getFirst().isRead());
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

    @Test
    void findActiveMemosByRoleOrPublic_isReadTest_returnTrue(){
        Memo cashierMemo = Memo.builder() 
        .title("Cashier") 
        .body("Cashier body")
        .role(EmployeeRole.CASHIER)
        .build();
 
        Memo savedMemo = memoRepository.saveAndFlush(cashierMemo);;

        Employee employee = Employee.builder()
        .username("TESTEMPLOYE")
        .fullname("TESTEMPLOYE")
        .password("TESTEMPLOYE")
        .joinDate(LocalDateTime.now())
        .build();

        Employee savedEmployee = employeeRepository.saveAndFlush(employee);

        memoViewRepository.saveAndFlush(new MemoView(savedMemo, savedEmployee));

        Slice<MemoResponse> activeMemosByRoleOrPublic = memoRepository.findActiveMemosByRoleOrPublic(savedEmployee.getId(), EmployeeRole.CASHIER, LocalDateTime.now().minusHours(1));

        assertEquals(1, activeMemosByRoleOrPublic.getNumberOfElements());  
        assertTrue(activeMemosByRoleOrPublic.getContent().getFirst().isRead());
    }
} 
