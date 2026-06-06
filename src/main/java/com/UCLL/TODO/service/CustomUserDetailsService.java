package com.UCLL.TODO.service;

import com.UCLL.TODO.model.UserDetailsImpl;
import com.UCLL.TODO.repository.jpa.UserJpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserJpaRepository userJpaRepository;

    public CustomUserDetailsService(UserJpaRepository userJpaRepository) {
        this.userJpaRepository = userJpaRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        final var user = userJpaRepository.findUserByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found: " + username));
        return new UserDetailsImpl(user);
    }
}
