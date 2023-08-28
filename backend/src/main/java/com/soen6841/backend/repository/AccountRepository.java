package com.soen6841.backend.repository;

import com.soen6841.backend.constant.Role;
import com.soen6841.backend.entity.Account;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository extends MongoRepository<Account, String> {
    @Query("{email:'?0'}")
    Account findByEmail(String email);

    @Query("{registrationNumber: '?0'}")
    Account findByRegistrationNumber(String registrationNumber);

    @Query(value = "{email:'?0'}", delete = true)
    Account deleteByEmail(String email);

    @Query(value = "{}")
    List<Account> findAllAccounts();

    @Query("{role:'?0'}")
    List<Account> findAccountByRole(Role role);
}
