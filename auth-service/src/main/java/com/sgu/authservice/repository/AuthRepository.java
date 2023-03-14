package com.sgu.authservice.repository;

import com.sgu.authservice.model.Account;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.MongoId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

public interface AuthRepository extends MongoRepository<Account, ObjectId> {
    public Optional<Account> findByUsername(String username);

}
