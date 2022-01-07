package com.restConsumer.codeChallenge;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.restConsumer.codeChallenge.controllers.ConsumerEndPointController;
import com.restConsumer.codeChallenge.exceptionHandlers.ConnectionToJsonFeedRefusedException;
import com.restConsumer.codeChallenge.exceptionHandlers.IndexNotFoundException;
import com.restConsumer.codeChallenge.model.Posts;
import com.restConsumer.codeChallenge.services.ConsumerService;
import com.restConsumer.codeChallenge.services.JsonDataParserService;

@SpringBootTest
class CodeChallengeApplicationTests {

	@Test
	void contextLoads() {
	}

	String serviceUrl="http://jsonplaceholder.typicode.com/posts";
	URI uri =URI.create("http://jsonplaceholder.typicode.com/posts");
	
	 @Test
	  public void responseTypeIsOk() throws IOException,
	  InterruptedException{

	  HttpUriRequest request = new HttpGet(uri);
	  
	  CloseableHttpResponse response =  HttpClientBuilder.create().build().execute(request);
	 
	  assertEquals(response.getStatusLine().getStatusCode(),200);
	  }
	  
	  @Test
	  public void 
	  defaultResponseContentTypeIsJson()
	    throws ClientProtocolException, IOException {	   
	     
	     String jsonMimeType = "application/json";
	     HttpUriRequest request = new HttpGet( uri );

	    
	     CloseableHttpResponse response = HttpClientBuilder.create().build().execute( request );

	     HttpEntity entity = response.getEntity();
		  
		 String res = null;
		 
		 InputStream instream = entity.getContent();

		 byte[] bytes =  IOUtils.toByteArray(instream);

		 res = new String(bytes, "UTF-8");

		  instream.close();
		  
		  assertThat(entity!=null);
		  assertNotNull(res);

	     String mimeType = ContentType.getOrDefault(response.getEntity()).getMimeType();
	     
	     assertEquals( jsonMimeType, mimeType );
	  }
	
	  
	   
	  @MockBean 
	  JsonDataParserService jsonDataParserService;
	  
	  @Autowired
	  ConsumerService consumerService;
		
		@Test
		public void consumerService_ensureGetDataFromEndPointReturnsListOfPosts() throws ClientProtocolException, IOException, ConnectionToJsonFeedRefusedException {
			when(jsonDataParserService.getPosts(serviceUrl)).thenReturn(Stream.of(new Posts(1l, 1l, "title", "body"),
					new Posts(2l, 2l, "title2", "body2")).collect(Collectors.toList()));
			
			assertEquals(2,consumerService.getDataFromEndPoint(serviceUrl).size());
			assertEquals("title",consumerService.getDataFromEndPoint(serviceUrl).get(0).getTitle());
			assertNotNull(consumerService.getDataFromEndPoint(serviceUrl));
		}
	  
		@Test
		public void customerService_ensuregetProcessedDataRetrunsUpdatedJson() throws ClientProtocolException, IOException, ConnectionToJsonFeedRefusedException {
			when(jsonDataParserService.getPosts(serviceUrl)).thenReturn(Stream.of(new Posts(1l, 1l, "title", "body"),
					new Posts(2l, 2l, "title2", "body2"),
					new Posts(3l, 3l, "title2", "body2"),
					new Posts(4l, 4l, "title3", "body3")).collect(Collectors.toList()));
			
			
			assertEquals("1800Flowers",consumerService.getProcessedData(serviceUrl).get(3).getTitle());
			assertEquals("1800Flowers",consumerService.getProcessedData(serviceUrl).get(3).getBody());
			assertNotNull(consumerService.getProcessedData(serviceUrl));
		}
		
		@Test
		public void customerService_ensureupdateTheNthElementSendsbackUpdatedData() throws ClientProtocolException, IOException, ConnectionToJsonFeedRefusedException, IndexNotFoundException {
			when(jsonDataParserService.getPosts(serviceUrl)).thenReturn(Stream.of(new Posts(1l, 1l, "title", "body"),
					new Posts(2l, 2l, "title2", "body2"),
					new Posts(3l, 3l, "title2", "body2"),
					new Posts(4l, 4l, "title3", "body3")).collect(Collectors.toList()));
			
			
			assertEquals("1800Flowers",consumerService.getProcessedDataWithUpdatedNode( 3,  serviceUrl).get(3).getTitle());
			assertEquals("1800Flowers",consumerService.getProcessedDataWithUpdatedNode( 3,  serviceUrl) .get(3).getBody());
			assertNotNull(consumerService.getProcessedDataWithUpdatedNode(3,serviceUrl));
		}
		
		@Autowired
		ConsumerEndPointController consumerEndPointController;
		
		@Test
		public void consumerEndPointController_ensureCountUniqueUserIdsReturnUnlyUniqueUSerIDsCount() throws ClientProtocolException, IOException, ConnectionToJsonFeedRefusedException {
			when(jsonDataParserService.getPosts(serviceUrl)).thenReturn(Stream.of(new Posts(1l, 1l, "title", "body"),
					new Posts(1l, 2l, "title2", "body2"),
					new Posts(3l, 3l, "title2", "body2"),
					new Posts(4l, 4l, "title3", "body3")).collect(Collectors.toList()));
			
			assertNotNull(consumerEndPointController.countUniqueUserIds());
			assertEquals(3l,consumerEndPointController.countUniqueUserIds());
			
		}
	  
}
