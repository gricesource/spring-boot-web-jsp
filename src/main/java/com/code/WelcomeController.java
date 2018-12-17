package com.code;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class WelcomeController {

	@Value("${welcome.message:test}")
	private String message = "";

	@RequestMapping("/")
	public String welcome(Map<String, Object> model) {
		model.put("message", this.message);
		return "welcome";
	}
	
	@RequestMapping(value = "/getFollowers", method = RequestMethod.POST)
	public String saveEnroll(HttpServletRequest request, Map<String, Object> model) {
		int id = Integer.parseInt(request.getParameter("id"));
		
		FileProcessing fp = new FileProcessing();
		String response = fp.createOutput(String.valueOf(id));
		
		model.put("message", response);
		return "results";
	}
}