package com.aisolutionvoice.api.terms.entity;

import com.aisolutionvoice.api.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberTerms {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Member member;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Terms terms;

    private LocalDateTime agreedAt;
}
