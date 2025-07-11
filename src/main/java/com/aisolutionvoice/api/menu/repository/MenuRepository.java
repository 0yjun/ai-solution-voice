package com.aisolutionvoice.api.menu.repository;

import com.aisolutionvoice.api.menu.entity.Menu;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Integer> {
    /**
     * 1) isActive = true인 최상위 메뉴 + 자식 (fetch join children)
     */
    @EntityGraph(attributePaths = {"children"})
    List<Menu> findByParentIsNullAndIsActiveTrueOrderBySeq();
}