package com.project.demo.rest.product;

import com.project.demo.logic.entity.category.Category;
import com.project.demo.logic.entity.category.CategoryRepository;
import com.project.demo.logic.entity.product.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/categories")
public class CategoryController {
    @Autowired
    private CategoryRepository CategoryRepository;

    @PreAuthorize("hasAnyRole('USER', 'SUPER_ADMIN_ROLE')")
    @GetMapping
    public List<Category> getAllCategories() {
        return CategoryRepository.findAll();
    }

    @PreAuthorize("hasAnyRole('SUPER_ADMIN_ROLE')")
    @PostMapping
    public Category addProduct(@RequestBody Category category) {
        return CategoryRepository.save(category);
    }

    @GetMapping("/{id}")
    public Category getProductById(@PathVariable Long id) {
        return CategoryRepository.findById(id).orElseThrow(RuntimeException::new);
    }

    @PreAuthorize("hasAnyRole('SUPER_ADMIN_ROLE')")
    @PutMapping("/{id}")
    public Category updateCategory(@PathVariable Long id, @RequestBody Category category) {
        return CategoryRepository.findById(id)
                .map(existingCategory -> {
                    existingCategory.setName(category.getName());
                    existingCategory.setDescription(category.getDescription());
                    return CategoryRepository.save(existingCategory);
                })
                .orElseGet(() -> {
                    category.setId(id);
                    return CategoryRepository.save(category);
                });
    }

    @PreAuthorize("hasAnyRole('SUPER_ADMIN_ROLE')")
    @DeleteMapping("/{id}")
    public List<Category> deleteCategory(@PathVariable Long id) {
        Optional<Category> category = CategoryRepository.findById(id);
        if(category.isPresent()) {
            CategoryRepository.deleteById(id);
        }
       return CategoryRepository.findAll();
    }
}
