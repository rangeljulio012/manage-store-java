package com.manage_store_api.main.controller;

import com.manage_store_api.main.Dto.CategoryDto;
import com.manage_store_api.main.Dto.PostingResponseDto;
import com.manage_store_api.main.Dto.RestResponseDto;
import com.manage_store_api.main.auth.UserPrincipal;
import com.manage_store_api.main.model.Category;
import com.manage_store_api.main.model.User;
import com.manage_store_api.main.repository.CategoriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoriesController {

    private final CategoriesRepository categoriesRepository;

    @Autowired
    public CategoriesController(CategoriesRepository categoriesRepository) {
        this.categoriesRepository = categoriesRepository;
    }

    // GET /api/categories - Listar todas las categorias
    @GetMapping("/listAll")
    public ResponseEntity<RestResponseDto> getStores() {
        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        User user = userPrincipal.getUser();
        Long storeId = user.getStore().getId();
        List<Category> categories = categoriesRepository.findByStore_id(storeId);
        RestResponseDto response = new RestResponseDto(categories, "GET FUNCIONANDO");
        return ResponseEntity.ok(response);
    }

    // POST /api/categories - Crear una nueva categoría
    @PostMapping("/create")
    public ResponseEntity<PostingResponseDto> createCategory(@RequestBody Category category) {
        try {
            User user = ((UserPrincipal) SecurityContextHolder.getContext()
                    .getAuthentication().getPrincipal()).getUser();

            if (user.getStore() == null) {
                return ResponseEntity.badRequest()
                        .body(new PostingResponseDto(null, "Usuario sin tienda asignada"));
            }

            Boolean alreadyExist = categoriesRepository.existsByStore_IdAndName(
                    user.getStore().getId(),
                    category.getName().trim()
            );

            if (alreadyExist) {
                return ResponseEntity.badRequest()
                        .body(new PostingResponseDto(null, "Ya existe una categoría con ese nombre en esta tienda"));
            }

            category.setStore(user.getStore());
            Category savedCategory = categoriesRepository.save(category);

            CategoryDto responseDto = new CategoryDto(savedCategory);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new PostingResponseDto(responseDto, "Categoría creada"));

        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(new PostingResponseDto(null, "Error: " + e.getMessage()));
        }
    }
}