package com.aisolutionvoice.api.Board.service;

import com.aisolutionvoice.api.Board.dto.BoardFormDto;
import com.aisolutionvoice.api.Board.entity.Board;
import com.aisolutionvoice.api.Board.repository.BoardRepository;
import com.aisolutionvoice.api.HotwordScript.dto.HotwordScriptDto;
import com.aisolutionvoice.api.HotwordScript.entity.HotwordScript;
import com.aisolutionvoice.api.member.entity.Member;
import com.aisolutionvoice.exception.CustomException;
import com.aisolutionvoice.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final ModelMapper modelMapper;


    public Board getBoardByProxy(Integer boardId){
        return boardRepository.getReferenceById(boardId);
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
                scripts
        );
    }
}
