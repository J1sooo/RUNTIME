package com.est.runtime.signup.repository;

import com.est.runtime.signup.entity.UserLevel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserLevelRepository extends JpaRepository<UserLevel, Long> {
    UserLevel findByLevelNumber(int levelName);
}