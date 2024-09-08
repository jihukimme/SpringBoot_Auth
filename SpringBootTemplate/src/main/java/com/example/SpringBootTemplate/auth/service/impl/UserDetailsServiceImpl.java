package com.example.SpringBootTemplate.auth.service.impl;

import com.example.SpringBootTemplate.auth.dto.UserDTO;
import com.example.SpringBootTemplate.auth.entity.UserEntity;
import com.example.SpringBootTemplate.auth.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final static Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);
    private final UserRepository userRepository;

    public UserDetailsServiceImpl(
            @Autowired UserRepository userRepository
    ) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        Optional<UserEntity> optionalUserEntity = this.userRepository.findById(Long.parseLong(id));

        UserEntity userEntity = optionalUserEntity.orElseThrow(() ->
                new UsernameNotFoundException("User not found with id: " + id)
        );

        return (UserDetails) userEntity;
    }
}


