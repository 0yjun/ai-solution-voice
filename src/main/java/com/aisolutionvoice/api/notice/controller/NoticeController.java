package com.aisolutionvoice.api.notice.controller;

import com.aisolutionvoice.api.notice.dto.NoticeListResponseDto;
import com.aisolutionvoice.api.notice.dto.NoticeRequestDto;
import com.aisolutionvoice.api.notice.dto.NoticeResponseDto;
import com.aisolutionvoice.api.notice.service.NoticeService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notices")
@RequiredArgsConstructor
@Tag(name = "Notice", description = "공지사항 API")
public class NoticeController {

    private final NoticeService noticeService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<NoticeResponseDto> createNotice(@RequestBody @Valid NoticeRequestDto requestDto) {
        NoticeResponseDto responseDto = noticeService.createNotice(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @GetMapping
    public ResponseEntity<Page<NoticeListResponseDto>> getAllNotices(Pageable pageable) {
        Page<NoticeListResponseDto> responseDtos = noticeService.findAllNotices(pageable);
        return ResponseEntity.ok(responseDtos);
    }

    @GetMapping("/{noticeId}")
    public ResponseEntity<NoticeResponseDto> getNoticeById(@PathVariable Long noticeId) {
        NoticeResponseDto responseDto = noticeService.findNoticeById(noticeId);
        return ResponseEntity.ok(responseDto);
    }

    @PutMapping("/{noticeId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<NoticeResponseDto> updateNotice(@PathVariable Long noticeId, @RequestBody @Valid NoticeRequestDto requestDto) {
        NoticeResponseDto responseDto = noticeService.updateNotice(noticeId, requestDto);
        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/{noticeId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteNotice(@PathVariable Long noticeId) {
        noticeService.deleteNotice(noticeId);
        return ResponseEntity.noContent().build();
    }
}
