package com.github.plugatarev.mapper;

import com.github.plugatarev.dto.TreeDTO;
import com.github.plugatarev.entity.TreeEntity;

import java.util.*;

public class TreeMapper {

    // Если TreeEntity - это не сущности БД или если в БД есть места, которые нарушают условия целостности БД, то нужно
    // вводить дополнительные проверки на то, что id != null.
    // Или если у какой-то ноды есть parentId, который на самом деле не существует в списке нод, то одного прохода не хватит.
    // Также есть вариант присутствия циклов и петель.
    public static Collection<TreeDTO> convert(Collection<TreeEntity> entities) {
        if (entities == null) {
            throw new IllegalArgumentException("Parameter 'entities' cannot be null");
        }

        Map<Integer, TreeDTO> nodes = new HashMap<>();

        entities.forEach(entity -> {
            TreeDTO currentTreeDTO = new TreeDTO(entity.getId(), entity.getName(), null);

            TreeDTO mapCurrentDto = nodes.putIfAbsent(entity.getId(), currentTreeDTO);
            if (mapCurrentDto != null) {
                mapCurrentDto.setName(entity.getName());
            }

            if (entity.getParentId() != null) {
                handleParent(nodes, entity.getParentId(), currentTreeDTO);
            }
        });

        return nodes.values();
    }

    private static void handleParent(Map<Integer, TreeDTO> nodes, Integer parentId , TreeDTO child) {
        TreeDTO mapCurrentDto = nodes.computeIfAbsent(parentId, id -> new TreeDTO(id, null, new ArrayList<>()));
        List<TreeDTO> children = mapCurrentDto.getChildren();
        if (children == null) {
            children = new ArrayList<>();
            mapCurrentDto.setChildren(children);
        }
        children.add(child);
    }
}
