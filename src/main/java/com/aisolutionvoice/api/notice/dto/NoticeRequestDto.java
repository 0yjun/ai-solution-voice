package com.aisolutionvoice.api.notice.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NoticeRequestDto {

    @NotBlank
    private String title;

    @NotBlank
    private String content;

    private Long boardId;
}
