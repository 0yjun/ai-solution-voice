package com.aisolutionvoice.api.Board.controller;

import com.aisolutionvoice.api.Board.dto.BoardFormDto;
import com.aisolutionvoice.api.Board.service.BoardService;
import com.aisolutionvoice.api.Role.domain.Role;
import com.aisolutionvoice.api.menu.dto.MenuClientDto;
import com.aisolutionvoice.api.menu.service.MenuService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/boards")
@RequiredArgsConstructor
@Slf4j
public class BoardController {
    private  final BoardService boardService;
    @GetMapping("/{boardId}/form")
    public BoardFormDto getBoardForm(@PathVariable Long boardId) {
        return boardService.getBoardForm(boardId);
    }
}