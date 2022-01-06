package com.restConsumer.codeChallenge.services;

import java.io.IOException;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.springframework.stereotype.Service;

import com.restConsumer.codeChallenge.exceptionHandlers.ConnectionToJsonFeedRefusedExcetion;
import com.restConsumer.codeChallenge.model.Posts;

@Service
public interface ConsumerService {

	List<Posts> getDataFromEndPoint(String serviceUrl) throws ClientProtocolException, IOException, ConnectionToJsonFeedRefusedExcetion;

	

	List<Posts> getProcessedData(String serviceUrl)
			throws ClientProtocolException, IOException, ConnectionToJsonFeedRefusedExcetion;

	

	
	
	
}
