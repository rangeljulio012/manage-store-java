package com.manage_store_api.main.repository;

import com.manage_store_api.main.model.Store;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreRepository extends JpaRepository<Store, Long> {
}