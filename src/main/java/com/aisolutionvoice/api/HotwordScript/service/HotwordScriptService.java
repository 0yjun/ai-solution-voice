package com.aisolutionvoice.api.HotwordScript.service;

import com.aisolutionvoice.api.Board.entity.Board;
import com.aisolutionvoice.api.HotwordScript.entity.HotwordScript;
import com.aisolutionvoice.api.HotwordScript.repository.HotwordScriptRepository;
import com.aisolutionvoice.api.Board.repository.BoardRepository;
import com.aisolutionvoice.exception.CustomException;
import com.aisolutionvoice.exception.ErrorCode;
import com.aisolutionvoice.api.HotwordScript.dto.HotwordScriptDto;
import com.aisolutionvoice.api.HotwordScript.dto.UpdateBoardScriptsRequestDto;
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

    @Transactional
    public void updateBoardScripts(Long boardId, List<Long> newScriptIds) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new CustomException(ErrorCode.BOARD_NOT_FOUND));

        // 게시글이 존재하는 게시판의 스크립트는 수정할 수 없도록 검증
        if (!board.getPosts().isEmpty()) {
            throw new CustomException(ErrorCode.BOARD_HAS_POSTS_CANNOT_MODIFY_SCRIPTS);
        }

        // 기존 스크립트 연결 해제
        // HotwordScript 엔티티의 board 필드에 cascade=ALL이 없으므로, HotwordScript 엔티티 자체가 삭제되지는 않습니다.
        // 대신, HotwordScript의 board_id가 null로 설정됩니다.

        for (HotwordScript script : board.getScripts()) {
            script.setBoard(null); // 기존 연결 해제
        }
        board.getScripts().clear(); // Board 엔티티의 리스트 비우기

        // 새로운 스크립트 연결
        if (newScriptIds != null && !newScriptIds.isEmpty()) {
            List<HotwordScript> scriptsToAssign = hotwordScriptRepository.findAllById(newScriptIds);

            if (scriptsToAssign.size() != newScriptIds.size()) {
                // 어떤 스크립트 ID가 유효하지 않은지 찾아서 메시지에 포함
                List<Long> foundScriptIds = scriptsToAssign.stream()
                        .map(HotwordScript::getScriptId)
                        .collect(Collectors.toList());
                List<Long> notFoundIds = newScriptIds.stream()
                        .filter(id -> !foundScriptIds.contains(id))
                        .collect(Collectors.toList());
                throw new CustomException(ErrorCode.HOTWORD_SCRIPT_NOT_FOUND, "다음 스크립트 ID를 찾을 수 없습니다: " + notFoundIds);
            }

            for (HotwordScript script : scriptsToAssign) {
                // 이미 다른 보드에 할당된 스크립트인지 확인 (선택적 로직)
                // 이 로직은 assignHotwordScriptToBoard와 중복될 수 있으나, 전체 교체 시나리오에서는 필요할 수 있습니다.
                if (script.getBoard() != null && !script.getBoard().getId().equals(boardId)) {
                    throw new CustomException(ErrorCode.VALIDATION_ERROR); // 예시: 이미 다른 게시판에 할당된 스크립트
                }
                board.addScript(script); // Board 엔티티의 addScript 메서드를 통해 양방향 연결
            }
        }
        boardRepository.save(board); // 변경사항 저장
    }
}
