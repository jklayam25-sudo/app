package lumi.insert.app.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import jakarta.transaction.Transactional;
import lumi.insert.app.entity.Employee;
import lumi.insert.app.entity.Memo;
import lumi.insert.app.entity.MemoView;
import lumi.insert.app.entity.nondatabase.EmployeeRole;

@DataJpaTest
@Transactional 
public class MemoViewRepositoryTest {
    
    @Autowired
    MemoRepository memoRepository;

    @Autowired
    MemoViewRepository memoViewRepository;

    @Autowired
    EmployeeRepository employeeRepository;

    @Test
    void getMemo_roleALL_returnALL(){
        Memo cashierMemo = Memo.builder() 
        .title("Cashier")
        .body("Cashier body")
        .role(EmployeeRole.CASHIER)
        .build();
 
        Memo savedMemo = memoRepository.save(cashierMemo);;

        Employee employee = Employee.builder()
        .username("TESTEMPLOYE")
        .fullname("TESTEMPLOYE")
        .password("TESTEMPLOYE")
        .joinDate(LocalDateTime.now())
        .build();

        Employee savedEmployee = employeeRepository.save(employee);
 
        MemoView memoView = new MemoView(savedMemo, savedEmployee);
         memoViewRepository.saveAndFlush(memoView);

        assertEquals(1, memoViewRepository.deleteMemoView(savedMemo.getId())); 
        assertEquals(0, memoViewRepository.deleteMemoView(0L)); 
    }

}
