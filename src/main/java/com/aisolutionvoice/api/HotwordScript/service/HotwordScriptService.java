package com.aisolutionvoice.api.HotwordScript.service;

import com.aisolutionvoice.api.Board.entity.Board;
import com.aisolutionvoice.api.HotwordScript.entity.HotwordScript;
import com.aisolutionvoice.api.HotwordScript.repository.HotwordScriptRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HotwordScriptService {
    private final HotwordScriptRepository hotwordScriptRepository;
    public HotwordScript getScriptByProxy(Long scriptId){
        return hotwordScriptRepository.getReferenceById(scriptId);
    }
}
