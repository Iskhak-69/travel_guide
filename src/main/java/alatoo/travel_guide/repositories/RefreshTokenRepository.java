package alatoo.travel_guide.repositories;

import alatoo.travel_guide.entities.RefreshToken;
import alatoo.travel_guide.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);
    Optional<RefreshToken> findByUser(UserEntity user);
    void deleteByUser(UserEntity user);
}
