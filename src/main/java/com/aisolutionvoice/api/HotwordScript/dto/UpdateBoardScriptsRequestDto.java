package com.aisolutionvoice.api.HotwordScript.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Schema(name = "UpdateBoardScriptsRequestDto", description = "게시판 스크립트 목록 업데이트 요청 DTO")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateBoardScriptsRequestDto {

    @Schema(description = "게시판에 할당할 스크립트 ID 목록")
    private List<Long> scriptIds;
}
