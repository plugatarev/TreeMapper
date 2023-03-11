package com.github.plugatarev.mapper;

import com.github.plugatarev.dto.TreeDTO;
import com.github.plugatarev.entity.TreeEntity;

import java.util.*;

public class TreeMapper {

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
