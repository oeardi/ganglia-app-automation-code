package com.entity;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author 冷枫红舞
 */
@Data
public class CasesEntity {
    private String name;
    private String description;
    private List<Map<String, Object>> modules;
}
