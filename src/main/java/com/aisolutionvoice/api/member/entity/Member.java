package com.aisolutionvoice.api.member.entity;

import com.aisolutionvoice.api.Role.domain.Role;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;

    @Column(nullable = false, unique = true, length = 50)
    private String loginId;

    @Column(nullable = false)
    private String password;

    @Column(length = 100)
    private String di;  // 본인확인값, nullable

    @Column(length = 6)
    private String birth;  // 주민번호 앞자리, nullable

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;
}