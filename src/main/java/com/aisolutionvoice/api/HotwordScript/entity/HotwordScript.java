package com.aisolutionvoice.api.HotwordScript.entity;

import com.aisolutionvoice.api.Board.entity.Board;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HotwordScript {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long scriptId;

    @Column(nullable = false)
    public String text;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", nullable = false)
    @JsonIgnore
    public Board board;
}
