package com.aisolutionvoice.api.post.service;

import com.aisolutionvoice.api.Board.entity.Board;
import com.aisolutionvoice.api.Board.service.BoardService;
import com.aisolutionvoice.api.member.entity.Member;
import com.aisolutionvoice.api.member.service.MemberService;
import com.aisolutionvoice.api.notice.service.NoticeService;
import com.aisolutionvoice.api.post.dto.*;
import com.aisolutionvoice.api.post.entity.Post;
import com.aisolutionvoice.api.post.repository.PostRepository;
import com.aisolutionvoice.api.voiceData.service.VoiceDataService;
import com.aisolutionvoice.exception.CustomException;
import com.aisolutionvoice.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostService {
    private  final PostRepository postRepository;
    private final MemberService memberService;
    private  final BoardService boardService;
    private final VoiceDataService voiceDataService;
    private final NoticeService noticeService;


    public BoardPageDto getSearchAndNotice(PostSearchRequestDto requestDto, Integer memberId, Pageable pageable){
        // 1. 일반 게시글 검색 및 PostItemDto로 변환
        Page<PostItemDto> postsPage = getSearch(requestDto, memberId, pageable)
                .map(this::convertPostSummaryToPostItem);

        // 2. 공지사항 목록 조회 (첫 페이지일 때만)
        List<PostItemDto> noticeDtos = noticeService.findNoticesForFirstPage(requestDto.getBoardId(), pageable);

        // 3. BoardPageDto 객체로 감싸서 반환
        return new BoardPageDto(noticeDtos, postsPage);
    }

    private PostItemDto convertPostSummaryToPostItem(PostSummaryDto postSummary) {
        return PostItemDto.builder()
                .id(postSummary.getPostId())
                .type(PostType.POST)
                .title(postSummary.getTitle())
                .author(postSummary.getWriterLoginId())
                .createdAt(postSummary.getCreateAt()) // 바로 String을 사용
                .viewCount(null) // PostSummaryDto에 조회수 정보가 없으므로 null
                .build();
    }


    public Page<PostSummaryDto> getSearch(PostSearchRequestDto requestDto, Integer memberId, Pageable pageable) {
        Page<PostSummaryDto> result = postRepository.search(requestDto, memberId, pageable);
        return result.map(PostSummaryDto::applyDefaultTitle);
    }

    public Long countCheckedPosts(PostSearchRequestDto requestDto, Integer memberId) {
        return postRepository.countByCheckedTrueAndCond(requestDto, memberId);
    }

    public Long countAll() {
        return postRepository.count();
    }

    @Transactional(readOnly = true)
    public PostStatDto getPostStats(PostSearchRequestDto requestDto, Integer memberId) {
        long checkedCount = countCheckedPosts(requestDto, memberId);
        long totalCount = postRepository.countBySearch(requestDto, memberId);

        if (totalCount == 0) {
            return new PostStatDto(0L, 0L, 0.0);
        }

        double progress = ((double) checkedCount / totalCount) * 100.0;
        return new PostStatDto(totalCount, checkedCount, progress);
    }

    public PostDetailDto getByPostId(Long postId){
        List<PostFlatRowDto>  flatRowDtoList =  postRepository.findPostFlatRows(postId);
        return PostDetailDto.fromFlatRows(flatRowDtoList);
    }

    public Optional<Long> findMyPostId(Integer memberId, Long boardId){
        return postRepository.findPostIdByMemberIdAndBoardId(memberId, boardId);
    }

    @Transactional
    public void createPostWithVoiceFiles(PostCreateDto dto, Map<String, MultipartFile> files,Integer memberId, Long boardId) {
        Post post = createPost(dto, memberId, boardId);
        voiceDataService.createVoiceDataList(post,files);
    }

    public Post createPost(PostCreateDto dto, Integer memberId, Long boardId) {
        Member member = memberService.getMemberProxy(memberId);
        Board board = boardService.getBoardByProxy(boardId);

        boolean exists = postRepository.existsByMemberAndBoard(member, board);
        if (exists) {
            throw new CustomException(ErrorCode.DUPLICATE_POST_EXISTS);
        }
        try{
            Post newPost = Post.create(member, board, dto.getMemo());
            return postRepository.saveAndFlush(newPost);
        }catch(DataIntegrityViolationException e){
            throw new CustomException(ErrorCode.DUPLICATE_POST_EXISTS);
        }catch (Exception e){
            throw new CustomException(ErrorCode.INTERNAL_COMMON_ERROR);
        }
    }

    @Transactional
    public void updatePostWithVoiceFiles(PostUpdateDto dto, Map<String, MultipartFile> files, Integer memberId) {
        Post post = updatePost(dto, memberId);
        voiceDataService.updateVoiceDataList(post,files);
    }

    public Post updatePost(PostUpdateDto dto, Integer memberId) {
        Member member = memberService.getMemberProxy(memberId);
        Board board = boardService.getBoardByProxy(1L);
        log.info(dto.getMemo());
        log.info(dto.getMemo());

        try{
            Post updatePost = postRepository.findByMemberAndBoard(member, board)
                    .orElseThrow(()->new CustomException(ErrorCode.RESOURCE_NOT_FOUND));
            updatePost.setMemo(dto.getMemo());
            return postRepository.saveAndFlush(updatePost);
        }catch(DataIntegrityViolationException e){
            throw new CustomException(ErrorCode.DUPLICATE_POST_EXISTS);
        }catch (Exception e){
            e.printStackTrace();
            throw new CustomException(ErrorCode.INTERNAL_COMMON_ERROR);
        }
    }

    @Transactional
    public boolean setChecked(Long id, boolean checked){
        Post updatePost = postRepository.findById(id)
                .orElseThrow(()->new CustomException(ErrorCode.RESOURCE_NOT_FOUND));
        updatePost.setChecked(checked);
        return updatePost.isChecked();
    }
}
