package com.example.SpringBootTemplate.auth.dao;

import com.example.SpringBootTemplate.auth.entity.UserEntity;

import java.util.Optional;

public interface UserDAO {
    public Optional<UserEntity> readUserById(Long id);
    public Optional<UserEntity> readUserByEmail(String email);
    public UserEntity createUser(UserEntity userEntity);
    public void deleteUser(Long id);
    public void deleteAllUser();
    public Boolean existEmail(String email);
    public Boolean existName(String name);
}
