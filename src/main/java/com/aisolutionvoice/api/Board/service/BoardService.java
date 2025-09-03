package com.aisolutionvoice.api.Board.service;

import com.aisolutionvoice.api.Board.dto.AdminBoardDto;
import com.aisolutionvoice.api.Board.dto.BoardCreateRequestDto;
import com.aisolutionvoice.api.Board.dto.BoardFormDto;
import com.aisolutionvoice.api.Board.entity.Board;
import com.aisolutionvoice.api.Board.repository.BoardRepository;
import com.aisolutionvoice.api.HotwordScript.dto.HotwordScriptDto;
import com.aisolutionvoice.api.HotwordScript.entity.HotwordScript;
import com.aisolutionvoice.api.HotwordScript.repository.HotwordScriptRepository;
import com.aisolutionvoice.common.dto.SelectOptionDto;
import com.aisolutionvoice.exception.CustomException;
import com.aisolutionvoice.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final HotwordScriptRepository hotwordScriptRepository;
    private final ModelMapper modelMapper;


    public Board getBoardByProxy(Long boardId){
        return boardRepository.getReferenceById(boardId);
    }

    // 공통 변환 메서드
    private BoardFormDto toBoardFormDto(Board board) {
        List<HotwordScriptDto> scripts = board.getScripts().stream()
                .sorted(Comparator.comparing(HotwordScript::getScriptId)) // 필요 시 정렬
                .map(s -> modelMapper.map(s, HotwordScriptDto.class))
                .toList();

        return new BoardFormDto(
                board.getId(),
                board.getName(),
                board.getDescription(),
                scripts,
                false
        );
    }

    @Transactional(readOnly = true)
    public List<BoardFormDto> getList() {
        // N+1 방지: fetch join 또는 EntityGraph 권장 (아래 패턴 참고)
        List<Board> boards = boardRepository.findAllWithScripts();
        return boards.stream().map(this::toBoardFormDto).toList();
    }

    @Transactional(readOnly = true)
    public BoardFormDto getBoardForm(Long boardId) {
        Board board = boardRepository.findBoardById(boardId)
                .orElseThrow(() -> new CustomException(ErrorCode.INTERNAL_COMMON_ERROR));

        List<HotwordScriptDto> scripts = board.getScripts().stream()
                //.filter(script -> script.getScriptId()==1)
                .map(script ->
                    modelMapper.map(script, HotwordScriptDto.class)
                )
                .toList();

        return new BoardFormDto(
                board.getId(),
                board.getName(),
                board.getDescription(),
                scripts,
                false
        );
    }

    @Transactional
    public List<Long> addScripts(Long boardId, List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            throw new CustomException(ErrorCode.VALIDATION_ERROR);
        }

        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new CustomException(ErrorCode.INTERNAL_COMMON_ERROR));

        List<Long> distinctIds = ids.stream().distinct().toList();
        List<HotwordScript> scripts = hotwordScriptRepository.findAllById(distinctIds);

        if (scripts.size() != distinctIds.size()) {
            throw new CustomException(ErrorCode.VALIDATION_ERROR); // 없는 ID 있음
        }

        for (HotwordScript s : scripts) {
            board.addScript(s); // 양방향 편의 메서드
        }

        // 처리된 스크립트 ID 반환
        return scripts.stream()
                .map(HotwordScript::getScriptId)
                .toList();
    }

    @Transactional
    public void updateBoard(Long boardId, BoardFormDto boardFormDto) {
        Board board = boardRepository.findBoardById(boardId)
                .orElseThrow(() -> new CustomException(ErrorCode.BOARD_NOT_FOUND));
        board.updateFromDto(boardFormDto);
        boardRepository.save(board);
    }

    @Transactional
    public BoardFormDto createBoard(BoardCreateRequestDto requestDto) {
        Board board = Board.builder()
                .name(requestDto.getBoardName())
                .description(requestDto.getDescription())
                .build();

        Board savedBoard = boardRepository.save(board);
        return toBoardFormDto(savedBoard);
    }

    @Transactional
    public void updateBoardStatus(Long boardId, Boolean activated) {
        Board board = boardRepository.findBoardById(boardId)
                .orElseThrow(() -> new CustomException(ErrorCode.BOARD_NOT_FOUND));

        board.setDeleted(!activated);
        boardRepository.save(board);
    }

    @Transactional(readOnly = true)
    public List<AdminBoardDto> getAdminBoardList() {
        // deleted 상태와 상관없이 모든 Board를 조회합니다.
        List<Board> allBoards = boardRepository.findAll();
        return allBoards.stream()
                .map(AdminBoardDto::fromEntity)
                .collect(Collectors.toList());
    }

    public List<SelectOptionDto> getBoardsForSelection() {
        return boardRepository.findAll().stream()
                .map(board -> new SelectOptionDto(board.getId(), board.getName()))
                .collect(Collectors.toList());
    }
}