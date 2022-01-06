package com.restConsumer.codeChallenge.servicesImpl;

import java.io.IOException;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.restConsumer.codeChallenge.exceptionHandlers.ConnectionToJsonFeedRefusedExcetion;
import com.restConsumer.codeChallenge.model.Posts;
import com.restConsumer.codeChallenge.services.ConsumerService;
import com.restConsumer.codeChallenge.services.JsonDataParserService;

@Service
public class ConsumerServiceImpl implements ConsumerService {
	
	
	@Autowired
	JsonDataParserService JsonDataParserService;
	
	
	@Override
	public List<Posts> getDataFromEndPoint(String serviceUrl) throws ClientProtocolException, IOException, ConnectionToJsonFeedRefusedExcetion {

		
		 
		return JsonDataParserService.getPosts(serviceUrl);
		  
		
	}
	
	@Value("${constant.string}")
	private String stringConstant;
	

	@Override
	public List<Posts> getProcessedData(String serviceUrl) 
			throws ClientProtocolException, IOException, ConnectionToJsonFeedRefusedExcetion {
		List<Posts> posts = JsonDataParserService.getPosts(serviceUrl);
		
			Posts post = posts.get(3);
			post.setTitle(stringConstant);
			post.setBody(stringConstant);
			posts.set(3, post);
			return posts;
		
			
		
	}

}
