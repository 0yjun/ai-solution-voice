package com.aisolutionvoice.api.post.controller;

import com.aisolutionvoice.api.post.dto.PostSummaryDto;
import com.aisolutionvoice.api.post.service.PostService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
@Tag(name = "Post", description = "게시글 제공 API")
public class PostController {
    private final PostService postService;

    @GetMapping
    public ResponseEntity<Page<PostSummaryDto>> getPostsByBoard(@RequestParam Long boardId, Pageable pageable) {
        Page<PostSummaryDto> postSummaryDtoPage = postService.getByBoardId(boardId, pageable);

        return ResponseEntity.ok(postSummaryDtoPage);
    }

    @PostMapping
    public ResponseEntity<?> getPostsByBoard(@RequestParam Long boardId) {

        return ResponseEntity.ok().build();
    }
}
