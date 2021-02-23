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

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.URL;
import java.util.jar.Manifest;

import com.github.technosf.connectomatic.ConnectionTask.Result;

/**
 * ConnectOMatic
 * <p>
 * Main class of <i>Connect-O-Matic</i>
 * Checks input arguments and process connections, collating and outputting the results.
 * <p>
 * Results are <i>.csv</i> format that can be saved of for examination in a spreadsheet
 * 
 * @since 1.0.0
 * 
 * @version 1.1.0
 * 
 * @author technosf
 */
public class ConnectOMatic
{

	private static LocalInterface	localInterface;
	private static CLIReader		clireader;

	private static final String		CONST_FORMAT_HELP	= "Output is .csv, with header. Fields are:"
			+ "\n\t• IPv\n\t• Local Interface\n\t• Remote Address\n\t• Remote Hostname\n\t• Remote Port"
			+ "\n\t• Connections\n\t• Connection μs Avg\n\t• Connection μs Min\n\t• Connection μs Max\n\t• Timeouts\n\t• Timeout μs Avg"
			+ "\n\t• Refused connection count\n\t• Unreachable network count\n";


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

		clireader = new CLIReader(args);

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

		System.out.println(testConnections());
		System.exit(0);

	} // main


	/**
	 * Run through and test all connections
	 * 
	 * @return the connection test results
	 */
	private static String testConnections ()
	{
		for ( int port : clireader.getPorts() )
		{
			if ( clireader.isIPv4Target() )
			/*
			 * Process IPv4 hosts connections
			 */
			{
				for ( Inet4Address localif : localInterface.getIPv4Addresses().keySet() )
				{
					clireader.getIPv4Addresses()
							.forEach(remoteaddr -> ConnectionTask.submit(localif, remoteaddr, port, 5));
				}
			} // for ipv4

			if ( clireader.isIPv6Target() )
			/*
			 * Process IPv6 hosts connections
			 */
			{
				for ( Inet6Address localif : localInterface.getIPv6Addresses().keySet() )
				{
					clireader.getIPv6Addresses()
							.forEach(remoteaddr -> ConnectionTask.submit(localif, remoteaddr, port, 5));
				}
			} // for ipv6
		} // for port


		int	connects = 0,timeouts = 0,refused = 0, unreachable = 0;

		StringBuilder data = new StringBuilder(Result.CSV_HEADER).append("\n");
		StringBuilder header = new StringBuilder();
		
		for ( Result result : ConnectionTask.getResults().values() )
		{
			result.collate();
			data.append(result.toString()).append("\n");
			connects += result.connects_millis.size();
			timeouts += result.timeouts_millis.size();
			refused += result.refused;
			unreachable += result.unreachable;
		}
		
		header.append("\tSummary \tConnects: ").append(connects)
		.append(" \tTimeouts: ").append(timeouts)
		.append(" \tRefused: ").append(refused)
		.append(" \tUnreachable: ").append(unreachable)
		.append("\n\n\n")
		.append(data);
		
		
		return header.toString();
	} // main


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
		{}
		return version;
	} // getVersion

}
