package com.UCLL.TODO.service;

import com.UCLL.TODO.model.UserDetailsImpl;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public interface JwtService {
    String generateToken(long id, String email, Collection<String> roles);
    String generateToken(UserDetailsImpl userDetails);
}
