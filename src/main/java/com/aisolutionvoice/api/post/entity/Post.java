package com.aisolutionvoice.api.post.entity;

import com.aisolutionvoice.api.Board.entity.Board;
import com.aisolutionvoice.api.member.entity.Member;
import com.aisolutionvoice.api.voiceData.entity.VoiceData;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "post", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "board_id"})
})
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String memo;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    // 과제 템플릿 (Board)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VoiceData> voiceDataList = new ArrayList<>();

    // 생성 시간 (선택)
    @CreationTimestamp
    private LocalDateTime createdAt;

    // 수정 시간 (선택)
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public static Post create(Member member, Board board, String title) {
        return Post.builder()
                .member(member)
                .board(board)
                .title(title)
                .build();
    }

    public void setBoard(Board board){
        this.board = board;
    }
    //== 비즈니스 로직 ==//
    public void addVoiceData(VoiceData data) {
        voiceDataList.add(data);
        data.setPost(this);
    }

}
