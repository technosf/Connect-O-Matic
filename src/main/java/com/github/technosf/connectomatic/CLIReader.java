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
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.net.URL;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Reads CLI arguments and generates the configuration objects
 * <p>
 * Derives a set of {@code InetAddress} objects that can be resolved and reached from input host arguments
 * 
 * @since 1.0.0
 * 
 * @version 1.2.0
 * 
 * @author technosf
 */
public class CLIReader
{

	private static final String FMT_ERROR = "\'\n\tError:";
	static final int CONNECTS_DEFAULT = 5;
	private static final int CONNECTS_MAX = 64;
	private static final int PORT_MAX = 65535;
	private static final Pattern REGEX_PORT_RANGE = Pattern.compile("(\\d+)-(\\d+)"); // Regex to capture 999-999 ranges

  
// @formatter:off
	private static final String			HELP_LEGEND		= "Copyright 2023  technosf  [http://github.com/technosf]\n\nHelp:" 
														+ "\n\t-i\tIPv - 4 and/or 6, defaults to 4 and 6 if absent" 	
														+ "\n\t-p\tPort numbers, at least one required, can be a hyphenated range" 	
														+ "\n\t-h\tHosts as hostnames, IPv4 or IPv6 addresses, at least one required"
														+ "\n\t-a\tAttempts to connect, defaults to 5, but can be 1-255"
														+ "\n\t-l\tLocal addresses in the host set should be tested and not ignored"
														+ "\n\t-j\tProduce JSON output instead of CSV"
														+ "\n\t-u\tURI, POST JSON results to the provided URI"
														+ "\n\t-q\tQuiet mode, outputs result only, without preamble or summary"
														+ "\n\t-d\tDry-run, run through resolving the targets without attempting any connects"
														+ "\n\t-?\tProduces this message" 
														+ "\n\nExamples:"
														+ "\n\tjava -jar connectomatic-*.*.*.jar -p 22 80 -h github.com www.github.com"
														+ "\n\tjava -jar connectomatic-*.*.*.jar -p 80-90 -h github.com www.github.com localhost -l"
														+ "\n\tjava -jar connectomatic-*.*.*.jar -i 4,6 -p 22,80-90 -h github.com,www.github.com" 	
														+ "\n\tjava -jar connectomatic-*.*.*.jar -j -i 4,6 -p 22,80-90 -h github.com,www.github.com" 	
														+ "\n\tjava -jar connectomatic-*.*.*.jar -a 1 -u http://myobjectdb/index -i 6 -p 22,80-90 -h github.com,www.github.com\n\n";	
// @formatter:on

