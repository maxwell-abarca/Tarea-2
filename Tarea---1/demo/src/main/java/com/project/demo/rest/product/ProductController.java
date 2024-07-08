package com.project.demo.rest.product;

import com.project.demo.logic.entity.category.Category;
import com.project.demo.logic.entity.category.CategoryRepository;
import com.project.demo.logic.entity.product.Product;
import com.project.demo.logic.entity.product.ProductRepository;
import com.project.demo.logic.entity.rol.Role;
import com.project.demo.logic.entity.rol.RoleEnum;
import com.project.demo.logic.entity.rol.RoleRepository;
import com.project.demo.logic.entity.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/products")
public class ProductController {
    @Autowired
    private ProductRepository ProductRepository;

    @Autowired
    private CategoryRepository CategoryRepository;

    @PreAuthorize("hasAnyRole('USER', 'SUPER_ADMIN_ROLE')")
    @GetMapping
    public List<Product> getAllProducts() {
        return ProductRepository.findAll();
    }

    @PreAuthorize("hasAnyRole('SUPER_ADMIN_ROLE')")
    @PostMapping
    public Product addProduct(@RequestBody Product product) {
        Optional<Category> category = CategoryRepository.findById(product.getCategory().getId());
        product.setCategory(category.get());
        return ProductRepository.save(product);
    }

    @GetMapping("/{id}")
    public Product getProductById(@PathVariable Long id) {
        return ProductRepository.findById(id).orElseThrow(RuntimeException::new);
    }

    @PreAuthorize("hasAnyRole('SUPER_ADMIN_ROLE')")
    @PutMapping("/{id}")
    public Product updateProduct(@PathVariable Long id, @RequestBody Product product) {
        Optional<Category> category = CategoryRepository.findById(product.getCategory().getId());
        return ProductRepository.findById(id)
                .map(existingProduct -> {
                    existingProduct.setName(product.getName());
                    existingProduct.setDescription(product.getDescription());
                    existingProduct.setPrice(product.getPrice());
                    existingProduct.setStock(product.getStock());
                    existingProduct.setCategory(category.get());
                    return ProductRepository.save(existingProduct);
                })
                .orElseGet(() -> {
                    product.setId(id);
                    return ProductRepository.save(product);
                });
    }

    @PreAuthorize("hasAnyRole('SUPER_ADMIN_ROLE')")
    @DeleteMapping("/{id}")
    public List<Product> deleteProduct(@PathVariable Long id) {
        Optional<Product> product = ProductRepository.findById(id);
        if(product.isPresent()) {
            ProductRepository.deleteById(id);
        }

        return ProductRepository.findAll();
    }
}
