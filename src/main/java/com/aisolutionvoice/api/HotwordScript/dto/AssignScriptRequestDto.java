package com.aisolutionvoice.api.HotwordScript.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(name = "AssignScriptRequestDto", description = "스크립트 할당 요청 DTO")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AssignScriptRequestDto {

    @NotNull(message = "스크립트 ID는 필수입니다.")
    @Schema(description = "할당할 스크립트 ID", example = "1")
    private Long scriptId;
}
