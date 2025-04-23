package com.manage_store_api.main.controller;

import com.manage_store_api.main.Dto.PostingResponseDto;
import com.manage_store_api.main.Dto.ProductDto;
import com.manage_store_api.main.Dto.RestResponseDto;
import com.manage_store_api.main.Request.UpdateProductRequest;
import com.manage_store_api.main.Utils.CloudinaryUploadUtil;
import com.manage_store_api.main.auth.UserPrincipal;
import com.manage_store_api.main.model.Category;
import com.manage_store_api.main.model.Product;
import com.manage_store_api.main.model.User;
import com.manage_store_api.main.repository.CategoriesRepository;
import com.manage_store_api.main.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/product")
public class ProductController {
    private final ProductRepository productRepository;
    private final CategoriesRepository categoriesRepository;
    private final CloudinaryUploadUtil cloudinaryUploadUtil;

    private static final String FALLBACK_IMAGE_URL = "https://res.cloudinary.com/dgj7gbzlp/image/upload/v1744811745/fallback_ahke41.png";

    @Autowired
    public ProductController(ProductRepository productRepository,
                            CategoriesRepository categoriesRepository,
                            CloudinaryUploadUtil cloudinaryUploadUtil) {
        this.productRepository = productRepository;
        this.categoriesRepository = categoriesRepository;
        this.cloudinaryUploadUtil = cloudinaryUploadUtil;
    }

    @GetMapping("/listAll")
    public ResponseEntity<RestResponseDto> getProducts() {
        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        User user = userPrincipal.getUser();
        Long storeId = user.getStore().getId();
        List<Product> products = productRepository.findByStore_Id(storeId);
        List<ProductDto> productsReturn = products.stream().map(ProductDto::new).collect(Collectors.toList());
        RestResponseDto response = new RestResponseDto(productsReturn, "GET FUNCIONANDO");
        return ResponseEntity.ok(response);
    }

    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PostingResponseDto> createProduct(
            @RequestParam("name") String name,
            @RequestParam("quantity") Float quantity,
            @RequestParam("price") Float price,
            @RequestParam("buyPrice") Float buyPrice,
            @RequestParam("categoryId") Long categoryId,
            @RequestParam(value = "image", required = false) MultipartFile imageFile) {

        try {
            User user = ((UserPrincipal) SecurityContextHolder.getContext()
                    .getAuthentication().getPrincipal()).getUser();

            if (user.getStore() == null) {
                return ResponseEntity.badRequest()
                        .body(new PostingResponseDto(null, "Usuario no tiene tienda asignada"));
            }

            // Validación de campos requeridos
            if (name == null || name.trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(new PostingResponseDto(null, "El nombre es requerido"));
            }

            if (categoryId == null) {
                return ResponseEntity.badRequest()
                        .body(new PostingResponseDto(null, "El ID de categoría es requerido"));
            }

            // Verificar si el producto ya existe
            boolean alreadyExist = productRepository.existsByStore_IdAndName(
                    user.getStore().getId(),
                    name.trim()
            );

            if (alreadyExist) {
                return ResponseEntity.badRequest()
                        .body(new PostingResponseDto(null, "Ya existe un producto con ese nombre en esta tienda"));
            }

            Category category = categoriesRepository.findById(categoryId)
                    .orElseThrow(() -> new RuntimeException("No existe la categoría seleccionada con ID: " + categoryId));

            // Manejo de la imagen
            String imageUrl = FALLBACK_IMAGE_URL;
            if (imageFile != null && !imageFile.isEmpty()) {
                Map<String, Object> uploadResult = cloudinaryUploadUtil.uploadFile(imageFile);
                imageUrl = (String) uploadResult.get("secure_url");
            }

            // Creación del producto
            Product product = new Product();
            product.setName(name.trim());
            product.setQuantity(quantity);
            product.setPrice(price);
            product.setBuyPrice(buyPrice);
            product.setPicture(imageUrl);
            product.setStore(user.getStore());
            product.setCategory(category);

            Product savedProduct = productRepository.save(product);
            ProductDto responseDto = new ProductDto(savedProduct);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new PostingResponseDto(responseDto, "Producto creado exitosamente"));

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(new PostingResponseDto(null, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(new PostingResponseDto(null, "Error interno al crear producto"));
        }
    }


    @PatchMapping(value = "/edit/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PostingResponseDto> updateProduct(
            @PathVariable Long id,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "quantity", required = false) Float quantity,
            @RequestParam(value = "price", required = false) Float price,
            @RequestParam(value = "buyPrice", required = false) Float buyPrice,
            @RequestParam(value = "categoryId", required = false) Long categoryId,
            @RequestParam(value = "image", required = false) MultipartFile imageFile) {

        try {
            User user = ((UserPrincipal) SecurityContextHolder.getContext()
                    .getAuthentication().getPrincipal()).getUser();

            if (user.getStore() == null) {
                return ResponseEntity.badRequest()
                        .body(new PostingResponseDto(null, "Usuario no tiene tienda asignada"));
            }

            Product existingProduct = productRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + id));

            if (!existingProduct.getStore().getId().equals(user.getStore().getId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new PostingResponseDto(null, "No tienes permiso para modificar este producto"));
            }

            // Validación de nombre único si se está cambiando
            if (name != null && !name.equals(existingProduct.getName())) {
                boolean nameExists = productRepository.existsByStore_IdAndName(
                        user.getStore().getId(),
                        name.trim());

                if (nameExists) {
                    return ResponseEntity.badRequest()
                            .body(new PostingResponseDto(null, "Ya existe un producto con ese nombre en esta tienda"));
                }
                existingProduct.setName(name.trim());
            }

