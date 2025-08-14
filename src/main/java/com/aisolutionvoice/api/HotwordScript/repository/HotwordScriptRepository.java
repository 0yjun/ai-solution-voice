package com.aisolutionvoice.api.HotwordScript.repository;

import com.aisolutionvoice.api.Board.entity.Board;
import com.aisolutionvoice.api.HotwordScript.entity.HotwordScript;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HotwordScriptRepository extends JpaRepository<HotwordScript, Long> {
    List<HotwordScript> findHotwordScriptByTextContains(String text);
    Page<HotwordScript> findHotwordScriptByTextContains(String text,  Pageable pageable);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
        update HotwordScript s
        set s.board = :board
        where s.scriptId in :ids
    """)
    void attachScripts(@Param("board") Board board, @Param("ids") List<Long> ids);
}