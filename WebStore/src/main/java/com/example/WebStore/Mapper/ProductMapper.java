package com.example.WebStore.Mapper;

import com.example.WebStore.DTO.ProductDTO;
import com.example.WebStore.model.Category;
import com.example.WebStore.model.Product;
import com.example.WebStore.repository.CategoryRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ProductMapper {

    private final CategoryRepository categoryRepository;

    public ProductMapper(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Product toEntity(ProductDTO productDTO) {

        Product product = new Product();

        product.setId(productDTO.id());
        product.setName(productDTO.name());
        product.setDescription(productDTO.description());
        product.setPrice(productDTO.price());
        product.setImage(productDTO.image());

        Optional<Category> category = categoryRepository
                .findById(productDTO.category_id());

        category.ifPresent(product::setCategory);

        return product;

    }

    public ProductDTO toDTO(Product product) {

        return new ProductDTO(product.getId(),
                product.getName(), product.getDescription(),
                product.getPrice(), product.getImage(),
                product.getCategory().getId(),
                product.getCategory().getName());
    }
}
