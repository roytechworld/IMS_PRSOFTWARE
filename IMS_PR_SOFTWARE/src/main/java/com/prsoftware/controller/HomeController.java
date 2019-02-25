package com.prsoftware.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Pradipto roy
 *
 */

@Controller
public class HomeController {
	
	@RequestMapping("/")
	public String run()
	{
		
		return "index.jsp";
	}
}
