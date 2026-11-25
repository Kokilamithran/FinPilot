package com.finpilot.dto.response;

import lombok.Data;

@Data
public class CategoryResponse {

    private Long categoryId;

    private String categoryName;

    private String categoryType;

    private String icon;

    private String colour;

    private Long parentCategoryId;

    private Boolean isSystem;

    private Long userId;

    private Integer displayOrder;
}