package com.aisolutionvoice.api.HotwordScript.service;

import com.aisolutionvoice.api.Board.entity.Board;
import com.aisolutionvoice.api.HotwordScript.entity.HotwordScript;
import com.aisolutionvoice.api.HotwordScript.repository.HotwordScriptRepository;
import com.aisolutionvoice.api.Board.repository.BoardRepository;
import com.aisolutionvoice.exception.CustomException;
import com.aisolutionvoice.exception.ErrorCode;
import com.aisolutionvoice.api.HotwordScript.dto.HotwordScriptDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HotwordScriptService {
    private final HotwordScriptRepository hotwordScriptRepository;
    private final BoardRepository boardRepository;
    private final ModelMapper modelMapper;

    public HotwordScript getScriptByProxy(Long scriptId){
        return hotwordScriptRepository.getReferenceById(scriptId);
    }

    public List<HotwordScript> getList(String text){
        return hotwordScriptRepository.findHotwordScriptByTextContains(text);
    }

    public Page<HotwordScript> getPage(String text, Pageable pageable){
        return hotwordScriptRepository.findHotwordScriptByTextContains(text, pageable);
    }

    public Long save(String text){
        HotwordScript newHotwordScript = HotwordScript.builder()
                .text(text)
                .build();
        return hotwordScriptRepository.save(newHotwordScript).getScriptId();
    }

    @Transactional
    public HotwordScript assignHotwordScriptToBoard(Long boardId, Long scriptId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new CustomException(ErrorCode.BOARD_NOT_FOUND));

        HotwordScript script = hotwordScriptRepository.findById(scriptId)
                .orElseThrow(() -> new CustomException(ErrorCode.INTERNAL_COMMON_ERROR)); // 스크립트 없음 에러코드 필요

        // 이미 다른 게시판에 할당되어 있는지 확인 (선택적 로직)
        if (script.getBoard() != null && !script.getBoard().getId().equals(boardId)) {
            // 이미 다른 게시판에 할당된 스크립트입니다. 재할당하시겠습니까? 등의 로직 필요
            // 여기서는 단순히 에러를 발생시키거나, 기존 할당을 해제하고 재할당할 수 있습니다.
            throw new CustomException(ErrorCode.VALIDATION_ERROR); // 예시: 이미 할당된 스크립트
        }

        script.setBoard(board); // 스크립트에 게시판 연결
        board.addScript(script); // 게시판에도 스크립트 추가 (양방향 편의 메서드)

        return hotwordScriptRepository.save(script);
    }

    @Transactional(readOnly = true)
    public List<HotwordScriptDto> getHotwordScriptsByBoard(Long boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new CustomException(ErrorCode.BOARD_NOT_FOUND));

        return board.getScripts().stream()
                .map(script -> modelMapper.map(script, HotwordScriptDto.class))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<HotwordScriptDto> getUnassignedHotwordScripts() {
        // board 필드가 null인 스크립트들을 조회
        List<HotwordScript> unassignedScripts = hotwordScriptRepository.findByBoardIsNull();
        return unassignedScripts.stream()
                .map(script -> modelMapper.map(script, HotwordScriptDto.class))
                .collect(Collectors.toList());
    }
}