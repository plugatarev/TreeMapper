package com.github.plugatarev.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class TreeDTO {

    private Integer id;
    private String name;
    private List<TreeDTO> children;

    public TreeDTO(Integer id, String name) {
        this.id = id;
        this.name = name;
    }
}