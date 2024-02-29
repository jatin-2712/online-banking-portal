package com.springboot.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.springboot.entity.PrimaryAccount;
import com.springboot.entity.Transactions;
import com.springboot.entity.UserAccount;

public interface UserTranscatactions  extends CrudRepository<Transactions, Integer> {
public List<Transactions> findByDescriptionContainingAndPrimaryAccount(String description,PrimaryAccount primaryAccount);
}
