package com.aisolutionvoice.api.notice.service;

import com.aisolutionvoice.api.Board.entity.Board;
import com.aisolutionvoice.api.Board.repository.BoardRepository;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NoticeService {

    private final NoticeRepository noticeRepository;
    private final BoardRepository boardRepository;

    @Transactional
    public NoticeResponseDto createNotice(NoticeRequestDto requestDto) {
        Notice notice = Notice.builder()
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .build();

        if (requestDto.getBoardId() != null) {
            Board board = boardRepository.findById(requestDto.getBoardId())
                    .orElseThrow(() -> new CustomException(ErrorCode.BOARD_NOT_FOUND));
            notice.setBoard(board);
        }

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

        Board board = null;
        if (requestDto.getBoardId() != null) {
            board = boardRepository.findById(requestDto.getBoardId())
                    .orElseThrow(() -> new CustomException(ErrorCode.BOARD_NOT_FOUND));
        }

        notice.update(requestDto.getTitle(), requestDto.getContent(), board);
        return NoticeResponseDto.from(notice);
    }

    @Transactional
    public void deleteNotice(Long noticeId) {
        noticeRepository.deleteById(noticeId);
    }
}
