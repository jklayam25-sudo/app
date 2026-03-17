package lumi.insert.app.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import com.github.f4b6a3.uuid.UuidCreator;

import jakarta.transaction.Transactional;
import lumi.insert.app.core.entity.Employee;
import lumi.insert.app.core.entity.Memo;
import lumi.insert.app.core.entity.MemoView;
import lumi.insert.app.core.entity.nondatabase.EmployeeRole;
import lumi.insert.app.core.repository.EmployeeRepository;
import lumi.insert.app.core.repository.MemoRepository;
import lumi.insert.app.core.repository.MemoViewRepository;

@DataJpaTest
@Transactional 
@ActiveProfiles("test")
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
        .id(UuidCreator.getTimeOrderedEpochFast())
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
