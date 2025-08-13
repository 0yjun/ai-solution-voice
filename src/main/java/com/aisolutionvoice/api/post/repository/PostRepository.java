package com.aisolutionvoice.api.post.repository;

import com.aisolutionvoice.api.Board.entity.Board;
import com.aisolutionvoice.api.member.entity.Member;
import com.aisolutionvoice.api.post.dto.PostDetailDto;
import com.aisolutionvoice.api.post.dto.PostFlatRowDto;
import com.aisolutionvoice.api.post.dto.PostSummaryDto;
import com.aisolutionvoice.api.post.entity.Post;
import com.aisolutionvoice.api.post.repository.query.PostQueryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PostRepository  extends JpaRepository<Post, Long>, PostQueryRepository {
    @Query("""
    SELECT new com.aisolutionvoice.api.post.dto.PostSummaryDto(p.id, p.title, m.loginId, p.createdAt, p.isChecked)
    FROM Post p
    JOIN p.member m
    WHERE p.board.id = :boardId
""")
    Page<PostSummaryDto> findSummaryByBoardId(@Param("boardId") Long boardId, Pageable pageable);

    @Query("""
    SELECT new com.aisolutionvoice.api.post.dto.PostSummaryDto(p.id, p.title, m.loginId, p.createdAt, p.isChecked)
    FROM Post p
    JOIN p.member m
    WHERE p.board.id = :boardId
    AND p.title LIKE CONCAT('%', :title, '%')
    """)
    Page<PostSummaryDto> findSummaryByBoardIdAndTitleLike(@Param("boardId") Long boardId,  @Param("title") String title, Pageable pageable);

    @Query("""
        SELECT new com.aisolutionvoice.api.post.dto.PostFlatRowDto(
            p.id,
            p.title,
            h.scriptId,
            h.text,
            v.id,
            p.memo,
            p.isChecked
        )
        FROM Post p
        JOIN HotwordScript h ON h.board.id = p.board.id
        LEFT JOIN VoiceData v ON v.post.id = p.id AND v.hotwordScript.scriptId = h.scriptId
        WHERE p.id = :postId
        ORDER BY h.scriptId
    """)
    List<PostFlatRowDto> findPostFlatRows(@Param("postId") Long postId);

    Optional<Post> findByMemberAndBoard(Member member, Board board);

    Boolean existsByMemberAndBoard(Member member, Board board);

}
