package com.aisolutionvoice.api.HotwordScript.repository;

import com.aisolutionvoice.api.HotwordScript.entity.HotwordScript;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HotwordScriptRepository extends JpaRepository<HotwordScript, Long> {
    List<HotwordScript> findHotwordScriptByTextContains(String text);
    Page<HotwordScript> findHotwordScriptByTextContains(String text,  Pageable pageable);
}