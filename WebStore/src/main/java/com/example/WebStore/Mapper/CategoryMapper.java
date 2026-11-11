package com.example.WebStore.Mapper;

import com.example.WebStore.DTO.CategoryDTO;
import com.example.WebStore.model.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    public static Category toEntity(CategoryDTO categoryDTO) {

        Category category = new Category();
        category.setId(categoryDTO.id());
        category.setName(categoryDTO.name());

        return category;
    }

    public static CategoryDTO toDTO(Category category) {

        return new CategoryDTO(
                category.getId(), category.getName()
        );
    }
}
