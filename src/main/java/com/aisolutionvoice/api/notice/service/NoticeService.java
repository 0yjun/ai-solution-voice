package com.aisolutionvoice.api.notice.service;

import com.aisolutionvoice.api.notice.dto.NoticeListResponseDto;
import com.aisolutionvoice.api.notice.dto.NoticeRequestDto;
import com.aisolutionvoice.api.notice.dto.NoticeResponseDto;
import com.aisolutionvoice.api.notice.entity.Notice;
import com.aisolutionvoice.api.notice.repository.NoticeRepository;
import com.aisolutionvoice.exception.CustomException;
import com.aisolutionvoice.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NoticeService {

    private final NoticeRepository noticeRepository;

    @Transactional
    public NoticeResponseDto createNotice(NoticeRequestDto requestDto) {
        Notice notice = Notice.builder()
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .build();
        Notice savedNotice = noticeRepository.save(notice);
        return NoticeResponseDto.from(savedNotice);
    }

    public Page<NoticeListResponseDto> findAllNotices(Pageable pageable) {
        return noticeRepository.findAll(pageable)
                .map(NoticeListResponseDto::from);
    }

    public NoticeResponseDto findNoticeById(Long noticeId) {
        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND));
        return NoticeResponseDto.from(notice);
    }

    @Transactional
    public NoticeResponseDto updateNotice(Long noticeId, NoticeRequestDto requestDto) {
        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND));
        notice.update(requestDto.getTitle(), requestDto.getContent());
        return NoticeResponseDto.from(notice);
    }

    @Transactional
    public void deleteNotice(Long noticeId) {
        noticeRepository.deleteById(noticeId);
    }
}
