package lumi.insert.app.repository;

import java.time.LocalDateTime;
import java.util.List; 

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import lumi.insert.app.entity.Memo; 
import lumi.insert.app.entity.nondatabase.EmployeeRole;

@Repository
public interface MemoRepository extends JpaRepository<Memo, Long>{
     
    @Query("SELECT m FROM memos m WHERE m.isActive = true " +
        "AND (m.role = :role OR m.role IS NULL) " +
        "AND m.updatedAt > :time ORDER BY m.updatedAt ASC")
    List<Memo> findActiveMemosByRoleOrPublic(@Param("role") EmployeeRole role, 
                                         @Param("time") LocalDateTime time);

    @Modifying
    @Query("UPDATE memos m SET m.isActive = false WHERE m.id = :id")
    int updateIsActiveFalse(@Param("id") Long id);

}
