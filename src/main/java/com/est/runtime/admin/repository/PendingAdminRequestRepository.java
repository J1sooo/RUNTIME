package com.est.runtime.admin.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.est.runtime.admin.entity.PendingAdminAuthorityRequest;

public interface PendingAdminRequestRepository extends JpaRepository<PendingAdminAuthorityRequest, Long> {
    List<PendingAdminAuthorityRequest> findAllByMemberId(Long memberId);
}
