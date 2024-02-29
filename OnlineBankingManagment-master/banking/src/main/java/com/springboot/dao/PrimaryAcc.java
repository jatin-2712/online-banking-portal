package com.springboot.dao;

import org.springframework.data.repository.CrudRepository;

import com.springboot.entity.PrimaryAccount;

public interface PrimaryAcc  extends CrudRepository<PrimaryAccount, Integer> {
public PrimaryAccount findByaccountID(Integer accountID);
}
