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
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Reads CLI arguments and generates the configuration objects
 * <p>
 * Derives a set of {@code InetAddress} objects that can be resolved and reached from input host arguments
 * 
 * @since 1.0.0
 * 
 * @version 1.0.1
 * 
 * @author technosf
 */
public class CLIReader
{

// @formatter:off
	private static final String			HELP_LEGEND		= "Help:" 
														+ "\n\t-i\tIPv - 4 and/or 6, defaults to 4 and 6 if absent" 	
														+ "\n\t-p\tPort numbers, at least one required" 	
														+ "\n\t-h\tHosts as hostnames, IPv4 or IPv6 addresses, at least one required"
														+ "\n\t-?\tProduces this message" 
														+ "\n\nExamples:"
														+ "\n\tjava -jar connectomatic-*.*.*.jar -p 22 80 -h github.com www.github.com"
														+ "\n\tjava -jar connectomatic-*.*.*.jar -i 4,6 -p 22,80 -h github.com,www.github.com\n\n" 	;
// @formatter:on

	private boolean						help, valid, IPv4Target, IPv6Target;
	private Map< Inet4Address, String >	IPv4Addresses	= new HashMap<>();
	private Map< Inet6Address, String >	IPv6Addresses	= new HashMap<>();
	private Set< String >				BadHosts		= new HashSet<>();
	private Set< Integer >				Ports			= new HashSet<>();

	private StringBuilder				feedback		= new StringBuilder();


	/**
	 * Creates a CLIReader from the given args
	 * <p>
	 * Uses a crude state machine to cycle through the args and accumulate settings
	 * and errors.
	 * <p>
	 * For each arg, checks to see if the initial state has been set and if
	 * it is this arg is a valid value.
	 * 
	 * @param args
	 *                 from the CLI
	 */
	CLIReader ( String[] args )
	{
		ArgTypeEnum currentState = ArgTypeEnum.NOT_A_FLAG; // The current state arg type

		for ( String arg : args )
		{
			ArgTypeEnum thisArgType = ArgTypeEnum.getArgType(arg);

			if ( ArgTypeEnum.HELP == thisArgType )
			/*
			 * If help was requested provide the legend as feedback and exit
			 */
			{
				help = true;
				feedback.setLength(0);
				feedback.append(HELP_LEGEND);
				IPv4Addresses.clear();
				IPv6Addresses.clear();
				BadHosts.clear();
				Ports.clear();
				return;
			}

			if ( thisArgType.isFlag() )
			/*
			 * This arg is a valid flag - reset the current state and break to parse the
			 * next arg
			 */
			{
				if ( currentState == thisArgType ) // Found a repeating flag
					feedback.append("Duplicate flag \'").append(arg).append("\'\n");
				currentState = thisArgType;
				continue;
			} // if

			if ( ArgTypeEnum.NOT_A_FLAG == currentState && !thisArgType.isFlag() )
			/*
			 * The current state is unset, so the first flag has not yet been found,
			 * and this arg is not a flag either (See prior clause too), so
			 * error condition.
			 */
			{
				feedback.append("Expected an argument flag, but got: \'").append(arg).append("\'\n");
				continue;
			} // if

			/*
			 * Parse the set of args globbed in this string.
			 */
			for ( String splitarg : arg.trim().split(",") )
			{
				switch ( currentState )
				{
					case IPV:
						processIPV(splitarg);
						break;
					case PORT:
						processPort(splitarg);
						break;
					case HOST:
						processHost(splitarg);
						break;
					default:
						feedback.append("Unknown argument: \'").append(splitarg).append("\'\n");
				} // switch
			} // for
		} // for

		/*
		 * Check CLI validity
		 */
		if ( IPv4Addresses.isEmpty() && IPv6Addresses.isEmpty() )
		{
			feedback.append("No valid addresses or host specified.\n");
		} // if

		if ( Ports.isEmpty() )
		{
			feedback.append("No ports specified.\n");
		} // if

		if ( feedback.length() > 0 )
		{
			return;
		} // if

		/*
		 * Final setup
		 */

		if ( !IPv4Target && !IPv6Target )
		{
			IPv4Target	= true;
			IPv6Target	= true;
		} // if

		IPv4Addresses	= Collections.unmodifiableMap(IPv4Addresses);
		IPv6Addresses	= Collections.unmodifiableMap(IPv6Addresses);
		Ports			= Collections.unmodifiableSet(Ports);
		BadHosts		= Collections.unmodifiableSet(BadHosts);

		valid			= true;

	} // CLIReader


