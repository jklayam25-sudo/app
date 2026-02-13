package lumi.insert.app.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated; 
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id; 
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor; 
import lumi.insert.app.entity.nondatabase.EmployeeRole;
import lumi.insert.app.entity.nondatabase.TimestampAuditing;

@Entity(name = "employees")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Data 
public class Employee extends TimestampAuditing{
    
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.UUID)
    private UUID id;

    @Column(unique = true, nullable = false, length = 55) 
    private String username;

    @Column(nullable = false)
    private String fullname;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private LocalDateTime joinDate;

    @Column(nullable = true)
    private String lastIp;
    
    @Column(nullable = true)
    private String lastDevice;

    @Builder.Default
    @Column(nullable = false)
    private boolean isActive = true;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private EmployeeRole role = EmployeeRole.CASHIER;

}
