package com.manage_store_api.main.repository;

import com.manage_store_api.main.model.Product;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByStore_Id(Long id);
    List<Product> findByCategory_Id(Long id);
    boolean existsByStore_IdAndName(Long storeId, String name);

    @Modifying
    @Transactional
    @Query("UPDATE Product p SET p.quantity = p.quantity - :quantity WHERE p.id = :productId AND p.store.id = :storeId")
    int decrementQuantity(@Param("productId") Long productId,
                          @Param("storeId") Long storeId,
                          @Param("quantity") Integer quantity);
}