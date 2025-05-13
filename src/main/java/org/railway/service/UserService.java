package org.railway.service;

import lombok.RequiredArgsConstructor;
import org.railway.entity.User;
import org.railway.service.impl.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String emailOrUsername) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(emailOrUsername)
                .orElseGet(() -> userRepository.findByEmail(emailOrUsername)
                        .orElseThrow(() -> new UsernameNotFoundException("User not found")));
        List<GrantedAuthority> authorities = new ArrayList<>();
        if (user.getUserType() == 1) {
            authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        }
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                "{noop}" + user.getPassword(),
                authorities
        );
    }

}
