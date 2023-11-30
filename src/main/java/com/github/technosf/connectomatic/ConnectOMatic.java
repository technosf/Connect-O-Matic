/*
 * Connect-O-Matic - IP network connection tester [https://github.com/technosf/Connect-O-Matic]
 * 
 * Copyright 2023 technosf [https://github.com/technosf]
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
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.jar.Manifest;


/**
 * ConnectOMatic
 * <p>
 * Main class of <i>Connect-O-Matic</i>
 * <p>
 * Oranizes classes that chcek input arguments, identify local interfaces and process connections.
 * THis class then collates and outputs the results.
 * <p>
 * Results can be in <i>.csv</i> format that can be saved of for examination in a spreadsheet, or
 * <i>JSON</i>, or posted to an URL as <i>JSON</i>
 * 
 * @since 1.0.0
 * 
 * @version 1.2.0
 * 
 * @author technosf
 */
public class ConnectOMatic
{
	private static final String		CONST_FORMAT_DRYRUN
			= "==> Dry Run\n";

	private static final String		CONST_FORMAT_HELP	
			= "Output by default is .csv with header - JSON via a switch.\nFields are:"
			+ "\n\t• IPv\n\t• Local Interface\n\t• Remote Address\n\t• Remote Hostname\n\t• Remote Port"
			+ "\n\t• Connections\n\t• Connection μs Avg\n\t• Connection μs Min\n\t• Connection μs Max\n\t• Timeouts\n\t• Timeout μs Avg"
			+ "\n\t• Refused connection count\n\t• Unreachable network count\n";


	private static LocalInterface	localInterface;
	private static CLIReader		clireader;
	private static StringBuilder 	data = new StringBuilder();
	
	/**
	 * Entry point for the executable .jar
	 * <p>
	 * Takes the arguments, processes then and processes accordingly, producing output to the CLI
	 * 
	 * @param args
	 */
	public static void main ( String[] args )
	{
		System.out.println("\nConnect-O-Matic\t\tVersion: " + getVersion() + "\n");

		localInterface = new LocalInterface();
		if ( !localInterface.isValid() )
		/*
		 * Error getting network i/f
		 * 
		 */
		{
			System.out.println("Could not determine local network interfaces. Exiting");
			System.exit(1);
		}

		if ( args.length == 0 )
		{
			System.out.println("Use \"-?\" for help\n");
			System.out.println(localInterface.toString());
			System.exit(0);
		}

		clireader = new CLIReader(localInterface.getLocalAddresses(), args);

		if ( clireader.wantHelp() )
		/*
		 * Input args were not valid. Output the feedback and exit
		 * 
		 */
		{
			System.out.print(clireader.getFeedback());
			System.out.println(CONST_FORMAT_HELP);
			System.exit(0);
		}

		if ( !clireader.isValid() )
		/*
		 * Input args were not valid. Output the feedback and exit
		 * 
		 */
		{
			System.out.println(clireader.getFeedback());
			System.exit(1);
		}

		if ( clireader.isDryrun() )
		{
			System.out.println(CONST_FORMAT_DRYRUN);
		}

		queueConnections();
		System.out.println(tryConnections());
		System.exit(0);

	} // main


	/**
	 * Contruct all connection attempts
	 * 
	 */
	private static void queueConnections ()  
	{
		for ( int port : clireader.getPorts() )
		{
			if ( clireader.isIPv4Target() )
			/*
			 * Process IPv4 hosts connections
			 */
			{
				for ( Inet4Address localif : localInterface.getIpV4Addresses().keySet() )
				{
					clireader.getIpV4Addresses()
							.forEach(remoteaddr -> ConnectionTask.submit( clireader.isJson(), localif, remoteaddr, port, clireader.getAttempts()));
				}
			} // for ipv4

			if ( clireader.isIPv6Target() )
			/*
			 * Process IPv6 hosts connections
			 */
			{
				for ( Inet6Address localif : localInterface.getIpV6Addresses().keySet() )
				{
					clireader.getIpV6Addresses()
							.forEach(remoteaddr -> ConnectionTask.submit( clireader.isJson(), localif, remoteaddr, port, clireader.getAttempts()));
				}
			} // for ipv6
		} // for port
	}

