package com.est.runtime.signup.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.est.runtime.signup.entity.AuthorityForLevel;

@Repository
public interface AuthorityForLevelRepository extends JpaRepository<AuthorityForLevel, Long> {
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM AuthorityForLevel a WHERE a.userLevel.id = :levelId AND a.authority.id = :authorityId")
    void deleteAuthorityForLevel(@Param("levelId") Long levelId, @Param("authorityId") Long authorityId);
}
