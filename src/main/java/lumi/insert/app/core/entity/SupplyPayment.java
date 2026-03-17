package lumi.insert.app.core.entity;
import java.util.UUID;

import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType; 
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lumi.insert.app.core.entity.nondatabase.BaseAuditing;

@Entity(name = "supply_payments")
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Audited
public class SupplyPayment extends BaseAuditing{
  
    @Id 
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supply_id", nullable = false)
    @NotAudited
    private Supply supply;

    @Column(nullable = false)
    private Long totalPayment;

    @Column(nullable = false)
    private String paymentFrom;

    @Column(nullable = false)
    private String paymentTo;

    @Builder.Default
    private Boolean isForRefund = false;

}
