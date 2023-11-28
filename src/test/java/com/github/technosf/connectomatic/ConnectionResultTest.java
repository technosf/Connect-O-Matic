package com.github.technosf.connectomatic;

import static org.testng.Assert.assertEquals;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class ConnectionResultTest 
{
    private  static InetAddress HOST_ADDR;
    private  static String CSV_FMT1, CSV_FMT2, JSON_FMT1, JSON_FMT2;
    // private  static String HOST_NAME;

    @BeforeClass
    public void beforeClass() 
        throws UnknownHostException
    {
        HOST_ADDR = InetAddress.getLocalHost();
        CSV_FMT1 = String.format("%%s,%1$s,%1$s,%2$s,%%d", HOST_ADDR.getHostAddress(),HOST_ADDR.getHostName());
        CSV_FMT2 = String.format("%%s,%1$s,%1$s,%2$s,%%d,%%d,%%.1f,%%.1f,%%.1f,%%d,%%.1f,%%d,%%d", HOST_ADDR.getHostAddress(),HOST_ADDR.getHostName());
        JSON_FMT1 = String.format("{\"IPv\":\"%%s\",\"Interface\":\"%1$s\",\"Remote Address\":\"%1$s\",\"Remote Hostname\":\"%2$s\",\"Remote Port\":%%d", HOST_ADDR.getHostAddress(),HOST_ADDR.getHostName());
        JSON_FMT2 = String.format("{\"IPv\":\"%%s\",\"Interface\":\"%1$s\",\"Remote Address\":\"%1$s\",\"Remote Hostname\":\"%2$s\",\"Remote Port\":%%d,\"Connections\":%%d,\"Connection μs Avg\":%%f,\"Connection μs Min\":%%f,\"Connection μs Max\":%%f,\"Timeouts\":%%d,\"Timeout μs Avg\":%%f,\"Refused\":%%d,\"Unreachable\":%%d}", HOST_ADDR.getHostAddress(),HOST_ADDR.getHostName());
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
            { 10, "", 80 },    
            { 20, "4", 8080 },    
            { 30, "6", 9999 },                
        };
    }

    @Test ( dataProvider = "dataMethod" )
    void testCollateCSV(int testno, String ipv, int port ) 
    {
        ConnectionResult aut = new ConnectionResult(false, ipv, HOST_ADDR, HOST_ADDR, port); //, ping);

		assertEquals(aut.toString(), String.format(CSV_FMT1, ipv, port), testno+": pre-collate");
        aut.connects_millis.add(1f);
        aut.connects_millis.add(1f);
        aut.connects_millis.add(7f);
        aut.timeouts_millis.add(5f);
        aut.collate();
		assertEquals(aut.toString(),  String.format(CSV_FMT2, ipv, port,3,3f,1f,7f,1,5f,0,0), testno+": post-collate");
        aut.connects_millis.add(99f);
        aut.timeouts_millis.add(95f);
		assertEquals(aut.toString(),  String.format(CSV_FMT2, ipv, port,3,3f,1f,7f,1,5f,0,0), testno+": post-collate");
    }

    @Test ( dataProvider = "dataMethod" )
    void testCollateJSON(int testno, String ipv, int port ) 
    {
        ConnectionResult aut = new ConnectionResult(true, ipv, HOST_ADDR, HOST_ADDR, port);

		assertEquals(aut.toString(), String.format(JSON_FMT1, ipv, port), testno+": pre-collate");
        aut.connects_millis.add(1f);
        aut.connects_millis.add(1f);
        aut.connects_millis.add(7f);
        aut.timeouts_millis.add(5f);
        aut.collate();
	    assertEquals(aut.toString(), String.format(JSON_FMT2, ipv, port,3,3f,1f,7f,1,5f,0,0), testno+": post-collate");
        aut.connects_millis.add(99f);
        aut.timeouts_millis.add(95f);
	    assertEquals(aut.toString(),  String.format(JSON_FMT2, ipv, port,3,3f,1f,7f,1,5f,0,0), testno+": post-collate");
        }
}
