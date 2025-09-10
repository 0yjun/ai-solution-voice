package com.aisolutionvoice.api.voiceData.controller;

import com.aisolutionvoice.api.voiceData.service.VoiceDataService;
import com.aisolutionvoice.common.file.FileStorageService;
import com.aisolutionvoice.exception.CustomException;
import com.aisolutionvoice.exception.ErrorCode;
import com.aisolutionvoice.security.model.CustomMemberDetails;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/api/voice-data")
@RequiredArgsConstructor
@Tag(name = "Post", description = "게시글 제공 API")
public class VoiceDataController {
    private final VoiceDataService voiceDataService;

    @GetMapping("/{voiceDataId}")
    public ResponseEntity<?> streamVoiceData(
            @PathVariable Long voiceDataId,
            @AuthenticationPrincipal CustomMemberDetails customMemberDetails
    ) {
        Resource audio = voiceDataService.getAudioResourceById(voiceDataId);
        String loginId = customMemberDetails.getLoginId();
        String filename = loginId + "_" + voiceDataId + ".wav";

        try {
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            ContentDisposition.inline()
                                    .filename(filename, StandardCharsets.UTF_8) // filename + filename* 처리
                                    .build().toString())
                    .header(HttpHeaders.ACCEPT_RANGES, "bytes") // 시킹 지원
                    .contentType(MediaType.parseMediaType("audio/wav"))
                    .contentLength(audio.contentLength())
                    .body(audio);
        } catch (IOException e) {
            throw new CustomException(ErrorCode.RESOURCE_NOT_FOUND);
        }
    }
}