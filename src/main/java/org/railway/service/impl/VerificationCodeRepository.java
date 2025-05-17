package org.railway.service.impl;

import io.lettuce.core.dynamic.annotation.Param;
import org.railway.entity.VerificationCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.Optional;

public interface VerificationCodeRepository extends JpaRepository<VerificationCode, Long> {
    Optional<VerificationCode> findByEmail(String email);

    @Modifying
    @Query("UPDATE VerificationCode v SET v.isUsed = 1 WHERE v.email = :email")
    void updateIsUsedByEmail(String email);

    @Modifying
    @Query("UPDATE VerificationCode v SET v.code = :code, v.expireTime = :expireTime WHERE v.email = :email")
    int updateCodeAndExpireTimeByEmail(@Param("email") String email,
                                        @Param("code") String code,
                                        @Param("expireTime") LocalDateTime expireTime);

}
