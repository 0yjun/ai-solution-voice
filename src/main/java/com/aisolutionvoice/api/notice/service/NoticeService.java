package com.aisolutionvoice.api.notice.service;

import com.aisolutionvoice.api.Board.entity.Board;
import com.aisolutionvoice.api.Board.repository.BoardRepository;
import com.aisolutionvoice.api.notice.dto.NoticeListResponseDto;
import com.aisolutionvoice.api.notice.dto.NoticeRequestDto;
import com.aisolutionvoice.api.notice.dto.NoticeResponseDto;
import com.aisolutionvoice.api.notice.entity.Notice;
import com.aisolutionvoice.api.notice.repository.NoticeRepository;
import com.aisolutionvoice.api.post.dto.PostItemDto;
import com.aisolutionvoice.api.post.dto.PostType;
import com.aisolutionvoice.exception.CustomException;
import com.aisolutionvoice.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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

    public List<PostItemDto> findNoticesForFirstPage(Long boardId, Pageable pageable){
        if(pageable.getPageNumber() == 0){
            return noticeRepository.findNoticesForBoard(boardId).stream()
                    .map(this::convertNoticeToBoardItemDto)
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    private PostItemDto convertNoticeToBoardItemDto(Notice notice) {
        return PostItemDto.builder()
                .id(notice.getId())
                .type(PostType.NOTICE)
                .title(notice.getTitle())
                .author("관리자") // 공지사항 작성자는 '관리자'로 고정
                .createdAt(notice.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .viewCount(null) // 공지사항은 조회수 필드가 없으므로 null
                .build();
    }
}
