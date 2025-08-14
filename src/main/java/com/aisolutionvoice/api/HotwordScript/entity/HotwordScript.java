package com.aisolutionvoice.api.HotwordScript.entity;

import com.aisolutionvoice.api.Board.entity.Board;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class HotwordScript {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long scriptId;

    @Column(nullable = false)
    public String text;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", nullable = true)
    @JsonIgnore
    public Board board;
}
