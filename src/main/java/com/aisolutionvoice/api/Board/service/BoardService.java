package com.aisolutionvoice.api.Board.service;

import com.aisolutionvoice.api.Board.dto.BoardFormDto;
import com.aisolutionvoice.api.Board.entity.Board;
import com.aisolutionvoice.api.Board.repository.BoardRepository;
import com.aisolutionvoice.api.HotwordScript.dto.HotwordScriptDto;
import com.aisolutionvoice.api.HotwordScript.entity.HotwordScript;
import com.aisolutionvoice.exception.CustomException;
import com.aisolutionvoice.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;

    @Transactional(readOnly = true)
    public BoardFormDto getBoardForm(Long boardId) {
        Board board = boardRepository.findByIdWithScripts(boardId)
                .orElseThrow(() -> new CustomException(ErrorCode.INTERNAL_COMMON_ERROR));

        List<HotwordScriptDto> scripts = board.getScriptMappings().stream()
                .map(mapping -> {
                    HotwordScript script = mapping.getHotwordScript();
                    return new HotwordScriptDto(script.getScript_id(), script.getText());
                })
                .toList();

        return new BoardFormDto(
                board.getId(),
                board.getName(),
                board.getDescription(),
                scripts
        );
    }
}
