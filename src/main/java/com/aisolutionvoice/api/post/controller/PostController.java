package com.aisolutionvoice.api.post.controller;

import com.aisolutionvoice.api.post.dto.PostCreateDto;
import com.aisolutionvoice.api.post.dto.PostSummaryDto;
import com.aisolutionvoice.api.post.service.PostService;
import com.aisolutionvoice.security.model.CustomMemberDetails;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
@Tag(name = "Post", description = "게시글 제공 API")
@Slf4j
public class PostController {
    private final PostService postService;

    @GetMapping
    public ResponseEntity<Page<PostSummaryDto>> getPostsByBoard(@RequestParam Long boardId, Pageable pageable) {
        Page<PostSummaryDto> postSummaryDtoPage = postService.getByBoardId(boardId, pageable);

        return ResponseEntity.ok(postSummaryDtoPage);
    }

    @PostMapping( consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadPostWithVoice(
            @RequestPart("postCreateDto") @Validated PostCreateDto dto,
            @RequestParam Map<String, MultipartFile> files,
            @AuthenticationPrincipal CustomMemberDetails customMemberDetails
    ) {
        Integer memberId = customMemberDetails.getUserId();
        try{
            postService.createPostWithVoiceFiles(dto, files, memberId);
        }catch(Exception e){
            e.printStackTrace();
        }

        return ResponseEntity.ok(Map.of("data","ok"));
    }
}
