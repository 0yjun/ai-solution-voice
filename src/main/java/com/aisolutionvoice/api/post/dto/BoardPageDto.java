package com.aisolutionvoice.api.post.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@AllArgsConstructor
public class BoardPageDto {
    private final List<PostItemDto> notices;
    private final Page<PostItemDto> posts;
}