package com.manage_store_api.main.controller;

import com.manage_store_api.main.Dto.RestResponseDto;
import com.manage_store_api.main.model.Store;
import com.manage_store_api.main.repository.StoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stores")
public class StoreController {

    private final StoreRepository storeRepository;

    @Autowired
    public StoreController(StoreRepository storeRepository) {
        this.storeRepository = storeRepository;
    }

    // GET /api/stores - Listar todas las tiendas
    @GetMapping("/listAll")
    public ResponseEntity<RestResponseDto> getStores() {
        List<Store> stores = storeRepository.findAll();
        RestResponseDto response = new RestResponseDto(stores, "GET FUNCIONANDO");
        return ResponseEntity.ok(response);
    }

    // POST /api/stores - Crear una nueva tienda
    @PostMapping("/create")
    public ResponseEntity<Store> createStore(@RequestBody Store store) {
        Store savedStore = storeRepository.save(store);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedStore);
    }
}