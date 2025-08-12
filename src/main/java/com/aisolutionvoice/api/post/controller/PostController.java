package com.aisolutionvoice.api.post.controller;

import com.aisolutionvoice.api.Role.domain.Role;
import com.aisolutionvoice.api.post.dto.PostCreateDto;
import com.aisolutionvoice.api.post.dto.PostDetailDto;
import com.aisolutionvoice.api.post.dto.PostSummaryDto;
import com.aisolutionvoice.api.post.dto.PostUpdateDto;
import com.aisolutionvoice.api.post.service.PostService;
import com.aisolutionvoice.exception.CustomException;
import com.aisolutionvoice.exception.ErrorCode;
import com.aisolutionvoice.security.model.CustomMemberDetails;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    public ResponseEntity<Page<PostSummaryDto>> getPostsByBoard(
            @RequestParam Long boardId,
            Pageable pageable,
            @Nullable  @RequestParam String title
    ) {
        Page<PostSummaryDto> postSummaryDtoPage = postService.getByBoardId(boardId, title, pageable);

        return ResponseEntity.ok(postSummaryDtoPage);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostDetailDto> getPostId(@PathVariable Long postId) {
        PostDetailDto postDetailDto = postService.getByPostId(postId);

        return ResponseEntity.ok(postDetailDto);
    }

    @GetMapping("/check-created")
    public ResponseEntity<Boolean> isPostExist(
            @AuthenticationPrincipal CustomMemberDetails customMemberDetails,
            @RequestParam Long boardId
    ) {
        Integer memberId = customMemberDetails.getUserId();
        Boolean exist = postService.existPostByMemberIdAndBoardId(memberId, boardId);

        return ResponseEntity.ok(exist);
    }

    @PostMapping( consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadPostWithVoice(
            @RequestPart("postCreateDto") @Validated PostCreateDto dto,
            @RequestParam Map<String, MultipartFile> files,
            @AuthenticationPrincipal CustomMemberDetails customMemberDetails
    ) {
        Integer memberId = customMemberDetails.getUserId();
        postService.createPostWithVoiceFiles(dto, files, memberId);

        return ResponseEntity.ok(Map.of("data","ok"));
    }

    @PutMapping( consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> editPostWithVoice(
            @RequestPart("PostUpdateDto") @Validated PostUpdateDto dto,
            @RequestParam Map<String, MultipartFile> files,
            @AuthenticationPrincipal CustomMemberDetails customMemberDetails
    ) {
        Integer memberId = customMemberDetails.getUserId();
        postService.updatePostWithVoiceFiles(dto, files, memberId);

        return ResponseEntity.ok(Map.of("data","ok"));
    }

    @PatchMapping("/{postId}/check")
    @PreAuthorize("hasRole(Role.ADMIN)")
    public ResponseEntity<?> setChecked(
            @PathVariable Long postId,
            @RequestBody Boolean isChecked
    ) {
        postService.setChecked(postId, isChecked);
        return ResponseEntity.noContent().build();
    }
}
