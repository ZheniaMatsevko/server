package com.example.eventsmanager.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IUserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUsername(String username);

    Optional<UserEntity> findByEmail(String email);

    @Modifying
    @Query("UPDATE UserEntity a SET a.profileImageUrl = :profileImageUrl WHERE a.id = :userId")
    void updateImageUrl(@Param("userId") Long userId, @Param("profileImageUrl") String profileImageUrl);

}
