# Connect-O-Matic
Java CLI to test TCP reachability from local interfaces to a set of hosts over a range of ports in an automatic and time efficient manner. For firewall rule testing etc.


## Overview
Run on a given host, this Java executable  _.jar_  identifies the local host name, and each of its the available network interfaces. Provided with a list of hosts to connect to, and a set or range of ports, it will attempt to connect with **TCP** from each local interface to each combination of external host and port in parallel a number of times.
It collates, agregates and reports on connection:
* success  
* timeout  
* refused  
* unreachable 
and timings for each.
In this way each potential route from a local interface to a set of external hosts, say through a firewall, can be confirmed or diagnosed in as short a time as possible.

Output is formated for a spreadsheet as CSV by default, but can be JSON and also POSTed to an object DB such as ElasticSearch.


## Package and Help
_Connect-O-Matic_  is coded to be packaged as an executable  _.jar_  and run from the CLI. It's written with  _Maven_  packaging, because I find  _Maven_  to be easier than _gradle_ . Download the source and, to build and get help:

```console
technosf@github:connectomatic~$ mvn package
technosf@github:connectomatic~$ java -jar target/connectomatic-1.2.0.jar -?	

Connect-O-Matic		Version: 1.2.0

Help:
	-i	IPv - 4 and/or 6, defaults to 4 and 6 if absent
	-p	Port numbers, at least one required
	-h	Hosts as hostnames, IPv4 or IPv6 addresses, at least one required
	-a  Attempts to connect, defaults to 5, but can be 1-64
	-l  Local addresses in the host set should be tested and not ignored
	-j  Produce JSON output instead of CSV
	-u  URI, POST JSON results to the provided URI
	-q  Quiet mode, outputs result only, without preamble or summary
	-?	Produces this message

Examples:
	java -jar connectomatic-*.*.*.jar -p 22 80 -h github.com www.github.com
	java -jar connectomatic-*.*.*.jar -i 4,6 -p 80-90 -h github.com,www.github.com
	java -jar connectomatic-*.*.*.jar -i 4,6 -p 22,80 -h github.com,www.github.com

Output by default is .csv, with header, or JSON via a switch. Fields are:
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
technosf@github:connectomatic~$ java -jar target/connectomatic-1.2.0.jar

Connect-O-Matic		Version: 1.2.0

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
technosf@github:connectomatic~$ java -jar connectomatic-1.2.0.jar -h github.com -p 22 80 -i 4

Connect-O-Matic		Version: 1.2.0

	Summary 	Connects: 10 	Timeouts: 0 	Refused: 0 	Unreachable: 0


"IPv","Interface","Remote Address","Remote Hostname","Remote Port","Connections","Connection μs Avg","Connection μs Min","Connection μs Max","Timeouts","Timeout μs Avg","Refused","Unreachable"
4,192.168.13.48,140.82.114.4,github.com,22,5,320.65453338623047,65.01171112060547,1101.0047607421875,0,0.0,0,0
4,192.168.13.48,140.82.114.4,github.com,80,5,86.8220932006836,63.963134765625,173.01504516601562,0,0.0,0,0

```
Output is in **CSV** format, replete with column header: It can be copied and  _paste/special_  directly into [LibreOffice Calc](https://www.libreoffice.org/) as  _csv_  or saved as a  _.csv_  and opened with a spreadsheet.


## Design Descisions
Coded as Java 8 rather than 14+ to maximise build/use options. Hence no Java Record types which would be the main benefit in the code.


## History
### 1.2.0
Added switch to test connections to/from local interfaces if they are included in the hosts parameter. Otherwise they are now ignored, allowing the same host set to be used accross hosts without redundant results in the output.
Added a switch for the number of connection attemps - defaults to 5.
Added a switch to output results as JSON instead of CSV
Added a switch to POST results to an URI as a JSON payload, i.e. to get the results into an object DB.
Added a switch to quiet version and summary output
### 1.1.1
Support for port ranges in the input parameters. If a consecutive range of ports is to be tested, rather than having to list every port individually, they can be specified with a range: startport-endport
### 1.1.0
Added summary of connection tries to the result header.
Added min and max connection times to .CSV
Corrected an error in the connection time math.
### 1.0.1
_Finessed Initial Release_
Basic discovery of local interfaces and connection testing from a given local interface to remote host and port, providing connection results and average connection timings in .csv format
### 1.0.0
_Initial Release_
Basic discovery of local interfaces and connection testing from a given local interface to remote host and port, providing connection results and average connection timings.


## License

Connect-O-Matic - IP network connection tester

Copyright 2023  technosf  [http://github.com/technosf]

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
