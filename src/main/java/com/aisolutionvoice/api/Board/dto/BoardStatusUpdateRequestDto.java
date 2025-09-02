package com.aisolutionvoice.api.Board.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(name = "BoardStatusUpdateRequestDto", description = "게시판 활성화 상태 변경 요청 DTO")
@Getter
@NoArgsConstructor
public class BoardStatusUpdateRequestDto {

    @NotNull(message = "활성화 상태값은 필수입니다.")
    @Schema(description = "게시판 활성화 여부", example = "true")
    private Boolean activated;
}
