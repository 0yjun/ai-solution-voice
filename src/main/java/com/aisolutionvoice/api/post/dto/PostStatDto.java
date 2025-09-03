package com.aisolutionvoice.api.post.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PostStatDto {
    private final long totalCount;
    private final long checkedCount;
    private final double progress;
}
