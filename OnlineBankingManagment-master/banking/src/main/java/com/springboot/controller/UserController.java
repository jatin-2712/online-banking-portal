package com.springboot.controller;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.springboot.dao.Account;
import com.springboot.dao.PrimaryAcc;
import com.springboot.dao.UserTranscatactions;
import com.springboot.entity.PrimaryAccount;
import com.springboot.entity.Transactions;
import com.springboot.entity.UserAccount;

@Controller
public class UserController {
	int nextAccountNumber = 11223145;
	@Autowired
	Account dao;
	@Autowired
	PrimaryAcc pdao;
	@Autowired
	UserTranscatactions usertansc;

	@RequestMapping("/")
	public String home() {
		return "redirect:/home";
	}

	@RequestMapping("/home")
	public String index() {
		return "home";
	}

	@RequestMapping("/register")
	public String register() {
		return "register";
	}

	@PostMapping("/registerpost")
	public String registeruser(@ModelAttribute("registerpost") UserAccount account, Model m) {

		System.out.println("hello");
		String user = account.getUsername();
		System.out.println(user);
		UserAccount list = this.dao.findByUsername(account.getUsername());

		if (list != null) {
			m.addAttribute("message", "User already exists");
			return "register";
		} else {
			UserAccount ua = this.dao.save(account);
			PrimaryAccount pm = new PrimaryAccount();
			pm.setAccountID(ua.getCustomerID());
			pm.setAccountBalance(new BigDecimal(0));
			pm.setAccountNumber(nextAccountNumber++);
			this.pdao.save(pm);

			Date date = new Date();

			Transactions primaryTransaction = new Transactions(date, "Deposited", "Account", "Finished", 0.0,
					pm.getAccountBalance(), pm);
			this.usertansc.save(primaryTransaction);
			return "home";

		}

	}

}
