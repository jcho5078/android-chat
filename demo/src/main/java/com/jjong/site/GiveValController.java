package com.jjong.site;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController //rest컨트롤러 사용하면 값 반출.
public class GiveValController {
	private String name=""; 
	private int age=0;
	
	public String getName() { return name; } 
	public void setName(String name) { this.name = name; }
	public int getAge() { return age; } public void setAge(int age) { this.age = age; }
	
	@RequestMapping(value = "/data", method = RequestMethod.POST)
	public String Send() {
		String message = "{'age':"+age+", 'name':'"+name+"'}";//보내는 json 형식 제대로 안되면 어플에서 오류남
		//json의 각 속성이 안드로이드의 gson객체에 의해 읽어진다음 유저가 해당 속성에 값 대입.
		//
		System.out.println(message);
		return message;
	}
	
}