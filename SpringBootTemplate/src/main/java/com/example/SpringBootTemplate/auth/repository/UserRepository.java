package com.example.SpringBootTemplate.auth.repository;

import com.example.SpringBootTemplate.auth.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    public Optional<UserEntity> findById(Long id); // optional 사용을 고려해보자.
    public Optional<UserEntity> findByEmail(String email); // optional 사용을 고려해보자.
    public void deleteById(Long id);
    public boolean existsByEmail(String email);
    public boolean existsByName(String name);

}
