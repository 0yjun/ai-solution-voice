package com.aisolutionvoice.api.HotwordScript.service;

import com.aisolutionvoice.api.Board.entity.Board;
import com.aisolutionvoice.api.HotwordScript.entity.HotwordScript;
import com.aisolutionvoice.api.HotwordScript.repository.HotwordScriptRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HotwordScriptService {
    private final HotwordScriptRepository hotwordScriptRepository;
    public HotwordScript getScriptByProxy(Long scriptId){
        return hotwordScriptRepository.getReferenceById(scriptId);
    }

    public List<HotwordScript> getList(String text){
        return hotwordScriptRepository.findHotwordScriptByTextContains(text);
    }

    public Page<HotwordScript> getPage(String text, Pageable pageable){
        return hotwordScriptRepository.findHotwordScriptByTextContains(text, pageable);
    }
    public Long save(String text){
        HotwordScript newHotwordScript = HotwordScript.builder()
                .text(text)
                .build();
        return hotwordScriptRepository.save(newHotwordScript).getScriptId();
    }
}
