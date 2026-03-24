package com.project.eventpass.service;

import com.project.eventpass.entity.Role;
import com.project.eventpass.entity.User;
import com.project.eventpass.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private UserRepository userRepository;
    private BCryptPasswordEncoder encoder;

    @Autowired
    public AuthService(UserRepository userRepository, BCryptPasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.encoder = encoder;
    }

    public User register(User user) {
        user.setPassword(encoder.encode(user.getPassword()));
        if(user.getRole() == null) user.setRole(Role.ROLE_USER);
        return userRepository.save(user);
    }

    public User login(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if(!encoder.matches(password, user.getPassword())) {
            return user;
        }
        throw new RuntimeException("Invalid Credentials");
    }
}
