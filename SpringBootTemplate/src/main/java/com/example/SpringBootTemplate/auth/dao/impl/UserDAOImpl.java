package com.example.SpringBootTemplate.auth.dao.impl;

import com.example.SpringBootTemplate.auth.dao.UserDAO;
import com.example.SpringBootTemplate.auth.entity.UserEntity;
import com.example.SpringBootTemplate.auth.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Repository
public class UserDAOImpl implements UserDAO {

    private static final Logger logger = LoggerFactory.getLogger(UserDAOImpl.class);
    private final UserRepository userRepository;

    public UserDAOImpl(
            @Autowired UserRepository userRepository
    ){
        this.userRepository = userRepository;
    }


    @Override
    @Transactional
    public Optional<UserEntity> readUserById(Long id) {
        return this.userRepository.findById(id);
    }

    @Override
    @Transactional
    public Optional<UserEntity> readUserByEmail(String email) {
        return this.userRepository.findByEmail(email);
    }

    @Override
    @Transactional
    public UserEntity createUser(UserEntity userEntity) {
        return this.userRepository.save(userEntity);
    }

    @Override
    public void deleteUser(Long id) {
        this.userRepository.deleteById(id);
    }

    @Override
    public void deleteAllUser() {
        this.userRepository.deleteAll();
    }

    @Override
    public Boolean existEmail(String email) {
        return this.userRepository.existsByEmail(email);
    }

    @Override
    public Boolean existName(String name) {
        return this.userRepository.existsByName(name);
    }

}
