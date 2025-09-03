package com.aisolutionvoice.api.notice.repository;

import com.aisolutionvoice.api.notice.entity.Notice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NoticeRepository extends JpaRepository<Notice, Long> {

    @Query("SELECT n FROM Notice n WHERE n.board.id = :boardId OR n.board IS NULL ORDER BY n.createdAt DESC")
    List<Notice> findNoticesForBoard(@Param("boardId") Long boardId);
}
