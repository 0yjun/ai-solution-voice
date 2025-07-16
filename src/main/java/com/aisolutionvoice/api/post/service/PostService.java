package com.aisolutionvoice.api.post.service;

import com.aisolutionvoice.api.post.dto.PostSummaryDto;
import com.aisolutionvoice.api.post.repository.PostRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostService {
    private  final PostRepository postRepository;
    private final ModelMapper modelMapper;

    public Page<PostSummaryDto> getByBoardId(Long boardId, Pageable pageable){
        return postRepository.findByBoardId(boardId, pageable)
                .map(post->modelMapper.map(post, PostSummaryDto.class));
    }
}
