package com.project.demo.logic.entity.category;

import com.project.demo.logic.entity.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Query(value = "SELECT * FROM Category", nativeQuery = true)
    List<Category> findAll();

    @Query("SELECT u FROM Category u WHERE u.name = ?1")
    Optional<Category> findByName(String name);
}