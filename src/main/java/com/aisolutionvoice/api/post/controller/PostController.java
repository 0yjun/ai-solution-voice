package com.aisolutionvoice.api.post.controller;

import com.aisolutionvoice.api.Role.domain.Role;
import com.aisolutionvoice.api.post.dto.*;
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
import java.util.Optional;
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
            PostSearchRequestDto searchRequest,
            Pageable pageable,
            @AuthenticationPrincipal CustomMemberDetails customMemberDetails
    ) {
        Integer memberId = null;
        if(Boolean.TRUE.equals(searchRequest.getOnlyMyPosts())){
            memberId = customMemberDetails.getUserId();
        }
        Page<PostSummaryDto> postSummaryDtoPage = postService.getSearch(searchRequest, memberId ,pageable);
        return ResponseEntity.ok(postSummaryDtoPage);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostDetailDto> getPostId(@PathVariable Long postId) {
        PostDetailDto postDetailDto = postService.getByPostId(postId);

        return ResponseEntity.ok(postDetailDto);
    }

    @GetMapping("/me/check")
    public ResponseEntity<?> checkMe(
            @AuthenticationPrincipal CustomMemberDetails customMemberDetails,
            @RequestParam Long boardId
    ) {
        Integer memberId = customMemberDetails.getUserId();
        return postService.findMyPostId(memberId, boardId)
                .map(ResponseEntity::ok)                      // 200 + postId
                .orElseGet(() -> ResponseEntity.noContent().build()); // 204
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

    @PatchMapping("/{postId:\\\\d+}/check")
    @PreAuthorize("hasRole(Role.ADMIN)")
    public ResponseEntity<?> setChecked(
            @PathVariable Long postId,
            @RequestBody Boolean isChecked
    ) {
        postService.setChecked(postId, isChecked);
        return ResponseEntity.noContent().build();
    }
}
