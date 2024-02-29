package com.springboot.entity;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class PrimaryAccount {
	@Id
	//@GeneratedValue(strategy = GenerationType.AUTO)
	//@JoinColumns(name="customerID")
	private Integer accountID;
	private int accountNumber;
	private BigDecimal accountBalance;
	@OneToMany(mappedBy = "primaryAccount", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JsonIgnore
	private List<Transactions> primaryTransactionList;
	
	public PrimaryAccount() {
		super();
		// TODO Auto-generated constructor stub
	}

	public PrimaryAccount(Integer accountID, int accountNumber, BigDecimal accountBalance) {
		super();
		this.accountID = accountID;
		this.accountNumber = accountNumber;
		this.accountBalance = accountBalance;
	}

	public List<Transactions> getPrimaryTransactionList() {
		return primaryTransactionList;
	}

	public void setPrimaryTransactionList(List<Transactions> primaryTransactionList) {
		this.primaryTransactionList = primaryTransactionList;
	}

	public Integer getAccountID() {
		return accountID;
	}

	public void setAccountID(Integer accountID) {
		this.accountID = accountID;
	}

	public int getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(int accountNumber) {
		this.accountNumber = accountNumber;
	}

	public BigDecimal getAccountBalance() {
		return accountBalance;
	}

	public void setAccountBalance(BigDecimal accountBalance) {
		this.accountBalance = accountBalance;
	}


	@Override
	public String toString() {
		return "PrimaryAccount [accountID=" + accountID + ", accountNumber=" + accountNumber + ", accountBalance="
				+ accountBalance + "]";
	}
}
