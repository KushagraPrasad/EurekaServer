package com.restConsumer.codeChallenge.services;

import java.io.IOException;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.springframework.stereotype.Service;

import com.restConsumer.codeChallenge.exceptionHandlers.ConnectionToJsonFeedRefusedExcetion;
import com.restConsumer.codeChallenge.model.Posts;

@Service
public interface JsonDataParserService {

	List<Posts> getPosts(String serviceUrl) throws ClientProtocolException, IOException, ConnectionToJsonFeedRefusedExcetion;

}
