package lumi.insert.app.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lumi.insert.app.entity.nondatabase.TimestampAuditing;

@Entity(name = "customers")
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Customer extends TimestampAuditing{
    
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.UUID)
    private UUID id;

    @Column(unique = true, nullable = false )
    private String name;
 
    private String email;

    @Column(nullable = false )
    private String contact;

    @Column(nullable = false )
    private String shippingAddress;

    private Double Latitude;

    private Double Longitude;

    @Builder.Default 
    private Boolean isActive = true;

    @Builder.Default 
    private Long totalTransaction = 0L;

    @Builder.Default
    private Long totalUnpaid = 0L;

    @Builder.Default
    private Long totalPaid = 0L;

    @OneToMany(mappedBy = "customer")
    @Builder.Default
    @ToString.Exclude
    private List<Transaction> transactions = new ArrayList<>();
}
