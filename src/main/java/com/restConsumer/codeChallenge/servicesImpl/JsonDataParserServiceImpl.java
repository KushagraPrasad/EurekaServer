package com.restConsumer.codeChallenge.servicesImpl;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.restConsumer.codeChallenge.exceptionHandlers.ConnectionToJsonFeedRefusedException;
import com.restConsumer.codeChallenge.model.Posts;
import com.restConsumer.codeChallenge.services.JsonDataParserService;

@Service
public class JsonDataParserServiceImpl implements JsonDataParserService {

	private List<Posts> posts;
	private String res=null;
	private static final Logger LOGGER=LoggerFactory.getLogger(JsonDataParserServiceImpl.class);
	 
	//private String responseString=null;

	@Override
	public List<Posts> getPosts(String serviceUrl) 
			throws ClientProtocolException, IOException, ConnectionToJsonFeedRefusedException {
		LOGGER.info("Starting to parse the JSON feed");
		HttpUriRequest
		  request = new HttpGet(serviceUrl);
		   
		  try {
			  CloseableHttpResponse response = HttpClientBuilder.create().build().execute(
		  request );
			  HttpEntity entity = response.getEntity();
			  
			  
			  
			  InputStream instream = entity.getContent();
			  
			  byte[] bytes = IOUtils.toByteArray(instream);
			 
			  res = new String(bytes, "UTF-8");
			 
			 
			  
			  instream.close(); 
			  
			  Gson gson = new Gson();
			  JsonReader reader = new JsonReader(new StringReader(res));
			  reader.setLenient(true);
			  
			  Type postsListType = new TypeToken<ArrayList<Posts>>(){}.getType();

			
			   posts =  gson.fromJson(reader ,postsListType);
					      
			   LOGGER.info("JSON feed parsing was successfull.");
			  
		  }
		  catch(HttpHostConnectException e) {
			  LOGGER.info("JSON feed parsing was failed. No response recived from the JSON feed endpoint: "+e.toString());
			  throw new ConnectionToJsonFeedRefusedException("No response recived from the JSON end point "+e.toString());
		  }
		  catch(JsonSyntaxException  e){
			  LOGGER.info("Exception occured: JSON feed parsing was failed "+e.toString());
			  throw new JsonSyntaxException("The JSON feed is not in proper format: "+e.toString());
	  }catch(Exception e){
			  LOGGER.info("Exception occured: "+e.toString());
		  }
		
		  return posts;
	}

}
