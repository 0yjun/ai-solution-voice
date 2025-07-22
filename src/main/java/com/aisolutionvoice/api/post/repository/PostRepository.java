package com.aisolutionvoice.api.post.repository;

import com.aisolutionvoice.api.Board.entity.Board;
import com.aisolutionvoice.api.member.entity.Member;
import com.aisolutionvoice.api.post.dto.PostDetailDto;
import com.aisolutionvoice.api.post.dto.PostSummaryDto;
import com.aisolutionvoice.api.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PostRepository  extends JpaRepository<Post, Long> {
    @Query("""
    SELECT new com.aisolutionvoice.api.post.dto.PostSummaryDto(p.id, p.title, m.loginId, p.createdAt)
    FROM Post p
    JOIN p.member m
    WHERE p.board.id = :boardId
""")
    Page<PostSummaryDto> findSummaryByBoardId(@Param("boardId") Long boardId, Pageable pageable);

//    @Query("""
//    SELECT new com.aisolutionvoice.api.post.dto.PostDetailDto(p.id, p.title, m.loginId, p.createdAt, b.scripts)
//    FROM Post p
//    JOIN p.member m
//    join fetch p.board b
//    WHERE p.id = :postId
//""")
//    PostDetailDto findByPostIdWithScriptAndVoiceData(@Param("postId") Long postId);

    Optional<Post> findByMemberAndBoard(Member member, Board board);
}
