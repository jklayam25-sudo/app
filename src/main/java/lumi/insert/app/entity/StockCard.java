package lumi.insert.app.entity;

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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lumi.insert.app.entity.nondatabase.StockMove;
import lumi.insert.app.entity.nondatabase.TimestampAuditing;

@Entity(name = "stock_cards")
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StockCard extends TimestampAuditing{
    
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.UUID)
    UUID id;

    @Column(nullable = false)
    UUID referenceId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false, unique = true) 
    private Product product;

    @Column(nullable = false) 
    private String productName;

    @Column(nullable = false)    
    private Long quantity;

    @Column(nullable = false)    
    private Long oldStock;

    @Column(nullable = false)    
    private Long newStock;

    @Column(nullable = false)    
    private Long basePrice;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)   
    private StockMove type;

    private String description;
}
