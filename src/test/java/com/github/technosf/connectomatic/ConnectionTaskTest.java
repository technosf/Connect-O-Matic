package com.github.technosf.connectomatic;

import static org.testng.Assert.assertEquals;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.util.Map;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.github.technosf.connectomatic.ConnectionTask.Result;

public class ConnectionTaskTest
{

	TimeServer	ts4, ts6;
	Thread		t4, t6;
	int			t4p	= 12344;
	int			t6p	= 12346;


	@BeforeClass
	public void beforeClass () throws UnknownHostException, Exception
	{
		ts4	= new TimeServer(InetAddress.getByName("127.0.0.1"), t4p, 500);
		ts6	= new TimeServer(InetAddress.getByName("::1"), t6p, 500);
		t4	= new Thread(ts4);
		t6	= new Thread(ts6);
		t4.start();
		t6.start();
	}


	@AfterClass
	public void afterClass ()
	{
		TimeServer.stop();
	}


	@Test ( groups =
	{
			"init"
	} )
	public void submitTestInet4AddressInet4Addressintint () throws UnknownHostException
	{
		Inet4Address local = (Inet4Address) Inet4Address.getByName("localhost");
		ConnectionTask.submit(local, local, t4p, 5);
	}


	@Test ( groups =
	{
			"init"
	} )
	public void submitTestInet6AddressInet6Addressintint () throws UnknownHostException
	{
		Inet6Address local = (Inet6Address) Inet6Address.getByName("::1");
		ConnectionTask.submit(local, local, t6p, 5);
	}


	@Test ( dependsOnGroups =
	{
			"init.*"
	} )
	public void getResultsTest ()
	{
		Map< String, Result > results = ConnectionTask.getResults();
		assertEquals(results.size(), 2, "Submition count");
		results.forEach( ( i, j ) ->
		{
			j.collate();
			System.out.println(j);
		});
	}

	/*
	 * ------------------------- Fixtures -------------------------------
	 */


	private static class TimeServer implements Runnable
	{

		static boolean	run	= true;
		int				port;
		long			delayms;
		ServerSocket	serverSocket;


		static void stop ()
		{
			System.out.println("TimeServers stopping");
			run = false;
		}


		public TimeServer ( InetAddress addr, int port, long delayms ) throws Exception
		{
			serverSocket	= new ServerSocket(port, 10, addr);
			this.port		= port;
			this.delayms	= delayms;
		}


		@Override
		public void run ()
		{

			System.out.println("TimeServer start:" + port);

			while ( run )
			{
				try
				{
					Socket clientSocket;
					clientSocket = serverSocket.accept();
					OutputStream	output	= clientSocket.getOutputStream();
					PrintWriter		writer	= new PrintWriter(output, true);

					writer.println(LocalDateTime.now());
					Thread.sleep(delayms);
					writer.close();
					output.close();
					clientSocket.close();
				}
				catch ( IOException e )
				{
					run = false;
				}
				catch ( InterruptedException e )
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			System.out.println("TimeServer stop:" + port);
		}

	}

}
