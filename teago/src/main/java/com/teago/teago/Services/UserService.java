package com.teago.teago.Services;

import org.springframework.stereotype.Service;


import com.teago.teago.Repositories.UserRepository;
import com.teago.teago.dto.LoginRequest;
import com.teago.teago.dto.RegisterRequest;
import com.teago.teago.dto.UserDTO;
import com.teago.teago.models.User;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void register(RegisterRequest request) {

        // Check username already exists
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already exists");
        }

        // Check NIC already exists
        if (userRepository.existsByNic(request.getNic())) {
            throw new RuntimeException("NIC already registered");
        }

        // Create new user
        User user = new User();
        user.setName(request.getName());
        user.setNic(request.getNic());
        user.setAddress(request.getAddress());
        user.setContactNumber(request.getContactNumber());
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());
        // Map frontend role values (e.g. "land_owner", "factory_owner", "supervisor")
        String roleStr = request.getRole();
        if (roleStr == null) throw new RuntimeException("Role is required");
        User.Role mappedRole;
        switch (roleStr.toLowerCase()) {
            case "land_owner":
                mappedRole = User.Role.LandOwner;
                break;
            case "factory_owner":
                mappedRole = User.Role.FactoryOwner;
                break;
            case "supervisor":
                mappedRole = User.Role.Supervisor;
                break;
            default:
                // Try direct enum name (e.g. "LandOwner")
                try {
                    mappedRole = User.Role.valueOf(roleStr);
                } catch (Exception ex) {
                    throw new RuntimeException("Invalid role: " + roleStr);
                }
        }
        user.setRole(mappedRole);
    
    userRepository.save(user);

    }

    public UserDTO login(LoginRequest request) {

        // Find user by username
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Check password
        if (!user.getPassword().equals(request.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        // Return response
        return new UserDTO(
                user.getName(),
                user.getNic(),
                user.getAddress(),
                user.getContactNumber(),
                user.getUsername(),
                user.getPassword(),
                user.getRole().toString()
        );
    }
}
