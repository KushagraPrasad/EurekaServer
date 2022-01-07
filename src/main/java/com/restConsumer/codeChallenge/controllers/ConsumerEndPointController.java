package com.restConsumer.codeChallenge.controllers;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;

import org.apache.http.client.ClientProtocolException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.restConsumer.codeChallenge.exceptionHandlers.ConnectionToJsonFeedRefusedException;
import com.restConsumer.codeChallenge.exceptionHandlers.IndexNotFoundException;
import com.restConsumer.codeChallenge.model.Posts;
import com.restConsumer.codeChallenge.services.ConsumerService;

@RestController
@RequestMapping("/")
public class ConsumerEndPointController {

	private static final Logger LOGGER=LoggerFactory.getLogger(ConsumerEndPointController.class);
	 
	
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
	public Long countUniqueUserIds() throws ClientProtocolException, IOException, ConnectionToJsonFeedRefusedException {
		
		LOGGER.info("Counting unique user ids in feed.");
		return  consumerService.getDataFromEndPoint(serviceUrl).stream().filter(distinctByKey(p->p.getUserId())).count();
	}
	
	@PutMapping("/updateNthElement/{index}")
	public List<Posts> updateTheNthElement(@PathVariable Integer index) throws ClientProtocolException, 
	IOException, ConnectionToJsonFeedRefusedException,  IndexNotFoundException{
		if(index!=0 && index!=null ) {
			LOGGER.info("Updating the Nth element on the feed.");
		return consumerService.getProcessedDataWithUpdatedNode(index-1,serviceUrl);
		}
		else {
			LOGGER.info("Exception occured, IndexNotFoundException will be thrown.");
			throw new IndexNotFoundException("Index cannot be null or 0."); 
		}
	}
	
	@PostMapping("/getProcessedData")
	public List<Posts> manipulateAndSendBack() throws ClientProtocolException, IOException, ConnectionToJsonFeedRefusedException {
		LOGGER.info("Getting processed data.");
		return  consumerService.getProcessedData(serviceUrl);
	}

	
}
