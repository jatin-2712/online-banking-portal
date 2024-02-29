package com.springboot.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.springboot.entity.UserAccount;

public interface Account extends CrudRepository<UserAccount, Integer> {

	public UserAccount findByUsername(String username);
	public UserAccount findByUsernameAndPassword(String username,String password);

}
