package com.github.technosf.connectomatic;

import static org.testng.Assert.assertEquals;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;

public class ConnectOMaticTest 
{
    static MockWebServer mockServer;
    static int mockServerPort = 8888;

    @BeforeClass
    public void setup() throws IOException 
    {
        mockServer = new MockWebServer();
        mockServer.start(mockServerPort);
    }

    @AfterClass
    public void teardown() throws IOException 
    {
        mockServer.shutdown();
    }

    @Test
    void testPostToUrl() 
        throws IOException, InterruptedException, URISyntaxException 
    {
        String data = "[{\"IPv\":\"4\",\"Interface\":\"192.168.222.180\",\"Remote Address\":\"140.82.112.4\",\"Remote Hostname\":\"github.com\",\"Remote Port\":80,\"Connections\":5,\"Connection μs Avg\":59.559118,\"Connection μs Min\":58.720257,\"Connection μs Max\":60.817410,\"Timeouts\":0,\"Timeout μs Avg\":0.000000,\"Refused\":0,\"Unreachable\":0},{\"IPv\":\"4\",\"Interface\":\192.168.222.180\",\"Remote Address\":\"192.168.222.180\",\"Remote Hostname\":\"pochi.501.brewup.com\",\"Remote Port\":80,\"Connections\":0,\"Connection μs Avg\":0.000000,\"Connection μs Min\":0.000000,\"Connection μs Max\":0.000000,\"Timeouts\":0,\"Timeout μs Avg\":0.000000,\"Refused\":5,\"Unreachable\":0}]";
        
        URL url = new URL("http://localhost:"+ mockServerPort + "/JDTest");
        //HttpURLConnection hurl = (HttpURLConnection) url.openConnection();

        mockServer.enqueue(new MockResponse());

        ConnectOMatic.postToUri( url.toURI(),  data);   
        
        RecordedRequest request = mockServer.takeRequest();

        assertEquals( request.getPath(), "/JDTest", "Path");
        assertEquals( request.getMethod(), "POST", "Method");
        assertEquals( request.getBodySize(), 608, "Body Size");
    }

}
