package com.est.runtime.signup.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.est.runtime.signup.entity.AccessAuthority;

@Repository
public interface AccessAuthorityRepository extends JpaRepository<AccessAuthority, Long> {
    Optional<AccessAuthority> findByName(String name);
}
