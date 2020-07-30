package com.github.technosf.connectomatic;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class CLIReaderTest
{

	@DataProvider
	public static Object[][] dataMethod ()
	{
		return new Object[][]
		{
				{
						new CLIReader(new String[] {}), false, false, false, false, 0, 0, 0, 0
				},
				{
						new CLIReader(new String[]
						{
								""
						}), false, false, false, false, 0, 0, 0, 0
				},
				{
						new CLIReader(new String[]
						{
								"-z"
						}), false, false, false, false, 0, 0, 0, 0
				},
				{
						new CLIReader(new String[]
						{
								"-i"
						}), false, false, false, false, 0, 0, 0, 0
				},
				{
						new CLIReader(new String[]
						{
								"-i", "-i"
						}), false, false, false, false, 0, 0, 0, 0
				},
				{
						new CLIReader(new String[]
						{
								"-i", "-p"
						}), false, false, false, false, 0, 0, 0, 0
				},
				{
						new CLIReader(new String[]
						{
								"-i", "-p", "-p"
						}), false, false, false, false, 0, 0, 0, 0
				},
				{
						new CLIReader(new String[]
						{
								"-i", "3"
						}), false, false, false, false, 0, 0, 0, 0
				},
				{
						new CLIReader(new String[]
						{
								"-i", "4"
						}), false, false, true, false, 0, 0, 0, 0
				},
				{
						new CLIReader(new String[]
						{
								"-i", "6"
						}), false, false, false, true, 0, 0, 0, 0
				},
				{
						new CLIReader(new String[]
						{
								"-i", "4", "6"
						}), false, false, true, true, 0, 0, 0, 0
				},
				{
						new CLIReader(new String[]
						{
								"-i", "4", "6", "9"
						}), false, false, true, true, 0, 0, 0, 0
				},
				{
						new CLIReader(new String[]
						{
								"-p", "999"
						}), false, false, false, false, 1, 0, 0, 0
				},
				{
						new CLIReader(new String[]
						{
								"-p", "999", "999", "999"
						}), false, false, false, false, 1, 0, 0, 0
				},
				{
						new CLIReader(new String[]
						{
								"-p", "999", "999", "22"
						}), false, false, false, false, 2, 0, 0, 0
				},
				{
						new CLIReader(new String[]
						{
								"-h", "name1"
						}), false, false, false, false, 0, 1, 0, 0
				},
				{
						new CLIReader(new String[]
						{
								"-h", "name1", "name1", "name1"
						}), false, false, false, false, 0, 1, 0, 0
				},
				{
						new CLIReader(new String[]
						{
								"-h", "name1", "name1", "name2"
						}), false, false, false, false, 0, 2, 0, 0
				},
				{
						new CLIReader(new String[]
						{
								"-h", "name1"
						}), false, false, false, false, 0, 1, 0, 0
				},
				{
						new CLIReader(new String[]
						{
								"-h", "localhost", "-i", "4"
						}), false, false, true, false, 0, 0, 1, 0
				},
				{
						new CLIReader(new String[]
						{
								"-h", "localhost", "-p", "22", "23"
						}), true, false, true, true, 2, 0, 1, 0
				},
				{
						new CLIReader(new String[]
						{
								"-i", "4", "-h", "localhost", "-p", "22",
						}), true, false, true, false, 1, 0, 1, 0
				},
				{
						new CLIReader(new String[]
						{
								"-i", "4", "-h", "localhost", "localhost", "-p", "22",
						}), true, false, true, false, 1, 0, 1, 0
				},
				{
						new CLIReader(new String[]
						{
								"-i", "4", "-h", "localhost", "::1", "-p", "22", "22",
						}), true, false, true, false, 1, 0, 1, 1
				},
				{
						new CLIReader(new String[]
						{
								"-i", "6", "4", "-h", "localhost", "::1", "-p", "22", "22",
						}), true, false, true, true, 1, 0, 1, 1
				},
				{
						new CLIReader(new String[]
						{
								"-h", "::1", "-p", "22", "23",
						}), true, false, true, true, 2, 0, 0, 1
				},
				{
						new CLIReader(new String[]
						{
								"-h", "badaddress", "::1", "-p", "22", "23",
						}), false, false, false, false, 2, 1, 0, 1
				},
				{
						new CLIReader(new String[]
						{
								"-h", "badaddress", "::1", "-p", "22", "23", "-?"
						}), false, true, false, false, 0, 0, 0, 0
				},
				{
						new CLIReader(new String[]
						{
								"22", "23", "-?"
						}), false, true, false, false, 0, 0, 0, 0
				},
				{
						new CLIReader(new String[]
						{
								"-?", "22", "23"
						}), false, true, false, false, 0, 0, 0, 0
				},
				{
						new CLIReader(new String[]
						{
								"-?"
						}), false, true, false, false, 0, 0, 0, 0
				},
		};

	}


	@Test ( dataProvider = "dataMethod" )
	public void cliReaderTest (
			CLIReader clireader, boolean valid, boolean help, boolean ipv4, boolean ipv6, int ports, int hosts,
			int ipv4s, int ipv6s
	)
	{
		System.out.print(clireader.getFeedback());
		assertEquals(clireader.isValid(), valid, "Validity flag");
		assertEquals(clireader.wantHelp(), help, "Help flag");
		assertEquals(clireader.isIPv4Target(), ipv4, "IPv4 flag");
		assertEquals(clireader.isIPv6Target(), ipv6, "IPv6 flag");
		assertEquals(clireader.getPorts().size(), ports, "Port count mismatch");
		assertEquals(clireader.getBadHosts().size(), hosts, "Bad Host Name count mismatch");
		assertEquals(clireader.getIPv4Addresses().size(), ipv4s, "IPv4 Address count mismatch");
		assertEquals(clireader.getIPv6Addresses().size(), ipv6s, "IPv6 Address count mismatch");

	}

}
