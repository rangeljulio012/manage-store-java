package com.manage_store_api.main.repository;

import com.manage_store_api.main.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClientsRepository extends JpaRepository<Client, Long> {
    List<Client> findByStore_id(Long id);
    boolean existsByStore_IdAndName(Long storeId, String name);
    boolean existsByStore_IdAndDocument(Long storeId, String document);
    boolean existsByStore_IdAndId(Long storeId, Long id);
}