	private boolean						help, valid, IPv4Target, IPv6Target, local, json, quiet, dry;
	private Set<ArgTypeEnum>			usedFlags 		= new HashSet<>();
	private Map< Inet4Address, String >	ipV4Addresses	= new HashMap<>();
	private Map< Inet6Address, String >	ipV6Addresses	= new HashMap<>();
	private Set< String >				badHosts		= new HashSet<>();
	private Set< Integer >				ports			= new HashSet<>();
	private int							attempts		= CONNECTS_DEFAULT;
	private URI							httpUri;

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
	CLIReader ( Set< String > localAddresses, String[] args )
	{
		processArgs( args );

		/*
		 * Check CLI validity
		 */
		if ( ipV4Addresses.isEmpty() && ipV6Addresses.isEmpty() )
		{
			feedback.append("No valid addresses or host specified.\n");
		} // if

		if ( ports.isEmpty() )
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

		if ( !local )
		// Scrub local addresses from lists
		{
			ipV4Addresses.entrySet().removeIf(e -> localAddresses.contains(e.getValue()));
			ipV6Addresses.entrySet().removeIf(e -> localAddresses.contains(e.getValue()));
		}

		attempts		= dry ? 0 : attempts;
		ipV4Addresses	= Collections.unmodifiableMap(ipV4Addresses);
		ipV6Addresses	= Collections.unmodifiableMap(ipV6Addresses);
		ports			= Collections.unmodifiableSet(ports);
		badHosts		= Collections.unmodifiableSet(badHosts);

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
	 * Were Local->local circuit connects requested?
	 * 
	 * @return true for IPv6
	 */
	public boolean isLocalIncluded ()
	{
		return local;
	} // isLocalIncluded


	/**
	 * Was dry run requested?
	 * 
	 * @return true for Dry run
	 */
	public boolean isDryrun ()
	{
		return dry;
	} // isDryrun


	/**
	 * Was quiet output (no summary) requested?
	 * 
	 * @return true for Quiet
	 */
	public boolean isQuiet ()
	{
		return quiet;
	} // isQuiet


	/**
	 * Was JSON output (no CSV) requested?
	 * 
	 * @return true for JSON, false for CSV
	 */
	public boolean isJson ()
	{
		return json;
	} // isJson


	/**
	 * Get the IPv4 addresses that were resolved from the hosts
	 * 
	 * @return requested IPv4 addresses
	 */
	public Set< Inet4Address > getIpV4Addresses ()
	{
		return ipV4Addresses.keySet();
	} // getIPv4Addresses


	/**
	 * Get the IPv6 addresses that were resolved from the hosts
	 * 
	 * @return requested IPv6 addresses
	 */
	public Set< Inet6Address > getIpV6Addresses ()
	{
		return ipV6Addresses.keySet();
	} // getIPv6Addresses


	/**
	 * Returns hosts that could not be resolved or reached
	 * 
	 * @return bad hosts
	 */
	public Set< String > getBadHosts ()
	{
		return badHosts;
	} // getBadHosts


	/**
	 * Get the ports that were requested
	 * 
	 * @return requested ports
	 */
	public Set< Integer > getPorts ()
	{
		return ports;
	} // getPorts


	/**
	 * Get the number of connection attempts requested
	 * 
	 * @return requested attempts
	 */
	public int getAttempts ()
	{
		return attempts;
	} // getAttempts

	
	/**
	 * Get the Http URI to POST JSON results to
	 * Return NULL is no URL was provided in the parameters.
	 * 
	 * @return the Http URI, or NULL 
	 */
	public URI getHttpUri ()
	{
		return httpUri;
	} // getHttpUri

	//---------------------------------------------------------------------

	/**
	 * 
	 * @param arg
	 */
	private void processArgs( String[] args )
	{
		ArgTypeEnum currentState = ArgTypeEnum.NOT_A_FLAG; // The current state arg type
		boolean argReqParams = false;

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
				ipV4Addresses.clear();
				ipV6Addresses.clear();
				badHosts.clear();
				ports.clear();
				return;
			}

			if ( thisArgType.isFlag() )
			/*
				* This arg is a valid flag - reset the current state and break to parse the
				* next arg
				*/
			{
				if ( usedFlags.contains(thisArgType) ) // Found a repeating flag
					feedback.append("Duplicate flag \'").append(arg).append("\'\n");

				if ( argReqParams )	// Require param flag found for last arg type
					feedback.append("Missing param for flag \'").append(currentState.getFlag()).append("\'\n");

				usedFlags.add(thisArgType);
				currentState = thisArgType;
				argReqParams = currentState.reqParm();
				
				switch ( currentState )
				/* Set any boolean flgs */
				{
					case LOCAL:
						local	= true;
						continue;
					case JSON:
						json 	= true;
						continue;
					case QUIET:
						quiet	= true;
						continue;
					case DRY:
						dry	= true;
						continue;
					default:
						continue;
				} // switch
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

			//Process params
			argReqParams = false;
			
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
					case ATTEMPTS:
						processAttempts(splitarg);
						break;
					case URI:
						processUrl(splitarg);
						break;
					default:
						feedback.append("Unknown argument: \'").append(splitarg).append("\'\n");
				} // switch
			} // for
		}
	}

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
		Matcher matcher = REGEX_PORT_RANGE.matcher( splitarg );		

		try
		{

			if ( matcher.matches() )
			// Process port ranges
			{
				Integer portStart = Integer.decode( matcher.group(1) );
				Integer portEnd = Integer.decode( matcher.group(2) );

				if ( ( portEnd < portStart ) || ( portEnd > PORT_MAX ) )
				{
					feedback.append("Invalid port range: \'").append(splitarg).append("\'\n");
				}				
				else
				{
					while ( portStart <= portEnd ) ports.add(portStart++);
				}

				return;
			}

			// Process individual port number 
			Integer port = Integer.decode(splitarg);

			if ( ( port < 1 ) || ( port > PORT_MAX ) )
			{
				feedback.append("Invalid port range: \'").append(splitarg).append("\'\n");
			}
			else
			{
				ports.add(port);
			}			

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
					ipV4Addresses.put((Inet4Address) address, splitarg);
				} // if
				else if ( address instanceof Inet6Address )
				{
					ipV6Addresses.put((Inet6Address) address, splitarg);
				} // else if
				else
				{
					badHosts.add(splitarg);
				} // else
			} // for
		} // try
		catch ( UnknownHostException e )
		{
			badHosts.add(splitarg);
			feedback.append("Error on Host/Address: \'").append(splitarg).append(FMT_ERROR).append(e.getMessage())
					.append("\'\n");
		} // catch
	} // processHost


	private void processUrl(String splitarg) 
	{
		try {
			URL httpUrl = new URL(splitarg);
			httpUri = httpUrl.toURI();
			httpUrl.openConnection();			
		} 
		catch (MalformedURLException e) 
		{			
			feedback.append("Error with URL format: \'").append(splitarg).append(FMT_ERROR).append(e.getMessage())
					.append("\'\n");
		} 
		catch (URISyntaxException e) 
		{
			feedback.append("Error with URL syntax: \'").append(splitarg).append(FMT_ERROR).append(e.getMessage())
					.append("\'\n");
		} 
		catch (IOException e) 
		{
			feedback.append("Error with URL endpoint: \'").append(splitarg).append(FMT_ERROR).append(e.getMessage())
					.append("\'\n");
		}
    } // processUri


    private void processAttempts(String splitarg) 
	{
		int a;

		try 
		{	
			a = Integer.parseInt(splitarg);

			if ( a > 0 && a <= CONNECTS_MAX ) 
			{
				attempts = a;
				return;
			}
		}
		catch (NumberFormatException e)
		{
			//
		}
		
		feedback.append("Error on Attempts (requires a integer of 1 to 50): \'").append(splitarg).append("\'\n");
	} // processAttempts

} // CLIReader
