package com.spring.Demo;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class Main{
	

	@GetMapping("/")
	public String hello() {
		
		return "index";
	}


}
