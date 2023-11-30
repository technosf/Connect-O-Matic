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
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


/**
 * Extract the local network interfaces
 * <p>
 * Uses {@code java.net.NetworkInterface}
 * 
 * @since 1.0.0
 * 
 * @version 1.0.0
 * 
 * @author technosf
 */
public class LocalInterface
{

	private static final String FMT = "\n\t\t%-60s %s";

	/**
	 * Collation POJO
	 * 
	 * @param <T>
	 * @param <U>
	 */
	private static class Tuple< T, U >
	{

		T	primary;
		U	secondary;


		Tuple ( T primary, U secondary )
		{
			this.primary	= primary;
			this.secondary	= secondary;
		} // Tuple


		@Override
		public int hashCode() 
		{
			return primary.hashCode() + secondary.hashCode();
		}


		@Override
		public boolean equals(Object obj) 
		{
			return (obj != null && this.hashCode() == obj.hashCode());
		}

	};


	private Map< Inet4Address, Tuple< String, String > >	ipV4Addresses	= new HashMap<>();
	private Map< Inet6Address, Tuple< String, String > >	ipV6Addresses	= new HashMap<>();
	private Set< Tuple< String, String > >					ipV4Loopback	= new HashSet<>();
	private Set< Tuple< String, String > >					ipV6Loopback	= new HashSet<>();
	private Set< Tuple< String, String > >					ipV6LinkLocal	= new HashSet<>();
	private Set< Tuple< String, String > >					interfaces		= new HashSet<>();
	private Set< String >									localAddresses	= new HashSet<>();
	private String											digest;
	private boolean											valid;


	/**
	 * Identify local network interfaces and addresses at the point in time.
	 * The object {@code toString()} provides a digest of the interfaces found.
	 * 
	 */
	LocalInterface ()
	{
		try
		{
			collateLocalInterfaces();
		}
		catch ( SocketException e )
		{
			System.err.println(e.getMessage());
			return;
		}

		ipV4Addresses	= Collections.unmodifiableMap(ipV4Addresses);
		ipV6Addresses	= Collections.unmodifiableMap(ipV6Addresses);
		ipV4Loopback	= Collections.unmodifiableSet(ipV4Loopback);
		ipV6Loopback	= Collections.unmodifiableSet(ipV6Loopback);
		ipV6LinkLocal	= Collections.unmodifiableSet(ipV6LinkLocal);
		interfaces		= Collections.unmodifiableSet(interfaces);
		localAddresses	= Collections.unmodifiableSet(localAddresses);

		StringBuilder sb = new StringBuilder("Local Interfaces: ")
				.append(DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(LocalDateTime.now())).append("\n\nInterfaces:");

		interfaces.forEach(i ->
			sb.append(String.format(FMT, i.primary, i.secondary))
		);

		sb.append("\n\nLoopback Addresses:");
		if ( !ipV4Loopback.isEmpty() )
		{
			sb.append("\n\tIPv4");
			ipV4Loopback.forEach(i ->
				sb.append(String.format(FMT, i.primary, i.secondary))
			);
		} // if

		if ( !ipV6Loopback.isEmpty() )
		{
			sb.append("\n\tIPv6");
			ipV6Loopback.forEach(i ->
				sb.append(String.format(FMT, i.primary, i.secondary))
			);
		} // if

		if ( !ipV6LinkLocal.isEmpty() )
		{
			sb.append("\n\nLinkLocal Addresses IPv6:");
			ipV6LinkLocal.forEach(i ->
				sb.append(String.format(FMT, i.primary, i.secondary))
			);
		} // if

		if ( !ipV4Addresses.isEmpty() )
		{
			sb.append("\n\nIPv4 Addresses:");
			ipV4Addresses.forEach( ( i, j ) ->
				sb.append(String.format(FMT, j.primary, j.secondary))
			);
		} // if

		if ( !ipV6Addresses.isEmpty() )
		{
			sb.append("\n\nIPv6 Addresses:");
			ipV6Addresses.forEach( ( i, j ) ->
				sb.append(String.format(FMT, j.primary, j.secondary))
			);
		} // if

		digest	= sb.toString();
		valid	= true;

	} // LocalInterface


	/**
	 * Display local interface information
	 * 
	 * @param args
	 *                 ignored
	 * 
	 */
	public static void main ( String[] args )
	{
		System.out.println(new LocalInterface());
	}


	/**
	 * Was the information collated without issue?
	 * 
	 * @return true if valid
	 */
	public boolean isValid ()
	{
		return valid;
	} // isValid


