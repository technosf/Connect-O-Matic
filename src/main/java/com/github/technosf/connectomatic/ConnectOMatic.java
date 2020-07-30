/*
 * Connect-O-Matic - IP network connection tester
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

/**
 * ConnectOMatic
 * 
 * Main class of <i>Connect-O-Matic</i>
 * Checks input arguments and process connections, collating and outputting the results.
 * 
 * @author technosf
 *
 * @since 0.0.1
 */
public class ConnectOMatic
{

	static CLIReader clireader;


	/**
	 * @param args
	 */
	public static void main ( String[] args )
	{
		clireader = new CLIReader(args);

		if ( !clireader.isValid() )
		/*
		 * Input args were not valid. Output the feedback and exit
		 * 
		 */
		{
			System.out.print(clireader.getFeedback());
			System.exit(1);
		}

		if ( clireader.wantHelp() )
		/*
		 * Input args were not valid. Output the feedback and exit
		 * 
		 */
		{
			System.out.print(clireader.getFeedback());
		}
		else
		{
			System.out.print(testConnections());
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
		StringBuilder sb = new StringBuilder();

		return sb.toString();
	}

}
