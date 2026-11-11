package com.example.WebStore.Service;

import com.example.WebStore.DTO.ProductDTO;
import com.example.WebStore.Mapper.ProductMapper;
import com.example.WebStore.model.Category;
import com.example.WebStore.model.Product;
import com.example.WebStore.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import com.example.WebStore.repository.ProductRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final CategoryRepository categoryRepository;

    public ProductService(
            ProductRepository productRepository,
            ProductMapper productMapper,
            CategoryRepository categoryRepository
    ) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
        this.categoryRepository = categoryRepository;
    }

    //Save
    public ProductDTO saveProduct(ProductDTO productDTO) {

        Product product = productRepository
                .save(productMapper.toEntity(productDTO));

        return productMapper.toDTO(product);
    }

    //Get
    public List<ProductDTO> findProduct() {

        List<Product> products = productRepository
                .findAll();

        return products.stream()
                .map(productMapper::toDTO)
                .toList();
    }

    //Get - Id
    public Optional<ProductDTO> findById(Long id) {

        Optional<Product> product = productRepository
                .findById(id);

        return product.map(productMapper::toDTO);
    }

    //Get - Name
    public List<ProductDTO> findByNameLike(String name) {

        List<Product> products = productRepository
                .queryByNameLike(name + "%");

        return products.stream()
                .map(productMapper::toDTO)
                .toList();
    }

    //Put
    public Optional<ProductDTO> updateProduct(Long id,
                                    ProductDTO productDTO) {

        Optional<Product> existingProduct = productRepository.findById(id);

        if (existingProduct.isPresent()) {

            Product product = existingProduct.get();

            product.setName(productDTO.name());
            product.setDescription(productDTO.description());
            product.setImage(productDTO.image());
            product.setPrice(productDTO.price());

            Optional<Category> category = categoryRepository
                    .findById(productDTO.category_id());

            category.ifPresent(product::setCategory);

            Product savedProduct = productRepository.save(product);

            return Optional.of(productMapper.toDTO(savedProduct));

        }

        return Optional.empty();
    }

    //Delete
    public void deleteProduct(Long id) {

        Optional<Product> product = productRepository
                .findById(id);

        if (product.isPresent()) {

            productRepository.deleteById(id);
        }
    }

}
