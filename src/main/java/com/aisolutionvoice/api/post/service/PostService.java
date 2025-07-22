package com.aisolutionvoice.api.post.service;

import com.aisolutionvoice.api.Board.entity.Board;
import com.aisolutionvoice.api.Board.service.BoardService;
import com.aisolutionvoice.api.member.entity.Member;
import com.aisolutionvoice.api.member.service.MemberService;
import com.aisolutionvoice.api.post.dto.PostCreateDto;
import com.aisolutionvoice.api.post.dto.PostDetailDto;
import com.aisolutionvoice.api.post.dto.PostFlatRowDto;
import com.aisolutionvoice.api.post.dto.PostSummaryDto;
import com.aisolutionvoice.api.post.entity.Post;
import com.aisolutionvoice.api.post.repository.PostRepository;
import com.aisolutionvoice.api.voiceData.entity.VoiceData;
import com.aisolutionvoice.api.voiceData.repository.VoiceDataRepository;
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
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
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
    private final ModelMapper modelMapper;
    private final VoiceDataService voiceDataService;


    public Page<PostSummaryDto> getByBoardId(Long boardId, Pageable pageable){
        return postRepository.findSummaryByBoardId(boardId, pageable)
                .map(PostSummaryDto::applyDefaultTitle);
    }

    public PostDetailDto getByPostId(Long postId){
        List<PostFlatRowDto>  flatRowDtoList =  postRepository.findPostFlatRows(postId);
        return PostDetailDto.fromFlatRows(flatRowDtoList);
    }

    @Transactional
    public void createPostWithVoiceFiles(PostCreateDto dto, Map<String, MultipartFile> files,Integer memberId) {
        Post post = createPost(dto, memberId);
        voiceDataService.saveVoiceDataList(post,files);
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
}
