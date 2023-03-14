package com.sgu.userservice.repository;

import com.sgu.userservice.model.Account;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface AccountRepository extends MongoRepository<Account, ObjectId> {
    public Optional<Account> findByPersonId(Long personId);

    public Optional<Account> findByUsername(String username);
}
