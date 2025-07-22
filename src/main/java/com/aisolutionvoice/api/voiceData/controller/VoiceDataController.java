package com.aisolutionvoice.api.voiceData.controller;

import com.aisolutionvoice.api.voiceData.service.VoiceDataService;
import com.aisolutionvoice.common.file.FileStorageService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/voice-data")
@RequiredArgsConstructor
@Tag(name = "Post", description = "게시글 제공 API")
public class VoiceDataController {
    private final VoiceDataService voiceDataService;
    private final FileStorageService fileStorageService;

    @GetMapping("/{voiceDataId}")
    public ResponseEntity<?> streamVoiceData(@PathVariable Long voiceDataId) {
        Resource audio = voiceDataService.getAudioResourceById(voiceDataId);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(audio);
    }
}