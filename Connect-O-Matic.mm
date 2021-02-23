<map version="freeplane 1.8.0">
<!--To view this file, download free mind mapping software Freeplane from http://freeplane.sourceforge.net -->
<node TEXT="Connect-O-Matic" FOLDED="false" ID="ID_1327246214" CREATED="1595450457113" MODIFIED="1614028722278" STYLE="oval">
<font SIZE="18"/>
<hook NAME="MapStyle">
    <properties edgeColorConfiguration="#808080ff,#ff0000ff,#0000ffff,#00ff00ff,#ff00ffff,#00ffffff,#7c0000ff,#00007cff,#007c00ff,#7c007cff,#007c7cff,#7c7c00ff" show_icon_for_attributes="true" show_note_icons="true" fit_to_viewport="false"/>

<map_styles>
<stylenode LOCALIZED_TEXT="styles.root_node" STYLE="oval" UNIFORM_SHAPE="true" VGAP_QUANTITY="24.0 pt">
<font SIZE="24"/>
<stylenode LOCALIZED_TEXT="styles.predefined" POSITION="right" STYLE="bubble">
<stylenode LOCALIZED_TEXT="default" ICON_SIZE="12.0 pt" COLOR="#000000" STYLE="fork">
<font NAME="SansSerif" SIZE="10" BOLD="false" ITALIC="false"/>
</stylenode>
<stylenode LOCALIZED_TEXT="defaultstyle.details"/>
<stylenode LOCALIZED_TEXT="defaultstyle.attributes">
<font SIZE="9"/>
</stylenode>
<stylenode LOCALIZED_TEXT="defaultstyle.note" COLOR="#000000" BACKGROUND_COLOR="#ffffff" TEXT_ALIGN="LEFT"/>
<stylenode LOCALIZED_TEXT="defaultstyle.floating">
<edge STYLE="hide_edge"/>
<cloud COLOR="#f0f0f0" SHAPE="ROUND_RECT"/>
</stylenode>
</stylenode>
<stylenode LOCALIZED_TEXT="styles.user-defined" POSITION="right" STYLE="bubble">
<stylenode LOCALIZED_TEXT="styles.topic" COLOR="#18898b" STYLE="fork">
<font NAME="Liberation Sans" SIZE="10" BOLD="true"/>
</stylenode>
<stylenode LOCALIZED_TEXT="styles.subtopic" COLOR="#cc3300" STYLE="fork">
<font NAME="Liberation Sans" SIZE="10" BOLD="true"/>
</stylenode>
<stylenode LOCALIZED_TEXT="styles.subsubtopic" COLOR="#669900">
<font NAME="Liberation Sans" SIZE="10" BOLD="true"/>
</stylenode>
<stylenode LOCALIZED_TEXT="styles.important">
<icon BUILTIN="yes"/>
</stylenode>
</stylenode>
<stylenode LOCALIZED_TEXT="styles.AutomaticLayout" POSITION="right" STYLE="bubble">
<stylenode LOCALIZED_TEXT="AutomaticLayout.level.root" COLOR="#000000" STYLE="oval" SHAPE_HORIZONTAL_MARGIN="10.0 pt" SHAPE_VERTICAL_MARGIN="10.0 pt">
<font SIZE="18"/>
</stylenode>
<stylenode LOCALIZED_TEXT="AutomaticLayout.level,1" COLOR="#0033ff">
<font SIZE="16"/>
</stylenode>
<stylenode LOCALIZED_TEXT="AutomaticLayout.level,2" COLOR="#00b439">
<font SIZE="14"/>
</stylenode>
<stylenode LOCALIZED_TEXT="AutomaticLayout.level,3" COLOR="#990000">
<font SIZE="12"/>
</stylenode>
<stylenode LOCALIZED_TEXT="AutomaticLayout.level,4" COLOR="#111111">
<font SIZE="10"/>
</stylenode>
<stylenode LOCALIZED_TEXT="AutomaticLayout.level,5"/>
<stylenode LOCALIZED_TEXT="AutomaticLayout.level,6"/>
<stylenode LOCALIZED_TEXT="AutomaticLayout.level,7"/>
<stylenode LOCALIZED_TEXT="AutomaticLayout.level,8"/>
<stylenode LOCALIZED_TEXT="AutomaticLayout.level,9"/>
<stylenode LOCALIZED_TEXT="AutomaticLayout.level,10"/>
<stylenode LOCALIZED_TEXT="AutomaticLayout.level,11"/>
</stylenode>
</stylenode>
</map_styles>
</hook>
<hook NAME="AutomaticEdgeColor" COUNTER="5" RULE="ON_BRANCH_CREATION"/>
<node TEXT="Objects" POSITION="left" ID="ID_57923445" CREATED="1595451917869" MODIFIED="1614028720327" HGAP_QUANTITY="-88.76538009627875 pt" VSHIFT_QUANTITY="-139.4702809542715 pt">
<edge COLOR="#ff00ff"/>
<node TEXT="NetworkInterface" ID="ID_741906182" CREATED="1595451936898" MODIFIED="1595452821860"><richcontent TYPE="NOTE">

