package com.UCLL.TODO.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public record UserDetailsImpl(User user) implements UserDetails {

    @Override
    public String getPassword() {
        return user.getHashedPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        final List<String> roles = switch (user.getRole()) {
            case USER -> List.of("ROLE_USER");
            case ADMIN -> List.of("ROLE_USER", "ROLE_ADMIN");
        };
        return roles.stream().map(role -> new SimpleGrantedAuthority(role)).toList();
    }
}
