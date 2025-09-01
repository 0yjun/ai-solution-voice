package com.aisolutionvoice.api.Board.repository;

import com.aisolutionvoice.api.Board.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {


    Optional<Board> findBoardById(@Param("boardId") Long boardId);

    @Query("""
        select distinct b
        from Board b
        left join fetch b.scripts
    """)
    List<Board> findAllWithScripts();

    @Query("""
        select b
        from Board b
        left join fetch b.scripts
        where b.id = :id
    """)
    Optional<Board> findByIdWithScripts(@Param("id") Long id);
}
