package lumi.insert.app.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lumi.insert.app.entity.nondatabase.SupplyStatus;
import lumi.insert.app.entity.nondatabase.TimestampAuditing; 

@Entity(name = "supplies")
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Supply extends TimestampAuditing{

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.UUID)
    private UUID id;

    @Column(unique = true, nullable = false, length = 55)
    private String invoiceId;

    @Builder.Default 
    private Long totalItems = 0L;

    @Builder.Default
    private Long totalFee = 0L;

    @Builder.Default
    private Long totalDiscount = 0L;

    @Builder.Default
    private Long subTotal = 0L;

    @Builder.Default
    private Long grandTotal = 0L;

    @Builder.Default
    private Long totalUnpaid = 0L;

    @Builder.Default
    private Long totalPaid = 0L;

    @Builder.Default
    private Long totalUnrefunded = 0L;

    @Builder.Default
    private Long totalRefunded = 0L;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private SupplyStatus status = SupplyStatus.UNPAID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_id", nullable = false) 
    private Supplier supplier;

    @OneToMany(mappedBy = "supply")
    @Builder.Default
    @ToString.Exclude
    private List<SupplyItem> supplyItems = new ArrayList<>();

    @OneToMany(mappedBy = "supply")
    @Builder.Default
    @ToString.Exclude
    private List<SupplyPayment> supplyPayments = new ArrayList<>();
    
}
