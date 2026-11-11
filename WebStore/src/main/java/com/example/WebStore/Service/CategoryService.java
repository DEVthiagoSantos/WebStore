package com.example.WebStore.Service;

import com.example.WebStore.DTO.CategoryDTO;
import com.example.WebStore.Mapper.CategoryMapper;
import com.example.WebStore.model.Category;
import org.springframework.stereotype.Service;
import com.example.WebStore.repository.CategoryRepository;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService (
            CategoryRepository categoryRepository
    ) {
        this.categoryRepository = categoryRepository;
    }

    //Save
    public CategoryDTO saveCategory(CategoryDTO categoryDTO) {

        Category category = CategoryMapper.toEntity(categoryDTO);

        Category saveCategory = categoryRepository.save(category);

        return CategoryMapper.toDTO(saveCategory);
    }

    //Get
    public List<CategoryDTO> findCategory() {

        List<Category> categories = categoryRepository.findAll();

        return categories.stream()
                .map(CategoryMapper::toDTO)
                .toList();
    }

    //Get - Id
    public Optional<CategoryDTO> findByIdCategory(Long id) {

        Optional<Category> category = categoryRepository
                .findById(id);

        return category.map(CategoryMapper::toDTO);
    }

    //Put
    public Optional<CategoryDTO> updateCategory(
            Long id,
            CategoryDTO categoryDTO) {

        Optional<Category> existingCategory =
                categoryRepository.findById(id);

        if (existingCategory.isPresent()) {

            Category category = existingCategory.get();

            category.setName(categoryDTO.name());

            Category savedCategory = categoryRepository
                    .save(category);

            return Optional.of(CategoryMapper.toDTO(savedCategory));
        }

        return Optional.empty();
    }

    //Delete - Id
    public void deleteCategory(Long id) {

        categoryRepository.deleteById(id);
    }
}
