package com.est.runtime.post;

import com.est.runtime.board.Board;
import com.est.runtime.board.BoardRepository;
import com.est.runtime.post.dto.PostRequest;
import com.est.runtime.post.dto.PostResponse;
import com.est.runtime.s3.ImgUploadService;
import com.est.runtime.signup.entity.Member;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@Service
public class PostService {
    private final PostRepository postRepository;
    private final ImgUploadService imgUploadService;
    private final BoardRepository boardRepository;

    public Page<Post> findPosts(Pageable pageable) {
        Pageable sortedByCreatedAtDesc = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by(Sort.Direction.DESC, "createdAt")
        );

        return postRepository.findAll(sortedByCreatedAtDesc);
    }

    @Transactional
    public Post savePost(PostRequest request, List<MultipartFile> files, Member author, Board board) throws IOException {
        Post post = request.toEntity(author, board);
        if (files != null && !files.isEmpty()) {
            imgUploadService.uploadFiles(files, post);
        }
        return postRepository.save(post);
    }

    public Post findPost(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));
    }

    @Transactional
    public void deletePost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));
        imgUploadService.deleteFile(post.getImages());
        postRepository.deleteById(id);
    }

    @Transactional
    public Post updatePost(Long id, PostRequest request, List<MultipartFile> files) throws IOException {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));
        if (files != null && !files.isEmpty()) {
            imgUploadService.deleteFile(post.getImages());
            post.getImages().clear();

            imgUploadService.uploadFiles(files, post);
        }
        post.update(request.getTitle(), request.getContent());

        Long boardId = request.getBoardId();
        if (boardId != null) {
            Board board = boardRepository.findById(boardId)
                    .orElseThrow(() -> new IllegalArgumentException("Board not found with ID: " + boardId));

            post.setBoard(board);
        }

        return post;
    }

    public Page<Post> findPostsByBoardId(Long boardId, Pageable pageable) {
        return postRepository.findByBoardIdAndHiddenFalse(boardId, pageable);
    }

    public Page<Post> searchPostsByTitle(String keyword, Long boardId, Pageable pageable) {
        Pageable sortedByCreatedAtDesc = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by(Sort.Direction.DESC, "createdAt")
        );
        return postRepository.findByBoardIdAndTitleContainingIgnoreCaseAndHiddenFalse(boardId, keyword, sortedByCreatedAtDesc);
    }

    public Page<Post> searchPostsByTitle(String keyword, Pageable pageable) {
        Pageable sortedByCreatedAtDesc = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by(Sort.Direction.DESC, "createdAt")
        );
        return postRepository.findByTitleContainingIgnoreCaseAndHiddenFalse(keyword, sortedByCreatedAtDesc);
    }

    public Page<Post> searchPostsByAuthorNickname(String nickname, Pageable pageable) {
        Pageable sorted = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.DESC, "createdAt"));
        return postRepository.findByMemberNicknameContainingIgnoreCaseAndHiddenFalse(nickname, sorted);
    }

    public Page<Post> searchPostsByAuthorNickname(String nickname, Long boardId, Pageable pageable) {
        Pageable sorted = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.DESC, "createdAt"));
        return postRepository.findByBoardIdAndMemberNicknameContainingIgnoreCaseAndHiddenFalse(boardId, nickname, sorted);
    }

    @Transactional
    public void hidePost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));
        post.hide();
    }

    @Transactional
    public void unhidePost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));
        post.unhide();
    }

    public boolean toggleHidden(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));

        post.setHidden(!post.isHidden());
        postRepository.save(post);

        return post.isHidden();
    }







}
