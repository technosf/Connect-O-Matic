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

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;

/**
 * Connection Result POJO
 * <p>
 * Broken out from {@code ConnectionTask}
 * 
 * @since 1.2.0
 * 
 * @version 1.2.0
 * 
 * @author technosf
 *
 */
public class ConnectionResult
{

	public static final String	CSV_HEADER		= "\"IPv\",\"Interface\",\"Remote Address\",\"Remote Hostname\",\"Remote Port\",\"Connections\",\"Connection μs Avg\",\"Connection μs Min\",\"Connection μs Max\",\"Timeouts\",\"Timeout μs Avg\",\"Refused\",\"Unreachable\"";
	private static final String	JSON_1_FORMAT	= "{\"IPv\":\"%s\",\"Interface\":\"%s\",\"Remote Address\":\"%s\",\"Remote Hostname\":\"%s\",\"Remote Port\":%s";
	private static final String	JSON_2_FORMAT	= ",\"Connections\":%d,\"Connection μs Avg\":%f,\"Connection μs Min\":%f,\"Connection μs Max\":%f,\"Timeouts\":%d,\"Timeout μs Avg\":%f,\"Refused\":%d,\"Unreachable\":%d}";


	private String				result;
	private boolean				collated;	
	private StringBuilder		sb			= new StringBuilder();
	private boolean				json;
	
	/*
	 * Allow the results to be directly modified by calling object
	 */
	List< Float >				connects_millis;
	List< Float >				timeouts_millis;
	int							refused, unreachable;


	/**
	 * Construct a result entry for the given parameters
	 * 
	 * @param json
	 *                          true for JSON output, false for CSV
	 * @param ipv
	 *                          the IPv
	 * @param localaddress
	 *                          local address call used for connections
	 * @param remoteaddress
	 *                          remote address connection attempts were made to
	 * @param port
	 *                          the remote port
	 * @param pingcount
	 *                          times connection attempts are to made, used to preallocate result array
	 */
	ConnectionResult ( boolean json, @NonNull String ipv, @NonNull InetAddress localaddress, @NonNull InetAddress remoteaddress, int port)//, int pingcount )
	{
		this.json 		= json;
		connects_millis	= new ArrayList< >();
		timeouts_millis	= new ArrayList< >();


		if (json)
		// JSON
		{
			sb.append(
				String.format(JSON_1_FORMAT
				,	ipv
				,	localaddress.getHostAddress()
				,	remoteaddress.getHostAddress()
				,	remoteaddress.getHostName()
				,	port
				)
			);
		}
		else
		// CSV
		{				
		sb.append(ipv).append(",")
			.append(localaddress.getHostAddress()).append(",")
			.append(remoteaddress.getHostAddress()).append(",")
			.append(remoteaddress.getHostName()).append(",")
			.append(port);
		}

		result = sb.toString();		
	}


	/**
	 * Collate the results
	 */
	public void collate ()
	{
		if ( collated )
			return;

		if (json)
		// JSON
		{
			sb.append(
				String.format(JSON_2_FORMAT
					, 	connects_millis.size()
					,	connects_millis.stream().mapToDouble(Float::doubleValue).average().orElse(0)
					,	connects_millis.stream().mapToDouble(Float::doubleValue).min().orElse(0)
					,	connects_millis.stream().mapToDouble(Float::doubleValue).max().orElse(0)
					,	timeouts_millis.size()
					,	timeouts_millis.stream().mapToDouble(Float::doubleValue).average().orElse(0)
					,	refused
					,	unreachable)
				);
		}
		else
		// CSV
		{				
			sb.append(",")
				.append(connects_millis.size()).append(",")
				.append(connects_millis.stream().mapToDouble(Float::doubleValue).average().orElse(0)).append(",")
				.append(connects_millis.stream().mapToDouble(Float::doubleValue).min().orElse(0)).append(",")
				.append(connects_millis.stream().mapToDouble(Float::doubleValue).max().orElse(0)).append(",")
				.append(timeouts_millis.size()).append(",")
				.append(timeouts_millis.stream().mapToDouble(Float::doubleValue).average().orElse(0)).append(",")
				.append(refused).append(",")
				.append(unreachable);
		}

		result		= sb.toString();
		collated	= true;
	}


	/**
	 * Output the result connection digest, and also the results once collated.
	 */
	@Override
	public String toString ()
	{
		return result;
	}

} // Result