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

import static org.testng.Assert.assertEquals;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;


public class CLIReaderTest
{
	static String LOCALIP			= "10.10.10.10";
	static Set< String > LOCALIPSET	= new HashSet<>(Arrays.asList( LOCALIP, "0:0:0:0:0:0:0:1", "127.0.0.1" ));
	static String URL_ADDR_GOOD 	= "https://github.com/";
	static URL URL_GOOD;
	{ 
		try {
			URL_GOOD = new URL(URL_ADDR_GOOD);
		} catch (MalformedURLException e) 
		{
			e.printStackTrace();
			org.testng.Assert.fail();
		}
	}

	/**
	 * CLIReader clireader, 
	 * boolean valid, 
	 * boolean help, 
	 * boolean ipv4, 
	 * boolean ipv6, 
	 * boolean local,
	 * boolean quiet,
	 * boolean json,
	 * int ports, 
	 * int hosts, 
	 * int ipv4s,  
	 * int ipv6s,
	 * byte attempts,
	 * URL url
	 * @return 
	 */
	@DataProvider
	public static Object[][] dataMethod ()
	{
		return new Object[][]
		{
			{10,
					new CLIReader(LOCALIPSET, new String[] {}), false, false, false, false,false,false, false,0, 0, 0, 0,CLIReader.CONNECTS_DEFAULT,null
			},
			{11,
					new CLIReader(LOCALIPSET, new String[]
					{
							""
					}), false, false, false, false,false,false,false, 0, 0, 0, 0,CLIReader.CONNECTS_DEFAULT,null
			},
			{12,
					new CLIReader(LOCALIPSET, new String[]
					{
							"-z"
					}), false, false, false, false, false,false,false,0, 0, 0, 0,CLIReader.CONNECTS_DEFAULT,null
			},
			{13,
					new CLIReader(LOCALIPSET, new String[]
					{
							"-i"
					}), false, false, false, false,false, false,false,0, 0, 0, 0,CLIReader.CONNECTS_DEFAULT,null
			},
			{14,
					new CLIReader(LOCALIPSET, new String[]
					{
							"-i", "-i"
					}), false, false, false, false, false,false,false,0, 0, 0, 0,CLIReader.CONNECTS_DEFAULT,null
			},
			{15,
					new CLIReader(LOCALIPSET, new String[]
					{
							"-i", "-p"
					}), false, false, false, false,false,false, false,0, 0, 0, 0,CLIReader.CONNECTS_DEFAULT,null
			},
			{16,
					new CLIReader(LOCALIPSET, new String[]
					{
							"-i", "-p", "-p"
					}), false, false, false, false,false,false, false,0, 0, 0, 0,CLIReader.CONNECTS_DEFAULT,null
			},
			{17,
					new CLIReader(LOCALIPSET, new String[]
					{
							"-i", "3"
					}), false, false, false, false,false,false,false, 0, 0, 0, 0,CLIReader.CONNECTS_DEFAULT,null
			},
			{18,
					new CLIReader(LOCALIPSET, new String[]
					{
							"-i", "4"
					}), false, false, true, false, false,false,false,0, 0, 0, 0,CLIReader.CONNECTS_DEFAULT,null
			},
			{20,
					new CLIReader(LOCALIPSET, new String[]
					{
							"-i", "6"
					}), false, false, false, true,false,false, false,0, 0, 0, 0,CLIReader.CONNECTS_DEFAULT,null
			},
			{21,
					new CLIReader(LOCALIPSET, new String[]
					{
							"-i", "4", "6"
					}), false, false, true, true,false,false, false,0, 0, 0, 0,CLIReader.CONNECTS_DEFAULT,null
			},
			{22,
					new CLIReader(LOCALIPSET, new String[]
					{
							"-i", "4", "6", "9"
					}), false, false, true, true, false,false,false,0, 0, 0, 0,CLIReader.CONNECTS_DEFAULT,null
			},
			{23,
					new CLIReader(LOCALIPSET, new String[]
					{
							"-p", "999"
					}), false, false, false, false,false, false,false,1, 0, 0, 0,CLIReader.CONNECTS_DEFAULT,null
			},
			{24,
					new CLIReader(LOCALIPSET, new String[]
					{
							"-p", "999", "999", "999"
					}), false, false, false, false,false,false, false,1, 0, 0, 0,CLIReader.CONNECTS_DEFAULT,null
			},
			{25,
					new CLIReader(LOCALIPSET, new String[]
					{
							"-p", "999", "999", "22"
					}), false, false, false, false, false,false,false,2, 0, 0, 0,CLIReader.CONNECTS_DEFAULT,null
			},
			{26,
					new CLIReader(LOCALIPSET, new String[]
					{
							"-p", "-2224"
					}), false, false, false, false, false,false,false,0, 0, 0, 0,CLIReader.CONNECTS_DEFAULT,null
			},	
			{27,
					new CLIReader(LOCALIPSET, new String[]
					{
							"-p", "2299-2224"
					}), false, false, false, false,false, false,false,0, 0, 0, 0,CLIReader.CONNECTS_DEFAULT,null
			},
			{28,
					new CLIReader(LOCALIPSET, new String[]
					{
							"-p", "2222-2225"
					}), false, false, false, false,false,false, false,4, 0, 0, 0,CLIReader.CONNECTS_DEFAULT,null
			},
			{29,
					new CLIReader(LOCALIPSET, new String[]
					{
							"-h", "name1"
					}), false, false, false, false, false,false,false,0, 1, 0, 0,CLIReader.CONNECTS_DEFAULT,null
			},
			{30,
					new CLIReader(LOCALIPSET, new String[]
					{
							"-h", "name1", "name1", "name1"
					}), false, false, false, false, false,false,false,0, 1, 0, 0,CLIReader.CONNECTS_DEFAULT,null
			},
			{31,
					new CLIReader(LOCALIPSET, new String[]
					{
							"-h", "name1", "name1", "name2"
					}), false, false, false, false,false, false, false,0, 2, 0, 0,CLIReader.CONNECTS_DEFAULT,null
			},
			{32,
					new CLIReader(LOCALIPSET, new String[]
					{
							"-h", "name1"
					}), false, false, false, false,false,false, false,0, 1, 0, 0,CLIReader.CONNECTS_DEFAULT,null
			},
			{33,
					new CLIReader(LOCALIPSET, new String[]
					{
							"-h", LOCALIP, "-i", "4"
					}), false, false, true, false,false, false,false,0, 0, 1, 0,CLIReader.CONNECTS_DEFAULT,null
			},
			{34,
					new CLIReader(LOCALIPSET, new String[]
					{
							"-h", LOCALIP, "-p", "22", "23"
					}), true, false, true, true, false,false,false,2, 0, 0, 0,CLIReader.CONNECTS_DEFAULT,null
			},
			{35,
					new CLIReader(LOCALIPSET, new String[]
					{
							"-i", "4", "-h", LOCALIP, "-p", "22",
					}), true, false, true, false, false, false, false, 1, 0, 0, 0,CLIReader.CONNECTS_DEFAULT,null
			},
			{36,
					new CLIReader(LOCALIPSET, new String[]
					{
							"-i", "4", "-h", LOCALIP, "localhost", "-p", "22",
					}), true, false, true, false, false, false, false,1, 0, 1, 0,CLIReader.CONNECTS_DEFAULT,null
			},
			{37,
					new CLIReader(LOCALIPSET, new String[]
					{
							"-i", "4", "-h", LOCALIP, "0:0:0:0:0:0:0:1", "-p", "22", "22",
					}), true, false, true, false, false, false, false,1, 0, 0, 0,CLIReader.CONNECTS_DEFAULT,null
			},
			{38,
					new CLIReader(LOCALIPSET, new String[]
					{
							"-i", "6", "4", "-h", LOCALIP, "0:0:0:0:0:0:0:1", "-p", "22", "22",
					}), true, false, true, true, false, false, false,1, 0, 0, 0,CLIReader.CONNECTS_DEFAULT,null
			},
			{39,
					new CLIReader(LOCALIPSET, new String[]
					{
							"-h", "0:0:0:0:0:0:0:1", "-p", "22", "23",
					}), true, false, true, true, false, false, false,2, 0, 0, 0,CLIReader.CONNECTS_DEFAULT,null
			},
			{40,
					new CLIReader(LOCALIPSET, new String[]
					{
							"-h", "badaddress", "0:0:0:0:0:0:0:1", "-p", "22", "23", "-l",
					}), false, false, false, false, true, false, false,2, 1, 0, 1,CLIReader.CONNECTS_DEFAULT,null
			},				
			{41,
					new CLIReader(LOCALIPSET, new String[]
					{
							"-h", LOCALIP, "-p", "22", "23", "-l",
					}), true, false, true, true, true, false, false,2, 0, 1, 0,CLIReader.CONNECTS_DEFAULT,null
			},
			{42,
					new CLIReader(LOCALIPSET, new String[]
					{
							"-i", "4", "-h", LOCALIP, "-p", "22", "-l",
					}), true, false, true, false, true, false, false, 1, 0, 1, 0,CLIReader.CONNECTS_DEFAULT,null
			},
			{43,
					new CLIReader(LOCALIPSET, new String[]
					{
							"-i", "4", "-h", LOCALIP, "localhost", "-p", "22", "-l",
					}), true, false, true, false, true, false,false,1, 0, 2, 0,CLIReader.CONNECTS_DEFAULT,null
			},
			{44,
					new CLIReader(LOCALIPSET, new String[]
					{
							"-i", "4", "-h", "localhost", "0:0:0:0:0:0:0:1", "-p", "22", "22", "-l",
					}), true, false, true, false, true, false, false,1, 0, 1, 1,CLIReader.CONNECTS_DEFAULT,null
			},
			{45,
					new CLIReader(LOCALIPSET, new String[]
					{
							"-i", "6", "4", "-h", "localhost", "0:0:0:0:0:0:0:1", "-p", "22", "22", "-l",
					}), true, false, true, true, true,false, false,1, 0, 1, 1,CLIReader.CONNECTS_DEFAULT,null
			},
			{46,
					new CLIReader(LOCALIPSET, new String[]
					{
							"-h", "0:0:0:0:0:0:0:1", "-p", "22", "23", "-l",
					}), true, false, true, true, true, false, false,2, 0, 0, 1,CLIReader.CONNECTS_DEFAULT,null
			},
			{50,
					new CLIReader(LOCALIPSET, new String[]
					{
							"-h", "badaddress", "0:0:0:0:0:0:0:1", "-p", "22", "23", "-?"
					}), false, true, false, false, false,false,false,0, 0, 0, 0,CLIReader.CONNECTS_DEFAULT,null
			},
			{51,
					new CLIReader(LOCALIPSET, new String[]
					{
							"22", "23", "-?"
					}), false, true, false, false, false,false,false,0, 0, 0, 0,CLIReader.CONNECTS_DEFAULT,null
			},
			{52,
					new CLIReader(LOCALIPSET, new String[]
					{
							"-?", "22", "23"
					}), false, true, false, false, false,false,false,0, 0, 0, 0,CLIReader.CONNECTS_DEFAULT,null
			},
			{53,
					new CLIReader(LOCALIPSET, new String[]
					{
							"-?"
					}), false, true, false, false, false,false,false,0, 0, 0, 0,CLIReader.CONNECTS_DEFAULT,null
			},
			{63,
					new CLIReader(LOCALIPSET, new String[]
					{
							"-q"
					}), false, false, false, false, false ,true, false,0, 0, 0, 0,CLIReader.CONNECTS_DEFAULT,null
			},
			{64,
					new CLIReader(LOCALIPSET, new String[]
					{
							"-q", "-q"
					}), false, false, false, false, false,true,false,0, 0, 0, 0,CLIReader.CONNECTS_DEFAULT,null
			},
			{65,
					new CLIReader(LOCALIPSET, new String[]
					{
							"-l"
					}), false, false, false, false, true, false, false,0, 0, 0, 0,CLIReader.CONNECTS_DEFAULT,null
			},
			{66,
					new CLIReader(LOCALIPSET, new String[]
					{
							"-l", "-l"
					}), false, false, false, false, true, false, false,0, 0, 0, 0,CLIReader.CONNECTS_DEFAULT,null
			},
			{67,
					new CLIReader(LOCALIPSET, new String[]
					{
							"-l", "-q"
					}), false, false, false, false, true, true, false,0, 0, 0, 0, CLIReader.CONNECTS_DEFAULT, null
			},
			{68,
					new CLIReader(LOCALIPSET, new String[]
					{
						"-l", "-q", "-h", "localhost", "-p", "80"
					}), true, false, true, true, true, true, false, 1, 0, 1, 0, CLIReader.CONNECTS_DEFAULT, null
			},
			{69,
					new CLIReader(LOCALIPSET, new String[]
					{
						"-l", "-j", "-h", "localhost", "-p", "80"
					}), true, false, true, true, true, false, true, 1, 0, 1, 0, CLIReader.CONNECTS_DEFAULT, null
			},
			{70,
					new CLIReader(LOCALIPSET, new String[]
					{
						"-l", "-a", "-j", "-h", "localhost", "-p", "80"
					}), false, false, false, false, true, false, true, 1, 0, 1, 0, CLIReader.CONNECTS_DEFAULT, null
			},
			{71,
					new CLIReader(LOCALIPSET, new String[]
					{
						"-l", "-j", "-h", "localhost", "-p", "80", "-a", "0"
					}), false, false, false, false, true, false, true, 1, 0, 1, 0, CLIReader.CONNECTS_DEFAULT, null
			},
			{72,
					new CLIReader(LOCALIPSET, new String[]
					{
						"-l", "-j", "-h", "localhost", "-p", "80", "-a", "100"
					}), false, false, false, false, true, false, true, 1, 0, 1, 0, CLIReader.CONNECTS_DEFAULT, null
			},
			{73,
					new CLIReader(LOCALIPSET, new String[]
					{
						"-l", "-j", "-h", "localhost", "-p", "80", "-a", "-10"
					}), false, false, false, false, true, false, true, 1, 0, 1, 0, CLIReader.CONNECTS_DEFAULT, null
			},
			{74,
					new CLIReader(LOCALIPSET, new String[]
					{
						"-l", "-j", "-h", "localhost", "-p", "80", "-a", "64"
					}), true, false, true, true, true, false, true, 1, 0, 1, 0, 64, null
			},
			{75,
					new CLIReader(LOCALIPSET, new String[]
					{
						"-l", "-j", "-q", "-h", "localhost", "-p", "80", "-a", "32"
					}), true, false, true, true, true, true, true, 1, 0, 1, 0, 32, null
			},
			{80,
					new CLIReader(LOCALIPSET, new String[]
					{
						"-l", "-j", "-h", "localhost", "-p", "80", "-u"
					}), true, false, true, true, true, false, true, 1, 0, 1, 0, CLIReader.CONNECTS_DEFAULT, null
			},
			{81,
					new CLIReader(LOCALIPSET, new String[]
					{
						"-l", "-j", "-h", "localhost", "-p", "80", "-u", "-u"
					}), false, false, false, false, true, false, true, 1, 0, 1, 0, CLIReader.CONNECTS_DEFAULT, null
			},
			{82,
					new CLIReader(LOCALIPSET, new String[]
					{
						"-l", "-j", "-h", "localhost", "-p", "80", "-u", "abc"
					}), false, false, false, false, true, false, true, 1, 0, 1, 0, CLIReader.CONNECTS_DEFAULT, null
			},
			{83,
					new CLIReader(LOCALIPSET, new String[]
					{
						"-l", "-j", "-h", "localhost", "-p", "80", "-u", "abc"
					}), false, false, false, false, true, false, true, 1, 0, 1, 0, CLIReader.CONNECTS_DEFAULT, null
			},
			{84,
					new CLIReader(LOCALIPSET, new String[]
					{
						"-l", "-j", "-h", "localhost", "-p", "80", "-u", URL_ADDR_GOOD
					}), true, false, true, true, true, false, true, 1, 0, 1, 0, CLIReader.CONNECTS_DEFAULT, URL_GOOD 
			},
			{85,
					new CLIReader(LOCALIPSET, new String[]
					{
						"-l", "-j", "-h", "localhost", "-p", "80", "-d", "-u", URL_ADDR_GOOD
					}), true, false, true, true, true, false, true, 1, 0, 1, 0, 0, URL_GOOD 
			},
		};
	} // dataMethod 


