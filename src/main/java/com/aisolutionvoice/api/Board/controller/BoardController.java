package com.aisolutionvoice.api.Board.controller;

import com.aisolutionvoice.api.Board.dto.BoardFormDto;
import com.aisolutionvoice.api.Board.service.BoardService;
import com.aisolutionvoice.api.Role.domain.Role;
import com.aisolutionvoice.api.menu.dto.MenuClientDto;
import com.aisolutionvoice.api.menu.service.MenuService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
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
}