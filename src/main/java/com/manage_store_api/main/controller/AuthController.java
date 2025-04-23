package com.manage_store_api.main.controller;

import com.manage_store_api.main.Utils.JwtService;
import com.manage_store_api.main.repository.StoreRepository;
import com.manage_store_api.main.repository.UserRepository;
import com.manage_store_api.main.Dto.RestResponseDto;
import com.manage_store_api.main.Dto.TokenResponseDto;
import com.manage_store_api.main.model.Store;
import com.manage_store_api.main.model.User;
import io.jsonwebtoken.Claims;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private static final Logger log = LoggerFactory.getLogger(AuthController.class);
    private final UserRepository userRepository;
    private final StoreRepository storeRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthController(UserRepository userRepository,
                          StoreRepository storeRepository,
                          JwtService jwtService,
                          PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.storeRepository = storeRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @Valid @RequestBody LoginRequest request,
            BindingResult bindingResult
    ) {
        log.info("Validando login - Errores: {}", bindingResult.hasErrors());

        if (bindingResult.hasErrors()) {
            bindingResult.getAllErrors().forEach(error -> {
                log.error("Error de validación: {}", error.getDefaultMessage());
            });

            String errorMessage = bindingResult.getFieldErrors().stream()
                    .findFirst()
                    .map(FieldError::getDefaultMessage)
                    .orElse("Error de validación desconocido");

            return ResponseEntity.badRequest().body(new ErrorResponse(errorMessage));
        }

        try {
            User user = userRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new AuthException("Credenciales inválidas"));

            if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                throw new AuthException("Credenciales inválidas");
            }

            String token = jwtService.generateToken(user, false);
            String refreshToken = jwtService.generateToken(user, true);

            return ResponseEntity.ok(new TokenResponseDto(token, refreshToken));
        } catch (AuthException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(
            @Valid @RequestBody SignupRequest request,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getFieldError().getDefaultMessage();
            return ResponseEntity.badRequest().body(new ErrorResponse(errorMessage));
        }

        try {
            if (userRepository.existsByEmail(request.getEmail())) {
                throw new AuthException("El usuario ya existe");
            }

            Store store = storeRepository.findById(request.getStoreId())
                    .orElseThrow(() -> new AuthException("La tienda no existe"));

            User newUser = new User();
            newUser.setEmail(request.getEmail());
            newUser.setPassword(passwordEncoder.encode(request.getPassword()));
            newUser.setName(request.getName());
            newUser.setRole(request.getRole());
            newUser.setStore(store);
            newUser.setAdmin(true);

            userRepository.save(newUser);

            String token = jwtService.generateToken(newUser, false);
            String refreshToken = jwtService.generateToken(newUser, true);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new TokenResponseDto(token, refreshToken));
        } catch (AuthException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }

    @PostMapping("/refreshToken")
    public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenRequest request) {
        try {
            if (!jwtService.isTokenValid(request.getRefreshToken())) {
                throw new AuthException("Token inválido o expirado");
            }

            if (!"REFRESH".equals(jwtService.getTokenType(request.getRefreshToken()))) {
                throw new AuthException("Token inválido");
            }

            Claims claims = jwtService.extractAllClaims(request.getRefreshToken());
            Long userId = claims.get("id", Long.class);

            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new AuthException("Usuario no encontrado"));

            String newAccessToken = jwtService.generateToken(user, false);
            String newRefreshToken = jwtService.generateToken(user, true);

            return ResponseEntity.ok(new TokenResponseDto(newAccessToken, newRefreshToken));
        } catch (AuthException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(e.getMessage()));
        }
    }

    @GetMapping("/listAll")
    public ResponseEntity<RestResponseDto> getStores() {
        List<User> users = userRepository.findAll();
        RestResponseDto response = new RestResponseDto(users, "GET FUNCIONANDO");
        return ResponseEntity.ok(response);
    }

    // Clases internas para requests (se mantienen igual)
    public static class LoginRequest {
        @NotBlank(message = "El Email es requerido")
        @Valid @Email(message = "El email debe respetar el formato de correo electronico")
        private String email;

        @NotBlank(message = "La contraseña es requerida")
        @Valid @NotNull(message = "La contraseña es requerida")
        private String password;

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }

    public static class SignupRequest {
        @NotBlank(message = "El Email es requerid")
        @Email(message = "El email debe respetar el formato de correo electronico")
        private String email;

        @NotBlank(message = "La contraseña es requerida")
        @NotNull(message = "La contraseña es requerida")
        private String password;

        @NotBlank(message = "Nombre es requerid")
        @NotNull(message = "Nombre es requerido")
        private String name;

        @NotBlank(message = "Rol es requerid")
        @NotNull(message = "Rol es requerido")
        private String role;

        @NotBlank(message = "Store es requerid")
        @NotNull(message = "Store es requerido")
        private Long storeId;

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getRole() { return role; }
        public void setRole(String role) { this.role = role; }
        public Long getStoreId() { return storeId; }
        public void setStoreId(Long storeId) { this.storeId = storeId; }
    }

    public static class RefreshTokenRequest {
        private String refreshToken;

        public String getRefreshToken() { return refreshToken; }
        public void setRefreshToken(String refreshToken) { this.refreshToken = refreshToken; }
    }

    // Clase para respuestas de error
    public static class ErrorResponse {
        private String message;

        public ErrorResponse(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public static class AuthException extends RuntimeException {
        public AuthException(String message) {
            super(message);
        }
    }
}