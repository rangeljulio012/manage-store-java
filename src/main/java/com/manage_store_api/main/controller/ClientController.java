package com.manage_store_api.main.controller;

import com.manage_store_api.main.Dto.ClientDto;
import com.manage_store_api.main.Dto.PostingResponseDto;
import com.manage_store_api.main.Dto.RestResponseDto;
import com.manage_store_api.main.auth.UserPrincipal;
import com.manage_store_api.main.model.Client;
import com.manage_store_api.main.model.User;
import com.manage_store_api.main.repository.ClientsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clients")
public class ClientController {
    private final ClientsRepository clientsRepository;
    @Autowired
    public ClientController(ClientsRepository clientsRepository) {
        this.clientsRepository = clientsRepository;
    }

    @GetMapping("/listAll")
    public ResponseEntity<RestResponseDto> getClients() {
        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        User user = userPrincipal.getUser();
        Long storeId = user.getStore().getId();
        List<Client> clients = clientsRepository.findByStore_id(storeId);
        RestResponseDto response = new RestResponseDto(clients, "GET FUNCIONANDO");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/create")
    public ResponseEntity<PostingResponseDto> createClient(@RequestBody Client client) {

        try {
            User user = ((UserPrincipal) SecurityContextHolder.getContext()
                    .getAuthentication().getPrincipal()).getUser();

            if (user.getStore() == null) {
                return ResponseEntity.badRequest()
                        .body(new PostingResponseDto(null, "Usuario sin tienda asignada"));
            }

            // Verificar si el client ya existe
            boolean alreadyExistByName = clientsRepository.existsByStore_IdAndName(
                    user.getStore().getId(),
                    client.getName().trim()
            );

            if (alreadyExistByName) {
                return ResponseEntity.badRequest()
                        .body(new PostingResponseDto(null, "Ya existe un cliente con ese nombre en esta tienda"));
            }

            boolean alreadyExistByDocument = clientsRepository.existsByStore_IdAndDocument(
                    user.getStore().getId(),
                    client.getDocument().trim()
            );

            if (alreadyExistByDocument) {
                return ResponseEntity.badRequest()
                        .body(new PostingResponseDto(null, "Ya existe un cliente con ese documento en esta tienda"));
            }

            client.setStore(user.getStore());
            Client savedClient = clientsRepository.save(client);
            ClientDto resClient = new ClientDto(savedClient);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new PostingResponseDto(resClient, "Cliente creado"));

        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(new PostingResponseDto(null, "Error: " + e.getMessage()));
        }
    }
}
