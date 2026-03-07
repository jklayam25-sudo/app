package lumi.insert.app.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lumi.insert.app.entity.nondatabase.TimestampAuditing;

@Entity(name = "auth_tokens")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Data
public class AuthToken extends TimestampAuditing{

    @Id 
    private UUID id;

    @Column(nullable = false, unique = true)
    private String refreshToken;

    @OneToOne 
    private Employee employee;

    @Column(nullable = false)
    private LocalDateTime expiredAt;
}