	/**
	 * Run through and retrieve all connection attempts
	 * 
	 * @return the connection try results
	 */
	private static String tryConnections ()  
	{
		int	connects = 0,timeouts = 0,refused = 0, unreachable = 0;
		boolean place = false;
		
		for ( ConnectionResult result : ConnectionTask.getResults().values() )
		{
			result.collate();
			data.append( ( place && clireader.isJson() ) ? "," : "") 
				.append(result.toString())
				.append("\n");
			connects += result.connects_millis.size();
			timeouts += result.timeouts_millis.size();
			refused += result.refused;
			unreachable += result.unreachable;
			place = true;
		}

		if ( clireader.isJson() ) 
		{
			data.insert( 0, "[" );		
			data.append("]");
		}

		if ( clireader.getHttpUri() != null )
		// POSTing result to HTTP end-point and output the HTTP Status 
		{
			return Integer.toString(postToUri( clireader.getHttpUri(), data.toString() ));
		}


		// Not POSTing results to endpoint
		StringBuilder header = new StringBuilder();


		if ( !clireader.isJson() ) 
		{
			data.append(ConnectionResult.CSV_HEADER).append("\n");
		}

		if (!clireader.isQuiet())
		{
			header.append("\tSummary \tConnects: ").append(connects)
			.append(" \tTimeouts: ").append(timeouts)
			.append(" \tRefused: ").append(refused)
			.append(" \tUnreachable: ").append(unreachable)
			.append("\n\n\n");
		}

		header.append(data);
		
		return header.toString();
	} // main


	/**
	 * POST data using HTTP to the given URI
	 * 
	 * @param huri the http URI
	 * @param data data to POST
	 * @return HTTP Status code
	 */
	protected static int postToUri( URI huri, String data ) 
	{
		int status = HttpURLConnection.HTTP_NOT_IMPLEMENTED;

		try {
			HttpURLConnection hurl = (HttpURLConnection) huri.toURL().openConnection();	
			hurl.setDoOutput(true);
			hurl.setRequestMethod("POST");
			hurl.setRequestProperty("Content-Type", "application/json");

			try(OutputStream os = hurl.getOutputStream()) 
			{
				byte[] input = data.getBytes(StandardCharsets.UTF_8);
				os.write(input, 0, input.length);
			}		

			status = hurl.getResponseCode();   

		} catch (IOException e) 
		{
			System.out.println("Error POSTing results.\n");
			e.printStackTrace();
			System.exit(1);
		}

		return status;

		/*
		 * Java 11+ Connection code - for any future upgrade
		 */
		// HttpClient client = HttpClient.newHttpClient();
        // HttpRequest request = HttpRequest.newBuilder()
        //     .uri(huri)
        //     .POST(HttpRequest.BodyPublishers.ofString(data))
        //     .build();
        //     try {
		// 		client.send(request, HttpResponse.BodyHandlers.discarding());
		// 	} catch (IOException | InterruptedException e) 
		// 	{				
		// 		System.out.println("Error POSTing results.\n");
		// 		e.printStackTrace();
		// 		System.exit(1);
		// 	} 
	}



	/**
	 * Derives, prints version info
	 */
	private static String getVersion ()
	{
		String version = "Unpackage Version";
		try
		{
			URL			url			= ConnectOMatic.class.getClassLoader().getResource("META-INF/MANIFEST.MF");
			Manifest	manifest	= new Manifest(url.openStream());
			version = manifest.getMainAttributes().getValue("Release");
		}
		catch ( Exception e )
		{
			System.err.println(e.getMessage());
		}
		return version;
	} // getVersion

}
