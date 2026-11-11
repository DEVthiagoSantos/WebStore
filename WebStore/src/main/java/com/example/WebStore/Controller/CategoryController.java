package com.example.WebStore.Controller;

import com.example.WebStore.DTO.CategoryDTO;
import com.example.WebStore.Service.CategoryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://127.0.0.1:5500")
@RestController
@RequestMapping("/category")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    //Get
    @GetMapping()
    public List<CategoryDTO> findAll() {

        return categoryService.findCategory();
    }

    //Post
    @PostMapping("/add")
    public CategoryDTO saveCategory(@RequestBody
                                        CategoryDTO categoryDTO) {

        return categoryService.saveCategory(categoryDTO);
    }

    //Get - Id
    @GetMapping("/category/{id}")
    public Optional<CategoryDTO> findById(@PathVariable Long id) {

        return categoryService.findByIdCategory(id);
    }

    //Put
    @PutMapping("/up/{id}")
    public Optional<CategoryDTO>
    updateCategory(@PathVariable Long id,
                                      @RequestBody
                                      CategoryDTO categoryDTO) {

        return categoryService.updateCategory(id, categoryDTO);
    }

    //Delete - Id
    @DeleteMapping("/delete/{id}")
    public void deleteCategory(@PathVariable Long id) {

        categoryService.deleteCategory(id);
    }

}
