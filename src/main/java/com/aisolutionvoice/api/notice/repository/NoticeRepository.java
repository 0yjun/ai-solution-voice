package com.aisolutionvoice.api.notice.repository;

import com.aisolutionvoice.api.notice.entity.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeRepository extends JpaRepository<Notice, Long> {
}
