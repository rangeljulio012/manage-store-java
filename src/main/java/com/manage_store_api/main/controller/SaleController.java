package com.manage_store_api.main.controller;

import com.manage_store_api.main.Dto.PostingResponseDto;
import com.manage_store_api.main.Dto.RestResponseDto;
import com.manage_store_api.main.Dto.SaleDto;
import com.manage_store_api.main.Request.CreateSaleRequest;
import com.manage_store_api.main.Utils.Enums.SalesStatus;
import com.manage_store_api.main.Utils.Enums.SalespPaymentMethod;
import com.manage_store_api.main.Utils.SaleItem;
import com.manage_store_api.main.auth.UserPrincipal;
import com.manage_store_api.main.model.Product;
import com.manage_store_api.main.model.Sale;
import com.manage_store_api.main.model.Store;
import com.manage_store_api.main.model.User;
import com.manage_store_api.main.repository.ClientsRepository;
import com.manage_store_api.main.repository.ProductRepository;
import com.manage_store_api.main.repository.SalesRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/sales")
public class SaleController {
    private final SalesRepository salesRepository;
    private final ClientsRepository clientsRepository;
    private final ProductRepository productRepository;

    @Autowired
    public SaleController(SalesRepository salesRepository, ClientsRepository clientsRepository, ProductRepository productRepository) {
        this.salesRepository = salesRepository;
        this.clientsRepository = clientsRepository;
        this.productRepository = productRepository;
    }

    @GetMapping("/listAll")
    public ResponseEntity<RestResponseDto> getSales() {
        try {
            UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext()
                    .getAuthentication().getPrincipal();
            User user = userPrincipal.getUser();
            Long storeId = user.getStore().getId();
            List<Sale> sales = salesRepository.findByStore_id(storeId);
            List<SaleDto> salesReturn = sales.stream().map(SaleDto::new).collect(Collectors.toList());
            RestResponseDto response = new RestResponseDto(salesReturn, "GET FUNCIONANDO");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(new RestResponseDto(null, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(new RestResponseDto(null, "Error interno al crear producto"));
        }
    }

    @PostMapping("/create")
    public ResponseEntity<PostingResponseDto> createSale(@RequestBody @Valid CreateSaleRequest request) {
        try {

            // Validación adicional manual (opcional, redundante con la validación de clase)
            if ((request.getPaymentMethod() == SalespPaymentMethod.PAGO_MOVIL ||
                    request.getPaymentMethod() == SalespPaymentMethod.TRANSFERENCIA) &&
                    (request.getPaymentReference() == null || request.getPaymentReference().trim().isEmpty())) {

                return ResponseEntity.badRequest()
                        .body(new PostingResponseDto(null,
                                "La referencia de pago es obligatoria para PAGO_MOVIL o TRANSFERENCIA"));
            }

            // Obtener usuario autenticado
            UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext()
                    .getAuthentication().getPrincipal();
            User user = userPrincipal.getUser();
            Store store = user.getStore();

            // Validar tienda
            if (store == null) {
                return ResponseEntity.badRequest()
                        .body(new PostingResponseDto(null, "Usuario no tiene tienda asignada"));
            }

            // Validar cliente
            boolean clientExists = clientsRepository.existsByStore_IdAndId(store.getId(), request.getClient_id());
            if (!clientExists) {
                return ResponseEntity.badRequest()
                        .body(new PostingResponseDto(null, "El cliente seleccionado no existe"));
            }

            // Validar productos y stock
            List<SaleItem> items = request.getItems();
            List<Product> inventory = productRepository.findByStore_Id(store.getId());

            for (SaleItem item : items) {
                Product product = inventory.stream()
                        .filter(p -> p.getId().equals(item.getProductId()))
                        .findFirst()
                        .orElse(null);

                if (product == null) {
                    return ResponseEntity.badRequest()
                            .body(new PostingResponseDto(null,
                                    "El producto con ID " + item.getProductId() + " no existe en la tienda"));
                }

                if (item.getQuantity() > product.getQuantity()) {
                    return ResponseEntity.badRequest()
                            .body(new PostingResponseDto(null,
                                    "El producto " + product.getName() + " no cumple con la existencia requerida"));
                }
            }

            // Crear la venta
            Sale newSale = new Sale();
            newSale.setAddress(request.getAddress());
            newSale.setClient(clientsRepository.findById(request.getClient_id()).orElse(null));
            newSale.setTotal(request.getTotal());
            newSale.setAppliedRate(request.getAppliedRate());
            newSale.setOficialRate(request.getOficialRate());
            newSale.setItems(items);
            newSale.setStore(store);
            newSale.setStatus(SalesStatus.COMPLETED);
            newSale.setPaymentReference(request.getPaymentReference());
            newSale.setPaymentMethod(request.getPaymentMethod());

            Sale savedSale = salesRepository.save(newSale);

            // Actualizar inventario
            for (SaleItem item : items) {
                int updated = productRepository.decrementQuantity(
                        item.getProductId(),
                        store.getId(),
                        item.getQuantity()
                );

                if (updated == 0) {
                    return ResponseEntity.badRequest()
                            .body(new PostingResponseDto(null,
                                    "No se pudo actualizar el stock para el producto ID: " + item.getProductId()));
                }
            }

            return ResponseEntity.ok(new PostingResponseDto(savedSale.getId(), "Venta creada exitosamente"));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(new PostingResponseDto(null, "Error al crear la venta: " + e.getMessage()));
        }
    }
}
