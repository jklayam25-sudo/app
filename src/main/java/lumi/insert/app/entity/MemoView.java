package lumi.insert.app.entity;
 

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lumi.insert.app.entity.nondatabase.TimestampAuditing;

@Entity(name = "memo_views")
@Table(
     uniqueConstraints = @UniqueConstraint(columnNames = {"memo_id", "employee_id"})
)
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemoView extends TimestampAuditing{

    @Id
    @Column(nullable = false)
    String id;

    @ManyToOne(fetch = FetchType.LAZY) 
    @JoinColumn(name = "memo_id", nullable = false) 
    private Memo memo;

    @ManyToOne(fetch = FetchType.LAZY) 
    @JoinColumn(name = "employee_id", nullable = false) 
    private Employee employee;

    public MemoView (Memo memo, Employee employee){
        this(memo.getId().toString() + employee.getId(), memo, employee);
    }

}
