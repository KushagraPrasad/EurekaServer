package com.restConsumer.codeChallenge.servicesImpl;

import java.io.IOException;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.restConsumer.codeChallenge.exceptionHandlers.ConnectionToJsonFeedRefusedException;
import com.restConsumer.codeChallenge.model.Posts;
import com.restConsumer.codeChallenge.services.ConsumerService;
import com.restConsumer.codeChallenge.services.JsonDataParserService;

@Service
public class ConsumerServiceImpl implements ConsumerService {
	private static final Logger LOGGER=LoggerFactory.getLogger(ConsumerServiceImpl.class);
	
	@Autowired
	JsonDataParserService JsonDataParserService;
	
	
	@Override
	public List<Posts> getDataFromEndPoint(String serviceUrl) throws ClientProtocolException, IOException, ConnectionToJsonFeedRefusedException {

		
		LOGGER.info("Getting data from the feed. ");
		return JsonDataParserService.getPosts(serviceUrl);
		  
		
	}
	
	@Value("${constant.string}")
	private String stringConstant;
	

	@Override
	public List<Posts> getProcessedData(String serviceUrl) 
			throws ClientProtocolException, IOException, ConnectionToJsonFeedRefusedException {
		LOGGER.info("Getting data from the feed.");
		List<Posts> posts = JsonDataParserService.getPosts(serviceUrl);
		LOGGER.info("Data was retrieved form the feed.");
		LOGGER.info("Performing data updation on the feed.");
			Posts post = posts.get(3);
			post.setTitle(stringConstant);
			post.setBody(stringConstant);
			posts.set(3, post);
		LOGGER.info("Data updation sucessfull.");
			return posts;
		
			
		
	}


	@Override
	public List<Posts> getProcessedDataWithUpdatedNode(int index, String serviceUrl) throws ClientProtocolException, IOException, ConnectionToJsonFeedRefusedException {
		LOGGER.info("Getting data from the feed.");
		List<Posts> posts = JsonDataParserService.getPosts(serviceUrl);
		LOGGER.info("Data was retrieved form the feed.");
		LOGGER.info("Performing data updation on the feed.");
		Posts post = posts.get(index);
		post.setTitle(stringConstant);
		post.setBody(stringConstant);
		posts.set(index, post);
		LOGGER.info("Data updation sucessfull.");
		return posts;
	

}
}
