package lumi.insert.app.entity;
 

import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import jakarta.persistence.Column; 
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
import lumi.insert.app.entity.nondatabase.BaseAuditing;

@Entity(name = "memo_views")
@Table(
     uniqueConstraints = @UniqueConstraint(columnNames = {"memo_id", "employee_id"})
)
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Audited
public class MemoView extends BaseAuditing{

    @Id
    @Column(nullable = false)
    String id;

    @ManyToOne(fetch = FetchType.LAZY) 
    @JoinColumn(name = "memo_id", nullable = false) 
    private Memo memo;

    @ManyToOne(fetch = FetchType.LAZY) 
    @JoinColumn(name = "employee_id", nullable = false) 
    @NotAudited
    private Employee employee;

    public MemoView (Memo memo, Employee employee){
        this(memo.getId().toString() + employee.getId(), memo, employee);
    }

}
