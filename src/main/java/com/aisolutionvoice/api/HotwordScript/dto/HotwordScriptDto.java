package com.aisolutionvoice.api.HotwordScript.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Schema(name = "HotwordScriptDto", description = "호출어 응답 DTO")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HotwordScriptDto {

    @Schema(description = "스크립트 아이디", example = "42")
    private Long scriptId;

    @Schema(description = "호출어 명", example = "시리야")
    private String text;
}