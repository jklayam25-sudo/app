package lumi.insert.app.core.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lumi.insert.app.core.entity.nondatabase.BaseAuditing; 

@Entity(name = "auth_tokens")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Data
@Audited
public class AuthToken extends BaseAuditing{

    @Id 
    @NotAudited
    private UUID id;

    @Column(nullable = false, unique = true)
    private String refreshToken;

    @OneToOne 
    @NotAudited
    private Employee employee;

    @Column(nullable = false)
    private LocalDateTime expiredAt;
}
