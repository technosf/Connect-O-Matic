# Connect-O-Matic
Java CLI to test TCP reachability from local interfaces to a set of hosts over a range of ports. For firewall rule testing etc.

Run on a given host, this Java executable _.jar_ identifies the host name, and the available external network interfaces. Provided with a list of hosts to try connecting to and a range of ports, it will attempt to connect with TCP from each local interface to each combination of external host and port.
It reports on connection success and failures for each combination.
In this way each potential route from the localhost to a set of external hosts, say, through a firewall, can be confirmed.


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