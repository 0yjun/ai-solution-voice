package com.aisolutionvoice.api.Board.controller;

import com.aisolutionvoice.api.Board.dto.AdminBoardDto;
import com.aisolutionvoice.api.Board.dto.BoardCreateRequestDto;
import com.aisolutionvoice.api.Board.dto.BoardFormDto;
import com.aisolutionvoice.api.Board.dto.BoardStatusUpdateRequestDto;
import com.aisolutionvoice.api.Board.service.BoardService;
import com.aisolutionvoice.common.dto.SelectOptionDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/boards")
@RequiredArgsConstructor
@Slf4j
public class BoardController {
    private  final BoardService boardService;

    @GetMapping
    public List<BoardFormDto> getBoardList(){
        return boardService.getList();
    }

    @GetMapping("/options")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<SelectOptionDto>> getBoardOptions() {
        List<SelectOptionDto> boardOptions = boardService.getBoardsForSelection();
        return ResponseEntity.ok(boardOptions);
    }

    // TODO: "/admin" 경로는 Spring Security를 통해 ADMIN 권한을 가진 사용자만 접근하도록 제한해야 합니다.
    @GetMapping("/admin")
    public ResponseEntity<List<AdminBoardDto>> getAdminBoardList() {
        return ResponseEntity.ok(boardService.getAdminBoardList());
    }
    @GetMapping("/{boardId}/form")
    public BoardFormDto getBoardForm(@PathVariable Long boardId) {
        return boardService.getBoardForm(boardId);
    }

    @PostMapping("/{boardId}/scripts")
    public ResponseEntity<?> createOne(
            @PathVariable Long boardId,
            @Valid @RequestBody List<Long> reqs
    ) {
        List<Long> processedIds = boardService.addScripts(boardId, reqs);

        URI location = URI.create("/api/boards/" + boardId + "/scripts");
        return ResponseEntity.created(location).body(processedIds);
    }

    @PutMapping("/{boardId}")
    public ResponseEntity<Void> updateBoard(
            @PathVariable Long boardId,
            @Valid @RequestBody BoardFormDto boardFormDto) {
        boardService.updateBoard(boardId, boardFormDto);
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    public ResponseEntity<BoardFormDto> createBoard(@Valid @RequestBody BoardCreateRequestDto requestDto) {
        BoardFormDto createdBoard = boardService.createBoard(requestDto);
        URI location = URI.create("/api/boards/" + createdBoard.getBoardId());
        return ResponseEntity.created(location).body(createdBoard);
    }

    @PutMapping("/{boardId}/status")
    public ResponseEntity<Void> updateBoardStatus(
            @PathVariable Long boardId,
            @Valid @RequestBody BoardStatusUpdateRequestDto requestDto) {
        boardService.updateBoardStatus(boardId, requestDto.getActivated());
        return ResponseEntity.noContent().build();
    }
}