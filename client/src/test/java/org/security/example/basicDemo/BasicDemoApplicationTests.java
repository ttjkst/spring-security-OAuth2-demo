package org.security.example.basicDemo;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BasicDemoApplicationTests {

	private RestTemplate restTemplate = new RestTemplate();
	@Test
	public void contextLoads() {
	}
	
	@Test
	public void loginTest(){
	  HttpHeaders requestHeaders = new HttpHeaders();
	  requestHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
	  MultiValueMap<String, String> body= new LinkedMultiValueMap<>(2);
	  body.add("username", "admin");
	  body.add("password", "admin");
	  HttpEntity<MultiValueMap<String, String>> postData = new HttpEntity<>(body,requestHeaders);
	  ResponseEntity<Map> postForEntity = restTemplate.postForEntity("http://localhost:9092/uc/login/process", postData, Map.class);
	  System.out.println(postForEntity.getBody());
	}
	

}
