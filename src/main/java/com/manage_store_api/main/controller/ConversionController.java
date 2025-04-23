package com.manage_store_api.main.controller;

import com.manage_store_api.main.Dto.ConversionDto;
import com.manage_store_api.main.Dto.PostingResponseDto;
import com.manage_store_api.main.Dto.RestResponseDto;
import com.manage_store_api.main.auth.UserPrincipal;
import com.manage_store_api.main.model.Conversion;
import com.manage_store_api.main.model.User;
import com.manage_store_api.main.repository.ConversionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/conversion")
public class ConversionController {
    private final ConversionRepository conversionRepository;

    @Autowired
    public ConversionController(ConversionRepository conversionRepository) {
        this.conversionRepository = conversionRepository;
    }

    @GetMapping("/listAll")
    public ResponseEntity<RestResponseDto> getConversions() {
        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        User user = userPrincipal.getUser();
        Long storeId = user.getStore().getId();
        List<Conversion> conversions = conversionRepository.findByStoreIdOrderByCreatedAtAsc(storeId);
        RestResponseDto response = new RestResponseDto(conversions, "GET FUNCIONANDO");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/create")
    public ResponseEntity<PostingResponseDto> createConversion(@RequestBody Conversion conversion) {
        try {
            User user = ((UserPrincipal) SecurityContextHolder.getContext()
                    .getAuthentication().getPrincipal()).getUser();

            if (user.getStore() == null) {
                return ResponseEntity.badRequest()
                        .body(new PostingResponseDto(null, "Usuario sin tienda asignada"));
            }

            int deactivatedCount = conversionRepository.deactivateCurrentConversions(user.getStore().getId(), false);

            conversion.setStore(user.getStore());
            conversion.setCurrentDate(true);
            Conversion savedConversion = conversionRepository.save(conversion);

            ConversionDto responseDto = new ConversionDto(savedConversion);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new PostingResponseDto(responseDto, "Conversion creada"));

        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(new PostingResponseDto(null, "Error: " + e.getMessage()));
        }
    }
}
