package com.est.runtime.note;

import com.est.runtime.signup.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {
    List<Note> findByReceiverIdOrderBySentAtDesc(Long receiverId);
    List<Note> findBySenderIdOrderBySentAtDesc(Long senderId);

    boolean existsByReceiverAndIsReadFalse(Member receiver);
    List<Note> findAllByReceiverAndIsReadFalse(Member receiver);
}
