package com.aisolutionvoice.api.post.service;

import com.aisolutionvoice.api.Board.entity.Board;
import com.aisolutionvoice.api.Board.service.BoardService;
import com.aisolutionvoice.api.member.entity.Member;
import com.aisolutionvoice.api.member.service.MemberService;
import com.aisolutionvoice.api.post.dto.PostCreateDto;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
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

    @Transactional
    public void createPostWithVoiceFiles(PostCreateDto dto, Map<String, MultipartFile> files,Integer memberId) {
        try {
            Post post = savePost(dto, memberId);
            voiceDataService.saveVoiceDataList(post,files);
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error("Post 생성 중 예외 발생", e);
            throw new CustomException(ErrorCode.INTERNAL_COMMON_ERROR);
        }
    }

    private Post savePost(PostCreateDto dto, Integer memberId) {
        Member member = memberService.getMemberProxy(memberId);
        Board board = boardService.getBoardByProxy(1);

        Optional<Post> optionalPost = postRepository.findByMemberAndBoard(member, board);

        if (optionalPost.isPresent()) {
            Post existingPost = optionalPost.get();
            existingPost.setMemo(dto.getMemo());
            return postRepository.save(existingPost);
        } else {
            Post newPost = Post.create(member,board, dto.getMemo());
            return postRepository.save(newPost);
        }
    }
}
