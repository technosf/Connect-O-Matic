/*
 * Connect-O-Matic - IP network connection tester [https://github.com/technosf/Connect-O-Matic]
 * 
 * Copyright 2020 technosf [https://github.com/technosf]
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */
package com.github.technosf.connectomatic;

import java.io.IOException;
import java.net.ConnectException;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.Executors;

/**
 * A runnable task that makes a number of connection attempts from a given interface to a given address and
 * port number
 * <p>
 * Connection tasks are created using a static submit method that creates and runs the task in its own
 * execution service.
 * 
 * @since 1.0.0
 * 
 * @version 1.0.0
 * 
 * @author technosf
 */
public class ConnectionTask implements Callable< ConnectionTask.Result >
{

	private static final CompletionService< Result >	POOL	= new ExecutorCompletionService< Result >(
			Executors.newCachedThreadPool()
	);
	private static final Object							LOCK	= new Object();
	private static int									futures;
	// private static ConnectionTask z = new ConnectionTask();


	/**
	 * Result POJO
	 * 
	 * @author technosf
	 *
	 */
	public static class Result
	{

		public final static String	CSV_HEADER	= "\"IPv\",\"Interface\",\"Remote Address\",\"Remote Hostname\",\"Remote Port\",\"Connections\",\"Connection μs Avg\",\"Timeouts\",\"Timeout μs Avg\",\"Refused\",\"Unreachable\"";

		StringBuilder				sb			= new StringBuilder();
		String						ipv;
		String						local;
		String						address;
		String						hostname;
		List< Float >				connects_millis;
		List< Float >				timeouts_millis;
		int							refused, unreachable;
		String						result;
		boolean						collated;


		/**
		 * @param ipv
		 * @param local
		 * @param remoteaddress
		 * @param port
		 * @param pingcount
		 */
		Result ( String ipv, String local, InetAddress remoteaddress, int port, int pingcount )
		{

			this.ipv		= ipv;
			address			= remoteaddress.getHostAddress();
			hostname		= remoteaddress.getHostName();
			connects_millis	= new ArrayList< Float >(pingcount);
			timeouts_millis	= new ArrayList< Float >(pingcount);

			sb.append(ipv).append(",").append(local).append(",").append(address).append(",").append(hostname)
					.append(",").append(port);
			result = sb.toString();
		}


		/**
		 * Collate the results
		 */
		public void collate ()
		{
			if ( collated )
				return;

			sb.append(",").append(connects_millis.size()).append(",").append(
					connects_millis.stream().mapToDouble(Float::doubleValue).average().orElse(0)
			).append(",").append(timeouts_millis.size()).append(",").append(
					timeouts_millis.stream().mapToDouble(Float::doubleValue).average().orElse(0)
			).append(",").append(refused).append(",").append(unreachable);
			result		= sb.toString();
			collated	= true;
		}


		/**
		 *
		 */
		@Override
		public String toString ()
		{
			return result;
		}

	} // Result


	Result		result;
	int			port;
	int			pingcount;
	InetAddress	localaddress;
	InetAddress	remoteaddress;


	private ConnectionTask ()
	{};


	private ConnectionTask ( String ipv, InetAddress localaddress, InetAddress remoteaddress, int port, int pingcount )
	{
		super();
		result				= new Result(ipv, localaddress.getHostAddress(), remoteaddress, port, pingcount);
		this.port			= port;
		this.pingcount		= pingcount;
		this.localaddress	= localaddress;
		this.remoteaddress	= remoteaddress;
	}


	/**
	 * IPv4 Constructor
	 */
	private ConnectionTask ( Inet4Address localaddress, Inet4Address remoteaddress, int port, int pingcount )
	{
		this("4", localaddress, remoteaddress, port, pingcount);
	}


	/**
	 * IPv6 Constructor
	 */
	private ConnectionTask ( Inet6Address localaddress, Inet6Address remoteaddress, int port, int pingcount )
	{
		this("6", localaddress, remoteaddress, port, pingcount);
	}


