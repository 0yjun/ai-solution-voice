package com.aisolutionvoice.api.Board.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(name = "BoardCreateRequestDto", description = "게시판 생성 요청 DTO")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BoardCreateRequestDto {

    @NotBlank(message = "게시판 이름은 필수입니다.")
    @Schema(description = "게시판 명", example = "새로운 호출어 수집")
    private String boardName;

    @Schema(description = "게시판 설명", example = "자유롭게 호출어를 녹음해주세요.")
    private String description;
}
