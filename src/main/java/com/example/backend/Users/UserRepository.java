package com.example.backend.Users;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<UserEntity , String> {


    UserEntity findByUserAddress(String userAddress);

    boolean existsByUserAddress(String userAddress);
}
