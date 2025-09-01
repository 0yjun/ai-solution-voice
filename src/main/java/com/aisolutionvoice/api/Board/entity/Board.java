package com.aisolutionvoice.api.Board.entity;

import com.aisolutionvoice.api.HotwordScript.entity.HotwordScript;
import com.aisolutionvoice.api.post.entity.Post;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "board")
@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 게시판 이름 (ex. "차량 호출어 수집")
    @Column(nullable = false)
    private String name;

    // 게시판 설명 (선택)
    private String description;

    // 게시글 목록 (사용자 참여)
    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Post> posts = new ArrayList<>();

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    // Soft delete (선택)
    @Column(nullable = false)
    private boolean deleted = false;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HotwordScript> scripts = new ArrayList<>();

    public void addPost(Post post) {
        posts.add(post);
        post.setBoard(this);
    }

    public void addScript(HotwordScript script){
        scripts.add(script);
        script.setBoard(this);
    }

    public void updateFromDto(com.aisolutionvoice.api.Board.dto.BoardFormDto boardFormDto) {
        this.name = boardFormDto.getBoardName();
        this.description = boardFormDto.getDescription();
    }
}