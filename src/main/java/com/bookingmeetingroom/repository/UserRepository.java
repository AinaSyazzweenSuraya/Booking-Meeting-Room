package com.bookingmeetingroom.repository;

import com.bookingmeetingroom.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    List<UserEntity> findAll();

    //find by username
    Optional<UserEntity> findByUsername(String username);


    boolean existsByUsername(String username);
    void deleteByUsername(String username);
    boolean existsById(Long id);
    void deleteById(Long id);
}
