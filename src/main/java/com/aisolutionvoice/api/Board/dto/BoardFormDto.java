package com.aisolutionvoice.api.Board.dto;

import com.aisolutionvoice.api.HotwordScript.dto.HotwordScriptDto;
import com.aisolutionvoice.api.HotwordScript.entity.HotwordScript;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Schema(name = "BoardFormDto", description = "게시판 응답 DTO")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BoardFormDto {

    @Schema(description = "게시판 ID", example = "42")
    private Long boardId;

    @Schema(description = "게시판 명", example = "차량 호출어 수집")
    private String boardName;

    @Schema(description = "게시판 설명", example = "운전 중 사용할 호출어를 녹음하세요")
    private String description;

    @Schema(description = "호출어 스크립트 리스트")
    private List<HotwordScriptDto> scripts;
}