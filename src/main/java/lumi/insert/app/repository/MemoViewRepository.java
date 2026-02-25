package lumi.insert.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import lumi.insert.app.entity.MemoView;

@Repository
public interface MemoViewRepository extends JpaRepository<MemoView, String>{
    
    @Modifying
    @Query("DELETE FROM memo_views mv WHERE mv.memo.id = :id")
    int deleteMemoView(@Param("id") Long id);

}