	/**
	 * Submit a new {@code ConnectionTask} for IPv4
	 * 
	 * @param localaddress
	 *                          the local address to connection from
	 * @param remoteaddress
	 *                          the remote address to connection to
	 * @param port
	 *                          the port to connect to
	 * @param pingcount
	 *                          the number of times to try and connect
	 */
	public static void submit ( Inet4Address localaddress, Inet4Address remoteaddress, int port, int pingcount )
	{
		synchronized ( LOCK )
		{
			POOL.submit(new ConnectionTask(localaddress, remoteaddress, port, pingcount));
			futures++;
		}
	}


	/**
	 * Submit a new {@code ConnectionTask} for IPv6
	 * 
	 * @param localaddress
	 *                          the local address to connection from
	 * @param remoteaddress
	 *                          the remote address to connection to
	 * @param port
	 *                          the port to connect to
	 * @param pingcount
	 *                          the number of times to try and connect
	 */
	public static void submit ( Inet6Address localaddress, Inet6Address remoteaddress, int port, int pingcount )
	{
		synchronized ( LOCK )
		{
			POOL.submit(new ConnectionTask(localaddress, remoteaddress, port, pingcount));
			futures++;
		} // synchronized
	} // submit


	/**
	 * Returns the results of all submitted {@code ConnectionTask}up until this point
	 * 
	 * Results are ordered by a digest of the input parameters
	 * 
	 * @return An ordered map of the results
	 */
	public static Map< String, Result > getResults ()
	{
		synchronized ( LOCK )
		{
			Map< String, Result > results = new TreeMap<>();
			while ( futures > 0 )
			{
				futures--;
				Result result = null;
				try
				{
					result = POOL.take().get();
					results.put(result.toString(), result);
				}
				catch ( NullPointerException e )
				{
					System.err.println("A Result was null. Exiting");
					System.exit(1);
				}
				catch ( InterruptedException | ExecutionException e )
				{
					System.err.println(e.getMessage());
					e.printStackTrace();
					System.exit(1);
				} // try
			} // while
			return results;
		} // synchronized
	} // getResults


	/**
	 * Callable routine that performs the connection task
	 * 
	 * Performs the connection several time, calculating the time to open the connection
	 */
	@Override
	public Result call ()
	{
		float				nanotime	= 0;
		InetSocketAddress	local		= new InetSocketAddress(localaddress, 0);
		InetSocketAddress	remote		= new InetSocketAddress(remoteaddress, port);

		for ( int ping = 0; ping < pingcount; ping++ )
		/*
		 * Attempt to connect from given localaddress/any port to remoteaddress/port
		 */
		{
			try ( Socket socket = new Socket() )
			{
				try
				{
					socket.bind(local);
				}
				catch ( IOException e )
				{
					System.out.println("Unexprected Exception in Connection Task call() binding socket");
					e.printStackTrace();
					return result;
				} // try/catch

				try
				{
					nanotime -= System.nanoTime();
					socket.connect(remote);
					nanotime += System.nanoTime();

					if ( socket.isConnected() )
					{
						result.connects_millis.add(nanotime / 1000000);
					}
					else
					{
						result.timeouts_millis.add(nanotime / 1000000);
					}
				}
				catch ( ConnectException e )
				/*
				 * Connection refused
				 */
				{
					result.refused++;
					continue;
				}
				catch ( SocketException e )
				/*
				 * Connection refused
				 */
				{
					result.unreachable++;
					continue;
				}
				catch ( Exception e )
				{
					System.out.print("Unexpected Exception in Connection Task call() opening socket");
					System.out.print(localaddress);
					System.out.print(" - ");
					System.out.println(remoteaddress);
					e.printStackTrace();
				} // try/catch
			} // try with resource
			catch ( IOException e )
			{
				// Auto close exception - ignore
			}
		} // for

		return result;
	} // call

}
