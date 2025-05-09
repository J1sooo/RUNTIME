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
    Page<Post> findByBoard(Board board, Pageable pageable);
    Page<Post> findByBoardId(Long boardId, Pageable pageable);
}
