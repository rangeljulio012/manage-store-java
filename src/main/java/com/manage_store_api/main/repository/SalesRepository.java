package com.manage_store_api.main.repository;

import com.manage_store_api.main.model.Sale;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SalesRepository extends JpaRepository<Sale, Long> {
    List<Sale> findByStore_id(Long id);
}