<html>
  <head>
    
  </head>
  <body>
    <p>
      <font size="1">A name and a list of IP addresses assigned to this interface</font>
    </p>
  </body>
</html>
</richcontent>
<node TEXT="getName()" ID="ID_231424967" CREATED="1595451948348" MODIFIED="1595451956341"/>
<node TEXT="getInetAddresses()" ID="ID_510011049" CREATED="1595451959468" MODIFIED="1614028780906" LINK="#ID_1346861719">
<arrowlink SHAPE="CUBIC_CURVE" COLOR="#000000" WIDTH="2" TRANSPARENCY="200" FONT_SIZE="8" FONT_FAMILY="SansSerif" DESTINATION="ID_1346861719" STARTINCLINATION="429;0;" ENDINCLINATION="405;-111;" STARTARROW="NONE" ENDARROW="DEFAULT"/>
</node>
<node TEXT="getInterface Addresses()" ID="ID_519491420" CREATED="1595451966768" MODIFIED="1614028753780" LINK="#ID_74228571">
<arrowlink SHAPE="CUBIC_CURVE" COLOR="#000000" WIDTH="2" TRANSPARENCY="200" FONT_SIZE="8" FONT_FAMILY="SansSerif" DESTINATION="ID_74228571" STARTINCLINATION="-1;13;" ENDINCLINATION="25;-12;" STARTARROW="NONE" ENDARROW="DEFAULT"/>
</node>
</node>
<node TEXT="InterfaceAddress" ID="ID_74228571" CREATED="1595452050634" MODIFIED="1595452898120" HGAP_QUANTITY="109.64963437066716 pt" VSHIFT_QUANTITY="18.65693417669607 pt"><richcontent TYPE="NOTE">

<html>
  <head>
    
  </head>
  <body>
    <p>
      <font size="1">IP address, a subnet maskand a broadcast address when the IP address is IPv4. </font>
    </p>
    <p>
      <font size="1">An IP address and network prefix length in the case of an IPv6 address.</font>
    </p>
  </body>
</html>
</richcontent>
<node TEXT="getNetworkPrefixLength()" ID="ID_1029871683" CREATED="1595452110389" MODIFIED="1595452201685" HGAP_QUANTITY="17.15328464958243 pt" VSHIFT_QUANTITY="-2.890510928783897 pt"/>
<node TEXT="getAddress()" ID="ID_1169376656" CREATED="1595452067089" MODIFIED="1595452433269" LINK="#ID_1346861719" HGAP_QUANTITY="20.83211674076194 pt" VSHIFT_QUANTITY="8.145985344754619 pt">
<arrowlink SHAPE="CUBIC_CURVE" COLOR="#000000" WIDTH="2" TRANSPARENCY="200" FONT_SIZE="8" FONT_FAMILY="SansSerif" DESTINATION="ID_1346861719" STARTINCLINATION="-41;51;" ENDINCLINATION="-102;-298;" STARTARROW="NONE" ENDARROW="DEFAULT"/>
</node>
</node>
<node TEXT="InetAddress" ID="ID_1346861719" CREATED="1595452220566" MODIFIED="1595452893605" HGAP_QUANTITY="116.21897739063054 pt" VSHIFT_QUANTITY="58.86131345887209 pt"><richcontent TYPE="NOTE">

<html>
  <head>
    
  </head>
  <body>
    <p>
      32-bit or 128-bit unsigned number used by IP
    </p>
  </body>
