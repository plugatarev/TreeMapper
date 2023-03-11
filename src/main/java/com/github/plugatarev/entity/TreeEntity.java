package com.github.plugatarev.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TreeEntity {

    private Integer id;

    private String name;

    private Integer parentId;
}