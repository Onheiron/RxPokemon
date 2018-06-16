<?xml version="1.0" encoding="UTF-8"?>
<tileset name="Walkable" tilewidth="32" tileheight="32" tilecount="16" columns="4">
 <image source="../tileset/walkable.png" width="128" height="128"/>
 <tile id="0">
  <properties>
   <property name="type" value="walkable"/>
  </properties>
 </tile>
 <tile id="1">
  <properties>
   <property name="type" value="surfable"/>
  </properties>
 </tile>
 <tile id="4">
  <properties>
   <property name="type" value="jump_down"/>
  </properties>
 </tile>
 <tile id="5">
  <properties>
   <property name="type" value="jump_left"/>
  </properties>
 </tile>
 <tile id="6">
  <properties>
   <property name="type" value="jump_right"/>
  </properties>
 </tile>
 <tile id="14">
  <properties>
   <property name="toMap" value="house_1"/>
   <property name="type" value="door"/>
  </properties>
 </tile>
 <tile id="15">
  <properties>
   <property name="toMap" value="map"/>
   <property name="type" value="door"/>
  </properties>
 </tile>
</tileset>