	/**
	 * Retrieves and collates all local network interfaces
	 * <p>
	 * Uses {@code java.net.NetworkInterface} to retrieve local Interfaces
	 * and then collates them into collection with their external IP
	 * 
	 * @throws SocketException
	 */
	private void collateLocalInterfaces () 
		throws SocketException
	{
		for ( NetworkInterface nif : Collections.list(NetworkInterface.getNetworkInterfaces()) )
		{
			if ( nif == null ) continue;

			interfaces.add(
					new Tuple<>(String.format("%-32s", nif.getDisplayName()), stringMAC(nif.getHardwareAddress()))
			);

			for ( InterfaceAddress ifAddr : nif.getInterfaceAddresses() )
			// collate each individual address on this Interface
			{
				if ( ifAddr != null ) collateInterfaceAddress(ifAddr);
			} // for
		} // for
	} // collateLocalInterfaces


	/**
	 * Collates individual addresses
	 * 
	 * @param ifAddr
	 */
	private void collateInterfaceAddress(InterfaceAddress ifAddr) 
	{
		InetAddress				ia		= ifAddr.getAddress();
		localAddresses.add( ia.getHostAddress().split("%")[0] );
		Tuple< String, String >	addr	
			= new Tuple<>(String.format("%-32s", ia.getHostName()), ia.getHostAddress().split("%")[0]);

		if ( ia instanceof Inet4Address )
		{
			if ( ia.isLoopbackAddress() )
			{
				ipV4Loopback.add(addr);
			} // if
			else
			{
				ipV4Addresses.put((Inet4Address) ia, addr);
			} // else
		} // if
		else if ( ia instanceof Inet6Address )
		{
			if ( ia.isLoopbackAddress() )
			{
				ipV6Loopback.add(addr);
			} // if
			else if ( ia.isLinkLocalAddress() )
			{
				ipV6LinkLocal.add(addr);
			} // else if
			else
			{
				ipV6Addresses.put((Inet6Address) ia, addr);
			} // else
		} // else if
		else
		{
			System.err.println("Bad Address:\t" + ia.getAddress());
		} // else
	}


	/**
	 * IPv4 Addresses
	 * 
	 * @return the iPv4Addresses
	 */
	public Map< Inet4Address, Tuple< String, String > > getIpV4Addresses ()
	{
		return ipV4Addresses;
	}


	/**
	 * IPv6 Addresses
	 * 
	 * @return the iPv6Addresses
	 */
	public Map< Inet6Address, Tuple< String, String > > getIpV6Addresses ()
	{
		return ipV6Addresses;
	}


	/**
	 * IPv4 Loopback addresses
	 * 
	 * @return the iPv4Loopback
	 */
	public Set< Tuple< String, String > > getIpV4Loopback ()
	{
		return ipV4Loopback;
	}


	/**
	 * IPv6 Loopback addresses
	 * 
	 * @return the iPv6Loopback
	 */
	public Set< Tuple< String, String > > getIpV6Loopback ()
	{
		return ipV6Loopback;
	}


	/**
	 * IPv6 LinkLocal addresses
	 * 
	 * @return the iPv6LinkLocal
	 */
	public Set< Tuple< String, String > > getIpV6LinkLocal ()
	{
		return ipV6LinkLocal;
	}


	/**
	 * All network interfaces
	 * 
	 * @return the Interfaces
	 */
	public Set< Tuple< String, String > > getInterfaces ()
	{
		return interfaces;
	}


	/**
	 * All local addresses
	 * 
	 * @return the local addresses
	 */
	public Set< String > getLocalAddresses ()
	{
		return localAddresses;
	}


	/**
	 * Returns a digest of all local network interfaces
	 */
	@Override
	public String toString ()
	{
		return digest;
	} // toString


	/**
	 * Return a {@code String} MAC address
	 * 
	 * @param hardwareAddress
	 *                            the byte representation of the MAC
	 * 
	 * @return the MAC address string
	 */
	private static String stringMAC ( byte[] hardwareAddress )
	{
		if ( hardwareAddress == null || hardwareAddress.length == 0 )
			return "";

		StringBuilder sb = new StringBuilder(String.format("%02X", hardwareAddress[0]));

		for ( int i = 1; i < hardwareAddress.length; i++ )
		{
			sb.append(":").append(String.format("%02X", hardwareAddress[i]));
		} // for
		return sb.toString();
	} // stringMAC

} // LocalInterface
