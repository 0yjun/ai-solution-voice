package com.aisolutionvoice.api.post.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class PostItemDto {
    private final Long id;
    private final PostType type; // "NOTICE" or "POST"
    private final String title;
    private final String author; // 작성자
    private final String createdAt;
    private final Integer viewCount; // 조회수 (공지에 없으면 null)
    private final boolean isChecked; // 체크 여부

    @Builder
    public PostItemDto(Long id, PostType type, String title, String author, String createdAt, Integer viewCount, boolean isChecked) {
        this.id = id;
        this.type = type;
        this.title = title;
        this.author = author;
        this.createdAt = createdAt;
        this.viewCount = viewCount;
        this.isChecked = isChecked;
    }
}
