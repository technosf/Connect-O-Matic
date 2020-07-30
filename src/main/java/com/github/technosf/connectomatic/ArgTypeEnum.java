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

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * CLI Argument types and flags
 * 
 * @author technosf
 * 
 * @since 0.0.1
 */
public enum ArgTypeEnum
{

	HELP ( "?" ), IPV ( "i" ), PORT ( "p" ), HOST ( "h" ), UNKNOWN ( null ), NOT_A_FLAG ( null );


	private final static String						FLAG_PREFIX	= "-";	// Argument flag prefix

	private final static Map< String, ArgTypeEnum >	FLAG_INDEX;			// Store for an index of flags

	private final boolean							IS_A_FLAG;			// Is flagged as an argument
	private final String							FLAG;				// Is flagged as an argument


	/**
	 * Constructs the enum for argument type flags
	 * 
	 * @param argFlag
	 *                    Flag for a particular argument type
	 * 
	 * @throws Exception
	 *                       Thrown if duplicate argument flags are submitted
	 */
	private ArgTypeEnum ( String argFlag ) throws ExceptionInInitializerError
	{
		if ( argFlag == null )
		{
			FLAG		= "";
			IS_A_FLAG	= false;
		}
		else
		{
			FLAG		= argFlag.trim();
			IS_A_FLAG	= true;
		}
	}


	static
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
	 * @param arg
	 * 
	 * @return
	 */
	static ArgTypeEnum getArgType ( String argFlag )
	{
		if ( !argFlag.trim().startsWith(FLAG_PREFIX) )
			return NOT_A_FLAG;
		return FLAG_INDEX.getOrDefault(argFlag.trim().substring(1), UNKNOWN);

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
	 * Returns the representation of the flag
	 * 
	 * @return the flag, or empty string
	 */
	String getFlag ()
	{
		return FLAG_PREFIX + FLAG;
	}

}
