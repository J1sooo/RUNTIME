package com.est.runtime.signup.repository;

import com.est.runtime.signup.entity.UserLevel;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserLevelRepository extends JpaRepository<UserLevel, Long> {
    Optional<UserLevel> findByLevelName(String levelName);
    Optional<UserLevel> findByLevelNumber(Integer levelNumber);
    List<UserLevel> findByLevelNumberLessThanEqual(Integer levelNumber);
}