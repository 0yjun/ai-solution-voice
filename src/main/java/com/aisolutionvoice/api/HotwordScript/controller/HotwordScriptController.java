package com.aisolutionvoice.api.HotwordScript.controller;

import com.aisolutionvoice.api.HotwordScript.dto.AssignScriptRequestDto;
import com.aisolutionvoice.api.HotwordScript.dto.HotwordScriptDto;
import com.aisolutionvoice.api.HotwordScript.entity.HotwordScript;
import com.aisolutionvoice.api.HotwordScript.service.HotwordScriptService;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class HotwordScriptController {
    private final HotwordScriptService hotwordScriptService;

    @GetMapping("/hotword-script")
    public ResponseEntity<?> getList(@RequestParam String text, @Nullable Pageable pageable){
        var result = (pageable == null)
                ? hotwordScriptService.getList(text)
                : hotwordScriptService.getPage(text, pageable);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/hotword-script")
    public ResponseEntity<?> save(@RequestBody String text) {
        Long scriptId = hotwordScriptService.save(text);
        URI location = URI.create("/api/hotword-script/" + scriptId);
        return ResponseEntity.created(location)
                .build();
    }

    // 게시판에 스크립트 할당 (기존 스크립트 연결)
    @PostMapping("/boards/{boardId}/hotword-scripts")
    public ResponseEntity<HotwordScriptDto> assignHotwordScriptToBoard(
            @PathVariable Long boardId,
            @Valid @RequestBody AssignScriptRequestDto requestDto) {
        HotwordScript assignedScript = hotwordScriptService.assignHotwordScriptToBoard(boardId, requestDto.getScriptId());
        HotwordScriptDto responseDto = new HotwordScriptDto(assignedScript.getScriptId(), assignedScript.getText(), null); // voiceDataUrl은 나중에 추가
        URI location = URI.create("/api/hotword-script/" + assignedScript.getScriptId());
        return ResponseEntity.created(location).body(responseDto);
    }

    // 게시판에 속한 스크립트 목록 조회
    @GetMapping("/boards/{boardId}/hotword-scripts")
    public ResponseEntity<List<HotwordScriptDto>> getHotwordScriptsByBoard(@PathVariable Long boardId) {
        List<HotwordScriptDto> scripts = hotwordScriptService.getHotwordScriptsByBoard(boardId);
        return ResponseEntity.ok(scripts);
    }

    // 게시판에 할당되지 않은 스크립트 목록 조회
    @GetMapping("/hotword-script/unassigned")
    public ResponseEntity<List<HotwordScriptDto>> getUnassignedHotwordScripts() {
        List<HotwordScriptDto> scripts = hotwordScriptService.getUnassignedHotwordScripts();
        return ResponseEntity.ok(scripts);
    }
}