</html>
</richcontent>
<node TEXT="Inet4Address" ID="ID_386782478" CREATED="1595452232119" MODIFIED="1595452243774"/>
<node TEXT="Inet6Address" ID="ID_838273324" CREATED="1595452244709" MODIFIED="1595452250041"/>
</node>
</node>
<node TEXT="Process" POSITION="right" ID="ID_1600595458" CREATED="1595451091675" MODIFIED="1614028722277" HGAP_QUANTITY="-61.9833158388792 pt" VSHIFT_QUANTITY="-93.8727836160297 pt">
<edge COLOR="#ff0000"/>
<node TEXT="Get params from CLI" FOLDED="true" ID="ID_1918793269" CREATED="1595451303666" MODIFIED="1595454274489" LINK="#ID_395641653" HGAP_QUANTITY="16.10218976638829 pt" VSHIFT_QUANTITY="-16.81751813110631 pt">
<node TEXT="IPv" ID="ID_1145165636" CREATED="1595451656888" MODIFIED="1595451667511"/>
<node TEXT="Ports" ID="ID_1901759915" CREATED="1595451668737" MODIFIED="1595451674591"/>
<node TEXT="Endpoints" ID="ID_1615154380" CREATED="1595451676467" MODIFIED="1595451684772"/>
<node TEXT="Output type" ID="ID_1468543366" CREATED="1595451685297" MODIFIED="1595451692840"/>
</node>
<node TEXT="Identify permissions" ID="ID_1126333311" CREATED="1595451332566" MODIFIED="1595453821470" HGAP_QUANTITY="15.313868603992681 pt" VSHIFT_QUANTITY="-14.18978092312095 pt">
<node TEXT="System Manager" ID="ID_922141352" CREATED="1595451697287" MODIFIED="1595451701786"/>
<node TEXT="Net permissions, if any" ID="ID_1981394425" CREATED="1595451706708" MODIFIED="1595451714339"/>
</node>
<node TEXT="Get network interfaces" ID="ID_400585065" CREATED="1595451345626" MODIFIED="1595451379820">
<node TEXT="Loopback" ID="ID_1106458780" CREATED="1595451720067" MODIFIED="1595451725853"/>
<node TEXT="Local" ID="ID_1668405782" CREATED="1595451727008" MODIFIED="1595451728962"/>
<node TEXT="Routable" ID="ID_1202710250" CREATED="1595451730017" MODIFIED="1595451737997"/>
</node>
<node TEXT="Test connections" ID="ID_642365413" CREATED="1595451357256" MODIFIED="1595453819045" HGAP_QUANTITY="15.576642324791216 pt" VSHIFT_QUANTITY="10.773722552739981 pt">
<node TEXT="By IPv" ID="ID_1323645135" CREATED="1595451744037" MODIFIED="1595451826774"/>
<node TEXT="By protocol" ID="ID_179212053" CREATED="1595451799307" MODIFIED="1595451807993"/>
<node TEXT="By host" ID="ID_1838833779" CREATED="1595451757788" MODIFIED="1595451769873"/>
<node TEXT="By port" ID="ID_817793665" CREATED="1595451778737" MODIFIED="1595451791538"/>
</node>
<node TEXT="Display results" ID="ID_678314865" CREATED="1595451381607" MODIFIED="1595453816841" HGAP_QUANTITY="11.372262792014638 pt" VSHIFT_QUANTITY="16.029196968710703 pt">
<node TEXT="Y-Hosts" ID="ID_85765572" CREATED="1595451896228" MODIFIED="1595451903854"/>
<node TEXT="X-Ports" ID="ID_438906905" CREATED="1595451904619" MODIFIED="1595451908926"/>
</node>
</node>
<node TEXT="CLI Logic" LOCALIZED_STYLE_REF="defaultstyle.floating" POSITION="right" ID="ID_395641653" CREATED="1595453878704" MODIFIED="1595456613706" HGAP_QUANTITY="7.430656980036602 pt" VSHIFT_QUANTITY="-1.8394160455897541 pt">
<edge COLOR="#00ffff"/>
<richcontent TYPE="DETAILS">

<html>
  <head>
    
  </head>
  <body>
    <p>
      <font size="1">For each argument</font>
    </p>
  </body>
</html>
</richcontent>
<node TEXT="Get next arg" ID="ID_718193070" CREATED="1595454783529" MODIFIED="1595454792522"/>
<node TEXT="Is arg a flag?" FOLDED="true" ID="ID_191250046" CREATED="1595453899194" MODIFIED="1595454542048">
<node TEXT="" ID="ID_480290042" CREATED="1595454411845" MODIFIED="1595454411845"/>
<node TEXT="Is it the same as the current flag?" ID="ID_242441433" CREATED="1595454544558" MODIFIED="1595455287683"><richcontent TYPE="DETAILS" HIDDEN="true">

<html>
  <head>
    
  </head>
  <body>
    <p>
      <font size="1">Note duplicate flag</font>
    </p>
  </body>
</html>
</richcontent>
</node>
<node TEXT="Set the current flag to arg" ID="ID_1300591782" CREATED="1595454740558" MODIFIED="1595454858782"/>
<node TEXT="Next arg" ID="ID_1791272256" CREATED="1595454760178" MODIFIED="1595454806624" LINK="#ID_718193070"/>
</node>
<node TEXT="Is a flag expected?" FOLDED="true" ID="ID_1608535741" CREATED="1595454872279" MODIFIED="1595455269905"><richcontent TYPE="DETAILS" HIDDEN="true">

<html>
  <head>
    
  </head>
  <body>
    <p>
      <font size="1">First time around we expect a flag, so this is an error state</font>
    </p>
  </body>
</html>
</richcontent>
<node TEXT="Note error" ID="ID_1724369999" CREATED="1595455315722" MODIFIED="1595455346786"/>
<node TEXT="Break to next arg" ID="ID_384409653" CREATED="1595455347452" MODIFIED="1595455361809" LINK="#ID_718193070"/>
</node>
<node TEXT="Process the arg parts" ID="ID_1825203850" CREATED="1595455367673" MODIFIED="1595455658002"><richcontent TYPE="DETAILS" HIDDEN="true">

<html>
  <head>
    
  </head>
  <body>
    <p>
      <font size="1">Split the arg by space and comma, and run each element against the current state processor</font>
    </p>
  </body>
</html>
</richcontent>
</node>
</node>
</node>
</map>
