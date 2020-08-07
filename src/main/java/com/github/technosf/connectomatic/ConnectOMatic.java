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
 * 
 * @since 1.0.0
 * 
 * @version 1.0.0
 * 
 * @author technosf
 */
public class ConnectOMatic
{

	static LocalInterface	localInterface;
	static CLIReader		clireader;


	/**
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
			System.out.println(localInterface.toString());
			System.exit(0);
		}

		clireader = new CLIReader(args);

		if ( !clireader.isValid() )
		/*
		 * Input args were not valid. Output the feedback and exit
		 * 
		 */
		{
			System.out.println(clireader.getFeedback());
			System.exit(1);
		}

		if ( clireader.wantHelp() )
		/*
		 * Input args were not valid. Output the feedback and exit
		 * 
		 */
		{
			System.out.println(clireader.getFeedback());
		}
		else
		{
			System.out.println(testConnections());
		}
		System.exit(0);

	}


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

		StringBuilder sb = new StringBuilder(Result.CSV_HEADER).append("\n");
		for ( Result result : ConnectionTask.getResults().values() )
		{
			result.collate();
			sb.append(result.toString()).append("\n");
		}

		return sb.toString();
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
