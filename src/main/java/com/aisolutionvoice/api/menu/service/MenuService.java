package com.aisolutionvoice.api.menu.service;

import com.aisolutionvoice.api.Role.domain.Role;
import com.aisolutionvoice.api.menu.dto.MenuClientDto;
import com.aisolutionvoice.api.menu.entity.Menu;
import com.aisolutionvoice.api.menu.repository.MenuRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MenuService {
    private final MenuRepository menuRepository;
    private final ModelMapper modelMapper;

    /**
     * 사용자 권한별 메뉴 조회
     * @return 권한에 맞는 사용자 메뉴 트리
     */
    public List<MenuClientDto> getAccessibleMenusByUserRole(Role userRole) {
        return menuRepository.findByParentIsNullAndIsActiveTrueOrderBySeq().stream()
                // (1) 부모 권한 필터
                .filter(parent -> parent.getRoles().contains(userRole))
                // (2) 자식 활성·권한 필터
                .peek(parent -> {
                    List<Menu> filteredChildren = parent.getChildren().stream()
                            .filter(child -> child.isActive())          // 자식 활성 여부
                            .filter(child -> child.getRoles().contains(userRole))  // 자식 권한
                            .collect(Collectors.toList());
                    parent.setChildren(filteredChildren);
                })
                // (3) DTO 매핑
                .map(this::toClientMenuDto)   // 재귀 호출
                .collect(Collectors.toList());
    }

    private MenuClientDto toClientMenuDto(Menu menu) {
        // 1) 기본 필드 매핑
        MenuClientDto dto = modelMapper.map(menu, MenuClientDto.class);
        // 2) children 재귀 매핑
        List<MenuClientDto> childDtos = menu.getChildren().stream()
                .map(this::toClientMenuDto)
                .collect(Collectors.toList());
        dto.setChildren(childDtos);
        return dto;
    }
}
