package lumi.insert.app.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
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
import lumi.insert.app.entity.nondatabase.BaseAuditing;
import lumi.insert.app.entity.nondatabase.TransactionStatus;

@Entity(name = "transactions")
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Audited
public class Transaction extends BaseAuditing{

    @Id 
    private UUID id;

    @Column(unique = true, nullable = false, length = 55)
    private String invoiceId;

    @Builder.Default 
    @NotAudited
    private Long totalItems = 0L;

    @Builder.Default
    @NotAudited
    private Long totalFee = 0L;

    @Builder.Default
    @NotAudited
    private Long totalDiscount = 0L;

    @Builder.Default
    @NotAudited
    private Long subTotal = 0L;

    @Builder.Default
    @NotAudited
    private Long grandTotal = 0L;

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

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private TransactionStatus status = TransactionStatus.PENDING;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false) 
    @NotAudited
    private Customer customer;

    @NotAudited
    private String customerName;

    @OneToMany(mappedBy = "transaction")
    @Builder.Default
    @ToString.Exclude
    @NotAudited
    private List<TransactionItem> transactionItems = new ArrayList<>();

    @OneToMany(mappedBy = "transaction")
    @Builder.Default
    @ToString.Exclude
    @NotAudited
    private List<TransactionPayment> transactionPayments = new ArrayList<>();
    
}
