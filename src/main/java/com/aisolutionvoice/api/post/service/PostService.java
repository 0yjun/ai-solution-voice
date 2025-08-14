package com.aisolutionvoice.api.post.service;

import com.aisolutionvoice.api.Board.entity.Board;
import com.aisolutionvoice.api.Board.service.BoardService;
import com.aisolutionvoice.api.member.entity.Member;
import com.aisolutionvoice.api.member.service.MemberService;
import com.aisolutionvoice.api.post.dto.*;
import com.aisolutionvoice.api.post.entity.Post;
import com.aisolutionvoice.api.post.repository.PostRepository;
import com.aisolutionvoice.api.voiceData.service.VoiceDataService;
import com.aisolutionvoice.exception.CustomException;
import com.aisolutionvoice.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

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


    public Page<PostSummaryDto> getSearch(PostSearchRequestDto requestDto, Integer memberId, Pageable pageable) {
        Page<PostSummaryDto> result = postRepository.search(requestDto, memberId, pageable);
        return result.map(PostSummaryDto::applyDefaultTitle);
    }

    public PostDetailDto getByPostId(Long postId){
        List<PostFlatRowDto>  flatRowDtoList =  postRepository.findPostFlatRows(postId);
        return PostDetailDto.fromFlatRows(flatRowDtoList);
    }

    public Optional<Long> findMyPostId(Integer memberId, Long boardId){
        return postRepository.findPostIdByMemberIdAndBoardId(memberId, boardId);
    }

    @Transactional
    public void createPostWithVoiceFiles(PostCreateDto dto, Map<String, MultipartFile> files,Integer memberId) {
        Post post = createPost(dto, memberId);
        voiceDataService.createVoiceDataList(post,files);
    }

    public Post createPost(PostCreateDto dto, Integer memberId) {
        Member member = memberService.getMemberProxy(memberId);
        Board board = boardService.getBoardByProxy(1);

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
        Board board = boardService.getBoardByProxy(1);
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
            throw new CustomException(ErrorCode.INTERNAL_COMMON_ERROR);
        }
    }

    @Transactional
    public void setChecked(Long id, boolean checked){
        Post updatePost = postRepository.findById(id)
                .orElseThrow(()->new CustomException(ErrorCode.RESOURCE_NOT_FOUND));
        updatePost.setChecked(checked);
    }
}
