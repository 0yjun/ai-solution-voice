package com.aisolutionvoice.api.Board.entity;

import com.aisolutionvoice.api.HotwordScript.entity.HotwordScript;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Entity
@Table(name = "board_script_mapping")
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class BoardScriptMapping {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hotword_script_id", nullable = false)
    private HotwordScript hotwordScript;

    // UI 노출 순서 등
    private int displayOrder;

    public void setBoard(Board board) {
        this.board = board;
    }
}
