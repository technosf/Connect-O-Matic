# Connect-O-Matic
Java CLI to test TCP reachability from local interfaces to a set of hosts over a range of ports. For firewall rule testing etc.

Run on a given host, this Java executable _.jar_ identifies the host name, and the available external network interfaces. Provided with a list of hosts to try connecting to and a range of ports, it will attempt to connect with TCP from each local interface to each combination of external host and port.
It reports on connection success and failures for each combination.
In this way each potential route from the localhost to a set of external hosts, say, through a firewall, can be confirmed.

## Package and Help
_Connect-O-Matic_ is coded to be packaged as an executable _.jar_ and run from the CLI. It's writing with _Maven_ packaging, because I find _Maven_ to be easier than _gradle_. Download the source and, to build and get help:

```console
technosf@github:connectomatic~$ mvn package
technosf@github:connectomatic~$ java -jar target/connectomatic-1.0.1.jar -?	

Connect-O-Matic		Version: 1.0.1

Help:
	-i	IPv - 4 and/or 6, defaults to 4 and 6 if absent
	-p	Port numbers, at least one required
	-h	Hosts as hostnames, IPv4 or IPv6 addresses, at least one required
	-?	Produces this message

Examples:
	java -jar connectomatic-*.*.*.jar -p 22 80 -h github.com www.github.com
	java -jar connectomatic-*.*.*.jar -i 4,6 -p 22,80 -h github.com,www.github.com

Output is .csv, with header. Fields are:
	• IPv
	• Local Interface
	• Remote Address
	• Remote Hostname
	• Remote Port
	• Connections
	• Connection μs Avg
	• Timeouts
	• Timeout μs Avg
	• Refused connection count
	• Unreachable network count

```
	
## Local Interfaces
To identify local interfaces, invoke with no arguments:	

```console
technosf@github:connectomatic~$ java -jar target/connectomatic-1.0.0.jar

Connect-O-Matic		Version: 1.0.0

Local Interfaces: 2020-08-07T14:26:38.114617

Interfaces:
		lo                              	
		wlp1s1                          	DE:AD:BE:ED:01:23A

Loopback Addresses:
	IPv4
		localhost                       	127.0.0.1
	IPv6
		ip6-localhost                   	0:0:0:0:0:0:0:1%lo

LinkLocal Addresses IPv6:
		fe80:0:0:::1a921%wlp1s1	fe80:0:0:::1a921%wlp1s1

IPv4 Addresses:
		192.168.0.99                    	192.168.0.99
```
## Connection Test
To test an example connection:

```console
technosf@github:connectomatic~$ java -jar target/connectomatic-1.0.1.jar -h github.com -p 22 80 -i 4

Connect-O-Matic		Version: 1.0.0

"IPv","Interface","Remote Address","Remote Hostname","Remote Port","Connections","Connection μs Avg","Timeouts","Timeout μs Avg","Refused","Unreachable"
4,192.168.0.99,140.82.113.4,github.com,22,5,148.05893249511718,0,0.0,0,0
4,192.168.0.99,140.82.113.4,github.com,80,5,141.97718963623046,0,0.0,0,0

```
Output is in **CSV** format, repleat with column header: It can be copied and  _paste/special_  directly into [LibreOffice Calc](https://www.libreoffice.org/) or saved as a  _.csv_  and opened with spreadsheet.

## License

Connect-O-Matic - IP network connection tester

Copyright 2020  technosf  [http://github.com/technosf]

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
https://www.gnu.org/licenses/gpl-3.0.en.html
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
![glp3 logo](https://www.gnu.org/graphics/gplv3-127x51.png)


This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <https://www.gnu.org/licenses/>.