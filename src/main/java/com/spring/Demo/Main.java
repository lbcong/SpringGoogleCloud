package com.spring.Demo;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class DemoApplication {
	

	@GetMapping("/")
	public String hello() {
		
		return "index";
	}


}
