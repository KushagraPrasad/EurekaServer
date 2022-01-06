package com.restConsumer.codeChallenge.controllers;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;

import org.apache.http.client.ClientProtocolException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.restConsumer.codeChallenge.exceptionHandlers.ConnectionToJsonFeedRefusedExcetion;
import com.restConsumer.codeChallenge.model.Posts;
import com.restConsumer.codeChallenge.services.ConsumerService;

@RestController
@RequestMapping("/")
public class ConsumerEndPointController {

	
	@Autowired
	ConsumerService consumerService;
	
	@Value("${service.url}")
	private String serviceUrl;
	
	
	public static <T> Predicate<T> distinctByKey(
		    Function<? super T, ?> keyExtractor) {
		  
		    Map<Object, Boolean> seen = new ConcurrentHashMap<>(); 
		    return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null; 
		}
	
	@GetMapping("/countUniqueUserIds")
	public Long countUniqueUserIds() throws ClientProtocolException, IOException, ConnectionToJsonFeedRefusedExcetion {
		
		
		return  consumerService.getDataFromEndPoint(serviceUrl).stream().filter(distinctByKey(p->p.getUserId())).count();
	}

	@GetMapping("/getProcessedData")
	public List<Posts> manipulateAndSendBack() throws ClientProtocolException, IOException, ConnectionToJsonFeedRefusedExcetion {

		return  consumerService.getProcessedData(serviceUrl);
	}

	
}
