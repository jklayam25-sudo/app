package lumi.insert.app.entity;

import java.util.UUID;

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
import lumi.insert.app.entity.nondatabase.BaseAuditing;

@Entity(name = "transaction_items")
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
@Builder 
public class TransactionItem extends BaseAuditing{

    @Id 
    private UUID id;

    @Builder.Default 
    private Long price = 0L;

    @Builder.Default
    private Long quantity = 0L;

    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false, unique = true) 
    private Product product;

    private String productName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transaction_id", nullable = false) 
    private Transaction transaction;
    

}
