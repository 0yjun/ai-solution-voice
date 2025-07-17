package com.aisolutionvoice.api.Board.repository;

import com.aisolutionvoice.api.Board.entity.Board;
import com.aisolutionvoice.api.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.Optional;

@Repository
public interface BoardRepository extends JpaRepository<Board, Integer> {
    @Query("""
        SELECT b FROM Board b
        LEFT JOIN FETCH b.scriptMappings m
        LEFT JOIN FETCH m.hotwordScript
        WHERE b.id = :boardId
    """)
    Optional<Board> findByIdWithScripts(@Param("boardId") Long boardId);
}
