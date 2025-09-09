package com.aisolutionvoice.api.post.controller;

import com.aisolutionvoice.api.post.dto.*;
import com.aisolutionvoice.api.post.service.PostService;
import com.aisolutionvoice.security.model.CustomMemberDetails;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/boards/{boardId}/posts")
@RequiredArgsConstructor
@Tag(name = "Post", description = "게시글 제공 API")
@Slf4j
public class PostController {
    private final PostService postService;

    @GetMapping
    public ResponseEntity<BoardPageDto> getPost(
            @PathVariable Long boardId,
            PostSearchRequestDto searchRequest,
            Pageable pageable,
            @AuthenticationPrincipal CustomMemberDetails customMemberDetails
    ) {
        Integer memberId = null;
        if(Boolean.TRUE.equals(searchRequest.getOnlyMyPosts())){
            memberId = customMemberDetails.getUserId();
        }
        searchRequest.setBoardId(boardId);
        BoardPageDto postSummaryDtoPage = postService.getSearchAndNotice(searchRequest, memberId, pageable);
        return ResponseEntity.ok(postSummaryDtoPage);
    }

    @GetMapping("/stat")
    public ResponseEntity<PostStatDto> getPostStat(
            @PathVariable Long boardId,
            PostSearchRequestDto searchRequest,
            @AuthenticationPrincipal CustomMemberDetails customMemberDetails
    ) {
        Integer memberId = null;
        if(Boolean.TRUE.equals(searchRequest.getOnlyMyPosts())){
            memberId = customMemberDetails.getUserId();
        }
        searchRequest.setBoardId(boardId);
        PostStatDto stats = postService.getPostStats(searchRequest, memberId);
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostDetailDto> getPostId(@PathVariable Long boardId, @PathVariable Long postId) {
        PostDetailDto postDetailDto = postService.getByPostId(postId);

        return ResponseEntity.ok(postDetailDto);
    }

    @GetMapping("/me/check")
    public ResponseEntity<?> checkMe(
            @PathVariable Long boardId,
            @AuthenticationPrincipal CustomMemberDetails customMemberDetails
    ) {
        Integer memberId = customMemberDetails.getUserId();
        return postService.findMyPostId(memberId, boardId)
                .map(ResponseEntity::ok)                      // 200 + postId
                .orElseGet(() -> ResponseEntity.noContent().build()); // 204
    }

    @PostMapping( consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadPostWithVoice(
            @PathVariable Long boardId,
            @RequestPart("postCreateDto") @Validated PostCreateDto dto,
            @RequestParam Map<String, MultipartFile> files,
            @AuthenticationPrincipal CustomMemberDetails customMemberDetails
    ) {
        Integer memberId = customMemberDetails.getUserId();
        postService.createPostWithVoiceFiles(dto, files, memberId, boardId);

        return ResponseEntity.ok(Map.of("data","ok"));
    }

    @PutMapping( consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> editPostWithVoice(
            @PathVariable Long boardId,
            @RequestPart("PostUpdateDto") @Validated PostUpdateDto dto,
            @RequestParam Map<String, MultipartFile> files,
            @AuthenticationPrincipal CustomMemberDetails customMemberDetails
    ) {
        Integer memberId = customMemberDetails.getUserId();
        postService.updatePostWithVoiceFiles(dto, files, memberId, boardId);

        return ResponseEntity.ok(Map.of("data","ok"));
    }

    @PatchMapping("/{postId:[0-9]+}/check")
    @PreAuthorize("hasRole(Role.ADMIN)")
    public ResponseEntity<Map<String, Boolean>> setChecked(
            @PathVariable Long boardId,
            @PathVariable Long postId,
            @RequestBody boolean isChecked
    ) {
        log.info(String.valueOf(isChecked));
        boolean isCheckedRes = postService.setChecked(postId, isChecked);
        return ResponseEntity.ok(Map.of("isChecked", isCheckedRes));
    }
}
