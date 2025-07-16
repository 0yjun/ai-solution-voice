package com.aisolutionvoice.api.post.service;

import com.aisolutionvoice.api.post.repository.PostRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostService {
    private  final PostRepository postRepository;


}
