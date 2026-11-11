package com.example.WebStore.Controller;

import com.example.WebStore.DTO.ProductDTO;
import com.example.WebStore.Service.ProductService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://127.0.0.1:5500")
@RestController
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;

    public ProductController(
            ProductService productService
    ) {
        this.productService = productService;
    }

    //Get
    @GetMapping()
    public List<ProductDTO> findProducts() {

        return productService.findProduct();
    }

    // Get - Id
    @GetMapping("/{id}")
    public Optional<ProductDTO> findById(@PathVariable Long id) {

        return productService.findById(id);
    }

    // Get - Name
    @GetMapping("/search")
    public List<ProductDTO> queryByNameLike(
            @RequestParam String name) {

        return productService.findByNameLike(name);
    }

    // Post
    @PostMapping("/add-product")
    public ProductDTO saveProduct(@RequestBody
                                  ProductDTO productDTO) {

        return productService.saveProduct(productDTO);
    }

    //Put
    @PutMapping("/update-product/{id}")
    public Optional<ProductDTO> updateProduct(@PathVariable Long id,
                                    @RequestBody ProductDTO productDTO) {

        return productService.updateProduct(id, productDTO);
    }

    // Delete
    @DeleteMapping("/delete/{id}")
    public void deleteProduct(@PathVariable Long id) {

        productService.deleteProduct(id);
    }
}
