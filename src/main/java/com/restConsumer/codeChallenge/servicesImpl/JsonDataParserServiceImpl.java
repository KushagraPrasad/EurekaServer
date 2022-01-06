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
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.restConsumer.codeChallenge.exceptionHandlers.ConnectionToJsonFeedRefusedExcetion;
import com.restConsumer.codeChallenge.model.Posts;
import com.restConsumer.codeChallenge.services.JsonDataParserService;

@Service
public class JsonDataParserServiceImpl implements JsonDataParserService {

	private List<Posts> posts;
	private String res=null;
	//private String responseString=null;

	@Override
	public List<Posts> getPosts(String serviceUrl) throws ClientProtocolException, IOException, ConnectionToJsonFeedRefusedExcetion {
		 HttpUriRequest
		  request = new HttpGet(serviceUrl);
		   
		  try {
			  CloseableHttpResponse response = HttpClientBuilder.create().build().execute(
		  request );
			  HttpEntity entity = response.getEntity();
			  
			  
			  
			  InputStream instream = entity.getContent();
			  
			  byte[] bytes = IOUtils.toByteArray(instream);
			 
			  res = new String(bytes, "UTF-8");
			 
			 // responseString = res.substring(1,res.length()-1);
			  
			  instream.close(); 
			  
			  Gson gson = new Gson();
			  JsonReader reader = new JsonReader(new StringReader(res));
			  reader.setLenient(true);
			  
			  Type postsListType = new TypeToken<ArrayList<Posts>>(){}.getType();

			
			   posts =  gson.fromJson(reader ,postsListType);
					      
			
			  
		  }
		  catch(HttpHostConnectException e) {
			  throw new ConnectionToJsonFeedRefusedExcetion("No response recived from the JSON end point");
		  }
		
		  return posts;
	}

}
