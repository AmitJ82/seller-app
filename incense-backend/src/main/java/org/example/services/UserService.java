package org.example.services;

import org.example.entity.User;
import org.example.dto.RegistrationRequest;
import org.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User registerUser(RegistrationRequest request) {
        if (userRepository.findByUsername(request.getUsername()) != null) {
            throw new IllegalArgumentException("Username already exists");
        }
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(new BCryptPasswordEncoder().encode(request.getPassword()));
        user.setRole("USER"); // default role
        return userRepository.save(user);
    }

}