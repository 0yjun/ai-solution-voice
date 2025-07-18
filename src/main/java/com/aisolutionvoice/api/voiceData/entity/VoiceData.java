package com.aisolutionvoice.api.voiceData.entity;

import com.aisolutionvoice.api.Board.entity.Board;
import com.aisolutionvoice.api.HotwordScript.entity.HotwordScript;
import com.aisolutionvoice.api.member.entity.Member;
import com.aisolutionvoice.api.post.entity.Post;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class VoiceData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String audioFilePath;

    private Double duration; // 초 단위 길이 (선택)

    @CreationTimestamp
    private LocalDateTime submittedAt;

    // 어느 게시글(참여 세션)에서
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    // 어떤 호출어에 대해
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "hotword_script_id", nullable = false)
    private HotwordScript hotwordScript;

    public void setPost(Post post) {
        this.post = post;
    }
}
