package com.est.runtime.post;

import com.est.runtime.board.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    Page<Post> findByBoardAndHiddenFalse(Board board, Pageable pageable);
    Page<Post> findByBoardIdAndHiddenFalse(Long boardId, Pageable pageable);
    Page<Post> findByTitleContainingIgnoreCaseAndHiddenFalse(String keyword, Pageable pageable);
    Page<Post> findByBoardIdAndTitleContainingIgnoreCaseAndHiddenFalse(Long boardId, String keyword, Pageable pageable);
    Page<Post> findByMemberNicknameContainingIgnoreCaseAndHiddenFalse(String nickname, Pageable pageable);
    Page<Post> findByBoardIdAndMemberNicknameContainingIgnoreCaseAndHiddenFalse(Long boardId, String nickname, Pageable pageable);

}
