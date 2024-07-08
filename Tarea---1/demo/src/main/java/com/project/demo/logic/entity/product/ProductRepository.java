package com.project.demo.logic.entity.product;
import com.project.demo.logic.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query(value = "SELECT * FROM Product", nativeQuery = true)
    List<Product> findAll();

    @Query("SELECT u FROM Product u WHERE u.name = ?1")
    Optional<Product> findByName(String name);
}
