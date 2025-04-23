package com.manage_store_api.main.repository;

import com.manage_store_api.main.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoriesRepository extends JpaRepository<Category, Long> {
    List<Category> findByStore_id(Long id);
    Optional<Category> findByStoreIdAndId(Long storeId, Long id);
    boolean existsByStore_IdAndName(Long storeId, String name);
}