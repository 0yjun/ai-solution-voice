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


    Optional<Board> findBoardById(@Param("boardId") Long boardId);
}