	/**
	 * Is the CLI valid?
	 * 
	 * @return true if valid
	 */
	public boolean isValid ()
	{
		return valid;
	} // isValid


	/**
	 * Was the Help flag set?
	 * 
	 * @return true if so
	 */
	public boolean wantHelp ()
	{
		return help;
	} // wantHelp


	/**
	 * Returns any feedback on argument validity, also the help message if requested
	 * 
	 * @return the feedback
	 */
	public String getFeedback ()
	{
		return feedback.toString();
	} // getFeedback


	/**
	 * Were IPv4 targets connects requested?
	 * 
	 * @return true for IPv4
	 */
	public boolean isIPv4Target ()
	{
		return IPv4Target;
	} // isIPv4Target


	/**
	 * Were IPv6 targets connects requested?
	 * 
	 * @return true for IPv6
	 */
	public boolean isIPv6Target ()
	{
		return IPv6Target;
	} // isIPv6Target


	/**
	 * Get the IPv4 addresses that were resolved from the hosts
	 * 
	 * @return requested IPv4 addresses
	 */
	public Set< Inet4Address > getIPv4Addresses ()
	{
		return IPv4Addresses.keySet();
	} // getIPv4Addresses


	/**
	 * Get the IPv6 addresses that were resolved from the hosts
	 * 
	 * @return requested IPv6 addresses
	 */
	public Set< Inet6Address > getIPv6Addresses ()
	{
		return IPv6Addresses.keySet();
	} // getIPv6Addresses


	/**
	 * Returns hosts that could not be resolved or reached
	 * 
	 * @return bad hosts
	 */
	public Set< String > getBadHosts ()
	{
		return BadHosts;
	} // getBadHosts


	/**
	 * Get the ports that were requested
	 * 
	 * @return requested ports
	 */
	public Set< Integer > getPorts ()
	{
		return Ports;
	} // getPorts


	/**
	 * Processes IPv flag arguments
	 * 
	 * @param splitarg
	 *                     the IPv flag arguments
	 */
	private void processIPV ( String splitarg )
	{
		switch ( splitarg )
		{
			case "4":
				IPv4Target = true;
				break;
			case "6":
				IPv6Target = true;
				break;
			default:
				feedback.append("Unknown IPv: \'").append(splitarg).append("\'\n");
		} // switch
	} // processIPV


	/**
	 * Processes Port flag arguments
	 * 
	 * @param splitarg
	 *                     the port flag arguments
	 */
	private void processPort ( String splitarg )
	{
		try
		{
			Ports.add(Integer.decode(splitarg));
		} // try
		catch ( NumberFormatException e )
		{
			feedback.append("Unknown port: \'").append(splitarg).append("\'\n");
		} // catch
	} // processPort


	/**
	 * Processes Hosts flag arguments
	 * 
	 * @param splitarg
	 *                     the hosts flag arguments
	 */
	private void processHost ( String splitarg )
	{
		try
		{
			for ( InetAddress address : InetAddress.getAllByName(splitarg) )
			{
				if ( address instanceof Inet4Address )
				{
					IPv4Addresses.put((Inet4Address) address, splitarg);
				} // if
				else if ( address instanceof Inet6Address )
				{
					IPv6Addresses.put((Inet6Address) address, splitarg);
				} // else if
				else
				{
					BadHosts.add(splitarg);
				} // else
			} // for
		} // try
		catch ( UnknownHostException e )
		{
			BadHosts.add(splitarg);
			feedback.append("Error on Host/Address: \'").append(splitarg).append("\'\n\tError:").append(e.getMessage())
					.append("\'\n");
		} // catch
	} // processHost

} // CLIReader
