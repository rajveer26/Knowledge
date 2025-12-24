package com.knowledge.microservice.auth;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final Map<String, UserDetails> inMemoryUsers = new HashMap<>();
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public CustomUserDetailsService() {
        // Initialize in-memory users
        inMemoryUsers.put("admin", new CustomUserDetails(
                "admin",
                passwordEncoder.encode("admin123"),
                Collections.singletonList(new SimpleGrantedAuthority("ADMIN"))
        ));
        inMemoryUsers.put("user", new CustomUserDetails(
                "user",
                passwordEncoder.encode("user123"),
                Collections.singletonList(new SimpleGrantedAuthority("USER"))
        ));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDetails user = inMemoryUsers.get(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        return user;
    }
}
