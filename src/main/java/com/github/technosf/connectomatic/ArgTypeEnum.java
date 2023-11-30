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

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * CLI Argument types and flags
 * 
 * @since 1.0.0
 * 
 * @version 1.2.0
 * 
 * @author technosf
 */
public enum ArgTypeEnum
{

	HELP ( "?" )
	, IPV ( "i", true )
	, PORT ( "p", true )
	, HOST ( "h", true )
	, ATTEMPTS ( "a", true )
	, LOCAL ( "l" )
	, JSON ( "j" )
	, URI ( "u", true )
	, QUIET ( "q" )
	, DRY ( "d" )
	, UNKNOWN ( null )
	, NOT_A_FLAG ( null );


	private static final String						FLAG_PREFIX	= "-";	// Argument flag prefix

	private static final Map< String, ArgTypeEnum >	FLAG_INDEX;			// Store for an index of flags

	private final boolean							IS_A_FLAG;			// Is flagged as an argument
	private final String							FLAG;				// The flag
	private final boolean							PARM_REQ;			// Is flagged as an argument


	/**
	 * Constructs the enum for argument type flags
	 * 
	 * @param argFlag
	 *                    Flag for a particular argument type
	 * @param reqParm
	 * 					Indicates if a following parameter is required
	 * 
	 * @throws Exception
	 *                       Thrown if duplicate argument flags are submitted
	 */
	private ArgTypeEnum ( String argFlag, boolean reqParm ) 
		throws ExceptionInInitializerError
	{
		if ( argFlag == null )
		{
			FLAG		= "";
			IS_A_FLAG	= false;
			PARM_REQ 	= false;
		}
		else
		{
			FLAG		= argFlag.trim();
			IS_A_FLAG	= true;
			PARM_REQ 	= reqParm;
		}
	}

	/**
	 * Constructs the enum for argument type flags
	 * 
	 * @param argFlag
	 *                    Flag for a particular argument type
	 * 
	 * @throws Exception
	 *                       Thrown if duplicate argument flags are submitted
	 */
	private ArgTypeEnum ( String argFlag) 
		throws ExceptionInInitializerError
	{
		this( argFlag, false); 
	}



	static
	/*
	 * Create a flag index
	 */
	{
		FLAG_INDEX = new ConcurrentHashMap<>();
		for ( ArgTypeEnum a : ArgTypeEnum.values() )
		{
			FLAG_INDEX.put(a.FLAG, a);
		}

	};


	/**
	 * Returns the enum for the given argument flag
	 * 
	 * @param argFlag
	 *                    a flag type string
	 * 
	 * @return enum for a flag type string
	 */
	static ArgTypeEnum getArgType ( String argFlag )
	{
		if ( !argFlag.trim().startsWith(FLAG_PREFIX) )
			return NOT_A_FLAG;
		return FLAG_INDEX.getOrDefault(argFlag.trim().substring(1), UNKNOWN);

	}


	/**
	 * Returns the representation of the flag
	 * 
	 * @return the flag, or empty string
	 */
	String getFlag ()
	{
		return FLAG_PREFIX + FLAG;
	}


	/**
	 * Does this {@code ArgTypeEnum} represent a flag?
	 * 
	 * @return true if this is a flag
	 */
	boolean isFlag ()
	{
		return IS_A_FLAG;
	}


	/**
	 * Does this {@code ArgTypeEnum} require a parameter
	 * 
	 * @return true if parameter required
	 */
	boolean reqParm ()
	{
		return PARM_REQ;
	}


}
