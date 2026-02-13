package lumi.insert.app.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import lumi.insert.app.entity.AuthToken;

public interface AuthTokenRepository extends JpaRepository<AuthToken, UUID> {
    
    Optional<AuthToken> findByRefreshToken(String token);

    void deleteByRefreshToken(String token);
    
}
