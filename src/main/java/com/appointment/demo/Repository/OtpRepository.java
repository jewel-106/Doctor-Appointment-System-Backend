
package com.appointment.demo.Repository;

import com.appointment.demo.model.OtpStore;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface OtpRepository extends JpaRepository<OtpStore, Long> {
    Optional<OtpStore> findByEmailAndUsedAtIsNull(String email);
    @Modifying
    @Transactional
    @Query("DELETE FROM OtpStore o WHERE o.email = :email")
    void deleteByEmail(@Param("email") String email);
    Optional<OtpStore> findTopByEmailOrderByExpiresAtDesc(String email);
}