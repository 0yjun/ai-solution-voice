package com.aisolutionvoice.api.menu.entity;

import com.aisolutionvoice.api.Role.converter.RoleConverter;
import com.aisolutionvoice.api.Role.domain.Role;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "menu")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString(exclude = {"parent","children","prevMenu","nextMenu"})
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 100, unique = true)
    private String name;

    @Column(length = 255)
    private String description;

    @Column(nullable = false, length = 200)
    private String url;

    @Column(nullable = false)
    private Integer seq;

    @Column(length = 100)
    private String icon;

    @Convert(converter = RoleConverter.class)
    @Column(name = "roles", nullable = false, length = 100)
    @Builder.Default
    private Set<Role> roles = new HashSet<>();

    @Column(nullable = false)
    private boolean isActive;
    @Column(name = "parent_id", insertable = false, updatable = false)
    private Integer parentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Menu parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Menu> children = new ArrayList<>();
}
