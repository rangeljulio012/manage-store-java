package com.manage_store_api.main.repository;

import com.manage_store_api.main.model.Bill;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BillsRepository extends JpaRepository<Bill, Long> {
    List<Bill> findByStore_id(Long id);
}