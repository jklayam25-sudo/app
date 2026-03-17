package lumi.insert.app.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lumi.insert.app.entity.nondatabase.BaseAuditing;

@Entity(name = "suppliers")
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Audited
public class Supplier extends BaseAuditing{
    
    @Id 
    private UUID id;

    @Column(unique = true, nullable = false )
    private String name;
 
    private String email;

    @Column(nullable = false )
    private String contact;  

    @Builder.Default 
    private Boolean isActive = true;

    @Builder.Default 
    @NotAudited
    private Long totalTransaction = 0L;

    @Builder.Default
    @NotAudited
    private Long totalUnpaid = 0L;

    @Builder.Default
    @NotAudited
    private Long totalPaid = 0L;

    @Builder.Default
    @NotAudited
    private Long totalUnrefunded = 0L;

    @Builder.Default
    @NotAudited
    private Long totalRefunded = 0L;

    @OneToMany(mappedBy = "supplier")
    @Builder.Default
    @ToString.Exclude
    @NotAudited
    private List<Supply> supplies = new ArrayList<>();

    public void addTransaction(){
        this.totalTransaction ++;
    }
}
