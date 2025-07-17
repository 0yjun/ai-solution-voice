package com.aisolutionvoice.api.HotwordScript.entity;

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
    private Long script_id;

    @Column(nullable = false)
    private String text;
}