            // Manejo de la imagen
            if (imageFile != null && !imageFile.isEmpty()) {
                if (existingProduct.getPicture() != null &&
                        !existingProduct.getPicture().equals(FALLBACK_IMAGE_URL)) {
                    try {
                        String publicId = extractPublicIdFromUrl(existingProduct.getPicture());
                        cloudinaryUploadUtil.deleteFile(publicId);
                    } catch (Exception e) {
                        System.out.println("Error al eliminar imagen anterior: " + e.getMessage());
                    }
                }

                Map<String, Object> uploadResult = cloudinaryUploadUtil.uploadFile(imageFile);
                existingProduct.setPicture((String) uploadResult.get("secure_url"));
            }

            // Actualización de campos
            if (quantity != null) {
                existingProduct.setQuantity(quantity);
            }
            if (price != null) {
                existingProduct.setPrice(price);
            }
            if (buyPrice != null) {
                existingProduct.setBuyPrice(buyPrice);
            }
            if (categoryId != null) {
                Category category = categoriesRepository.findById(categoryId)
                        .orElseThrow(() -> new RuntimeException("No existe la categoría con ID: " + categoryId));
                existingProduct.setCategory(category);
            }

            Product updatedProduct = productRepository.save(existingProduct);
            ProductDto responseDto = new ProductDto(updatedProduct);

            return ResponseEntity.ok()
                    .body(new PostingResponseDto(responseDto, "Producto actualizado exitosamente"));

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(new PostingResponseDto(null, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(new PostingResponseDto(null, "Error interno al actualizar producto"));
        }
    }

    @PatchMapping("/bulk-edit")
    public ResponseEntity<PostingResponseDto> bulkUpdateProducts(
            @Validated @RequestBody List<UpdateProductRequest> requests) {
        try {
            User user = ((UserPrincipal) SecurityContextHolder.getContext()
                    .getAuthentication().getPrincipal()).getUser();

            if (user.getStore() == null) {
                return ResponseEntity.badRequest()
                        .body(new PostingResponseDto(null, "Usuario no tiene tienda asignada"));
            }

            if (requests == null || requests.isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(new PostingResponseDto(null, "No se proporcionaron productos para actualizar"));
            }

            for (UpdateProductRequest request : requests) {
                if (request.getId() == null) {
                    return ResponseEntity.badRequest()
                            .body(new PostingResponseDto(null, "Todos los productos deben incluir un ID"));
                }

                Product existingProduct = productRepository.findById(request.getId())
                        .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + request.getId()));

                if (!existingProduct.getStore().getId().equals(user.getStore().getId())) {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN)
                            .body(new PostingResponseDto(null,
                                    "No tienes permiso para modificar el producto con ID: " + request.getId()));
                }

                if (request.getName() != null && !request.getName().equals(existingProduct.getName())) {
                    boolean nameExists = productRepository.existsByStore_IdAndName(
                            user.getStore().getId(),
                            request.getName().trim());

                    if (nameExists) {
                        return ResponseEntity.badRequest()
                                .body(new PostingResponseDto(null,
                                        "Ya existe un producto con ese nombre en esta tienda (ID: " + request.getId() + ")"));
                    }
                }

                if (request.getName() != null) {
                    existingProduct.setName(request.getName());
                }
                if (request.getQuantity() != null) {
                    existingProduct.setQuantity(request.getQuantity());
                }
                if (request.getPrice() != null) {
                    existingProduct.setPrice(request.getPrice());
                }
                if (request.getBuyPrice() != null) {
                    existingProduct.setBuyPrice(request.getBuyPrice());
                }

                productRepository.save(existingProduct);
            }

            return ResponseEntity.ok()
                    .body(new PostingResponseDto(null, "Productos actualizados exitosamente"));

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(new PostingResponseDto(null, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(new PostingResponseDto(null, "Error interno al actualizar productos"));
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<PostingResponseDto> deleteProduct(@PathVariable Long id) {
        try {
            User user = ((UserPrincipal) SecurityContextHolder.getContext()
                    .getAuthentication().getPrincipal()).getUser();

            if (user.getStore() == null) {
                return ResponseEntity.badRequest()
                        .body(new PostingResponseDto(null, "Usuario no tiene tienda asignada"));
            }

            Product existingProduct = productRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + id));

            if (!existingProduct.getStore().getId().equals(user.getStore().getId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new PostingResponseDto(null, "No tienes permiso para eliminar este producto"));
            }

            // Eliminar imagen de Cloudinary si no es la imagen fallback
            if (existingProduct.getPicture() != null &&
                    !existingProduct.getPicture().equals(FALLBACK_IMAGE_URL)) {
                try {
                    String publicId = extractPublicIdFromUrl(existingProduct.getPicture());
                    cloudinaryUploadUtil.deleteFile(publicId);
                } catch (Exception e) {
                    System.out.println("Error al eliminar imagen del producto: " + e.getMessage());
                }
            }

            productRepository.delete(existingProduct);

            return ResponseEntity.ok()
                    .body(new PostingResponseDto(null, "Producto eliminado exitosamente"));

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(new PostingResponseDto(null, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(new PostingResponseDto(null, "Error interno al eliminar producto"));
        }
    }

    private String extractPublicIdFromUrl(String url) {
        String[] parts = url.split("/upload/");
        if (parts.length < 2) return null;

        String afterUpload = parts[1];
        if (afterUpload.startsWith("v")) {
            afterUpload = afterUpload.substring(afterUpload.indexOf('/') + 1);
        }

        int lastDotIndex = afterUpload.lastIndexOf('.');
        if (lastDotIndex > 0) {
            afterUpload = afterUpload.substring(0, lastDotIndex);
        }

        return afterUpload;
    }
}