package com.teago.teago.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.teago.teago.Repositories.UserRepository;
import com.teago.teago.Services.UserService;
import com.teago.teago.dto.LoginRequest;
import com.teago.teago.dto.RegisterRequest;
import com.teago.teago.dto.UserDTO;
import com.teago.teago.models.User;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final UserRepository userRepository;

    public AuthController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        try {
            userService.register(request);
            return ResponseEntity.ok(Map.of("message", "registered"));
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(Map.of("message", ex.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            UserDTO dto = userService.login(request);

            User user = userRepository.findByUsername(request.getUsername()).orElse(null);

            String roleOut = "";
            Integer userId = null;
            if (user != null) {
                userId = user.getUserId();
                if (user.getRole() != null) {
                    switch (user.getRole()) {
                        case LandOwner:
                            roleOut = "LAND_OWNER";
                            break;
                        case FactoryOwner:
                            roleOut = "FACTORY_OWNER";
                            break;
                        case Supervisor:
                            roleOut = "SUPERVISOR";
                            break;
                    }
                }
            }

            Map<String, Object> resp = new HashMap<>();
            resp.put("token", "");
            resp.put("role", roleOut);
            resp.put("userId", userId);
            resp.put("fullName", dto.getName());
            resp.put("username", dto.getUsername());

            return ResponseEntity.ok(resp);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", ex.getMessage()));
        }
    }
}