	@Test ( dataProvider = "dataMethod" )
	public void cliReaderTest (int testno,
			CLIReader clireader, 
			boolean valid, boolean help, boolean ipv4, boolean ipv6, boolean local, boolean quiet, boolean json, 
			int ports, int hosts, int ipv4s, int ipv6s, int attempts, 
			URL url
	) throws IOException
	{
		System.out.print(clireader.getFeedback());
		assertEquals(clireader.isValid(), valid, testno+": Validity flag");
		assertEquals(clireader.wantHelp(), help, testno+": Help flag");
		assertEquals(clireader.isIPv4Target(), ipv4, testno+": IPv4 flag");
		assertEquals(clireader.isIPv6Target(), ipv6, testno+": IPv6 flag");
		assertEquals(clireader.isLocalIncluded (), local, testno+": Local flag");
		assertEquals(clireader.isQuiet (), quiet, testno+": Quiet flag");
		assertEquals(clireader.isJson (), json, testno+": JSON flag");
		assertEquals(clireader.getPorts().size(), ports, testno+": Port count mismatch");
		assertEquals(clireader.getBadHosts().size(), hosts, testno+": Bad Host Name count mismatch");
		assertEquals(clireader.getAttempts(), attempts, testno+": Attempts mismatch");		
		assertEquals(clireader.getIpV4Addresses().size(), ipv4s, testno+": IPv4 Address count mismatch");

		/* Different platforms produce different variations on IPv6 - commenting out this check */
		//assertEquals(clireader.getIpV6Addresses().size(), ipv6s, testno+": IPv6 Address count mismatch");

		if ( url == null )
		{
			assertEquals( clireader.getHttpUri(), null, testno+": URL mismatch");
		}
		else
		{
			assertEquals( clireader.getHttpUri().toURL(), url , testno+": URL mismatch");
		}
	}
}
