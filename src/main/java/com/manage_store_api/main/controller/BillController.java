package com.manage_store_api.main.controller;

import com.manage_store_api.main.Dto.BillDto;
import com.manage_store_api.main.Dto.PostingResponseDto;
import com.manage_store_api.main.Dto.RestResponseDto;
import com.manage_store_api.main.auth.UserPrincipal;
import com.manage_store_api.main.model.Bill;
import com.manage_store_api.main.model.User;
import com.manage_store_api.main.repository.BillsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bills")
public class BillController {

    private final BillsRepository billsRepository;
    @Autowired
    public BillController(BillsRepository billsRepository) {
        this.billsRepository = billsRepository;
    }

    //Listar todos los gastos
    @GetMapping("/listAll")
    public ResponseEntity<RestResponseDto> getBills() {
        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        User user = userPrincipal.getUser();
        Long storeId = user.getStore().getId();
        List<Bill> bills = billsRepository.findByStore_id(storeId);
        RestResponseDto response = new RestResponseDto(bills, "GET FUNCIONANDO");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/create")
    public ResponseEntity<PostingResponseDto> createBill(@RequestBody Bill bill) {
        try {
            User user = ((UserPrincipal) SecurityContextHolder.getContext()
                    .getAuthentication().getPrincipal()).getUser();

            if (user.getStore() == null) {
                return ResponseEntity.badRequest()
                        .body(new PostingResponseDto(null, "Usuario sin tienda asignada"));
            }

            bill.setStore(user.getStore());
            Bill savedBill = billsRepository.save(bill);
            BillDto resBill = new BillDto(savedBill);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new PostingResponseDto(resBill, "Gasto creado"));

        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(new PostingResponseDto(null, "Error: " + e.getMessage()));
        }
    }
}
