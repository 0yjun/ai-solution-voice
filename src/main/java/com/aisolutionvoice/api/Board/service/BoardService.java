package com.aisolutionvoice.api.Board.service;

import com.aisolutionvoice.api.Board.dto.BoardFormDto;
import com.aisolutionvoice.api.Board.entity.Board;
import com.aisolutionvoice.api.Board.repository.BoardRepository;
import com.aisolutionvoice.api.HotwordScript.dto.HotwordScriptDto;
import com.aisolutionvoice.api.HotwordScript.entity.HotwordScript;
import com.aisolutionvoice.api.HotwordScript.repository.HotwordScriptRepository;
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
    private final HotwordScriptRepository hotwordScriptRepository;
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

    @Transactional
    public List<Long> addScripts(Integer boardId, List<Long> ids) {
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
}
