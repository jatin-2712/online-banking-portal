package com.springboot.controller;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.view.RedirectView;

import com.springboot.dao.Account;
import com.springboot.dao.PrimaryAcc;
import com.springboot.dao.UserTranscatactions;
import com.springboot.entity.PrimaryAccount;
import com.springboot.entity.Transactions;
import com.springboot.entity.UserAccount;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/user")
public class homecontroller {
	@Autowired
	Account dao;
	@Autowired
	PrimaryAcc pdao;
	@Autowired
	UserTranscatactions usertansc;

	@GetMapping("/login")
	public String inde() {

		System.out.println();
		return "index";
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String loginSuccess(@ModelAttribute("login") UserAccount login, Model theModel) {

		String user = login.getUsername();
		System.out.println(user);
		UserAccount userlogin = this.dao.findByUsernameAndPassword(login.getUsername(), login.getPassword());

		if (userlogin != null) {

			// UserAccount acc =this.dao.getProduct(user);
			UserAccount acc = this.dao.findByUsername(login.getUsername());

			theModel.addAttribute("username", acc.getUsername());
			theModel.addAttribute("cust_id", acc.getCustomerID());
			PrimaryAccount primay = this.pdao.findByaccountID(acc.getCustomerID());

			theModel.addAttribute("amount", primay.getAccountBalance());
			return "index";

		} else {
			theModel.addAttribute("message", "Wrong customerID or Password");
			return "home";

		}

	}

	@RequestMapping("/index")
	public String ind() {

		return "index";
	}

	@RequestMapping("/deposit/{username}")
	public String deposit(@PathVariable("username") String User, Model model) {

		model.addAttribute("username", User);
		System.out.println("depo");
		System.out.println(User);
		UserAccount acc = this.dao.findByUsername(User);
		long cust_id = acc.getCustomerID();
		model.addAttribute("cust_id", cust_id);
		return "deposit";
	}

	@RequestMapping(value = "/dodeposit/{username}", method = RequestMethod.POST)
	public String dodeposit(@ModelAttribute("amount") String amount, @PathVariable("username") String username,
			Model m) {
		System.out.println("depo");
		System.out.println(amount);
		System.out.println(username);
		UserAccount acc = this.dao.findByUsername(username);
		PrimaryAccount primay = this.pdao.findByaccountID(acc.getCustomerID());
		primay.setAccountBalance(primay.getAccountBalance().add(new BigDecimal(amount)));
		this.pdao.save(primay);
		Date date = new Date();

		Transactions primaryTransaction = new Transactions(date, "Deposited", "Account", "Finished", new Double(amount),
				primay.getAccountBalance(), primay);
		this.usertansc.save(primaryTransaction);

		m.addAttribute("username", username);
		return "deposit";
	}

	@RequestMapping("/withdraw/{username}")
	public String withdraw(@PathVariable("username") String username, Model model) {

		model.addAttribute("username", username);

		UserAccount acc = this.dao.findByUsername(username);
		long cust_id = acc.getCustomerID();
		model.addAttribute("cust_id", cust_id);
		return "withdraw";
	}

	@RequestMapping(value = "/dowithdraw/{username}", method = RequestMethod.POST)
	public String dowithdraw(@ModelAttribute("amount") String amount, @ModelAttribute("username") String username,
			Model m) {
		UserAccount acc = this.dao.findByUsername(username);
		PrimaryAccount primay = this.pdao.findByaccountID(acc.getCustomerID());
		primay.setAccountBalance(primay.getAccountBalance().subtract(new BigDecimal(amount)));
		this.pdao.save(primay);
		m.addAttribute("username", username);
		Date date = new Date();

		Transactions primaryTransaction = new Transactions(date, "Withdrawn", "Account", "Finished", new Double(amount),
				primay.getAccountBalance(), primay);
		this.usertansc.save(primaryTransaction);

		return "withdraw";
	}

	@RequestMapping("/delete/{username}")
	public String delete(@PathVariable("username") String username, HttpServletRequest request) {

		UserAccount acc = this.dao.findByUsername(username);
		PrimaryAccount pa = this.pdao.findByaccountID(acc.getCustomerID());
		this.dao.deleteById(acc.getCustomerID());
		this.pdao.deleteById(pa.getAccountID());
		return "redirect:/";
	}

	@RequestMapping("/logout")
	public RedirectView logout(HttpServletRequest request) {
		request.getSession().invalidate();
		RedirectView rd = new RedirectView();
		rd.setUrl(request.getContextPath() + "/");
		return rd;
	}

	@RequestMapping("/transactions/{username}")
	public String transactions(@PathVariable("username") String username, Model m) {
		UserAccount acc = this.dao.findByUsername(username);
		PrimaryAccount pm = this.pdao.findByaccountID(acc.getCustomerID());

		List<Transactions> transactions = pm.getPrimaryTransactionList();
		System.out.println(transactions);
		m.addAttribute("transactions", transactions);
		m.addAttribute("username", username);
		m.addAttribute("amount", pm.getAccountBalance());
		return "transactions";
	}

	@RequestMapping("/search/{username}")
	public String search(@ModelAttribute("gsearch") String name, @PathVariable("username") String username, Model m) {// System.out.println(name);
		System.out.println(username);
		UserAccount user = this.dao.findByUsername(username);
		PrimaryAccount pm = this.pdao.findByaccountID(user.getCustomerID());
		List<Transactions> transactions = this.usertansc.findByDescriptionContainingAndPrimaryAccount(name, pm);

		m.addAttribute("transactions", transactions);
		return "transactions";
	}
}
