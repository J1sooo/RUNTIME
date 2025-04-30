package com.est.runtime.signup.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.est.runtime.signup.entity.AuthorityForLevel;

@Repository
public interface AuthorityForLevelRepository extends JpaRepository<AuthorityForLevel, Long> {
}
