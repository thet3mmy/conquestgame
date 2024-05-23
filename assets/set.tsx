<?xml version="1.0" encoding="UTF-8"?>
<tileset version="1.4" tiledversion="1.4.3" name="set" tilewidth="64" tileheight="64" tilecount="5" columns="0">
 <grid orientation="orthogonal" width="1" height="1"/>
 <tile id="0">
  <properties>
   <property name="food" type="int" value="2"/>
   <property name="passable" type="bool" value="true"/>
  </properties>
  <image width="64" height="64" source="grass.png"/>
 </tile>
 <tile id="1">
  <properties>
   <property name="food" type="int" value="1"/>
   <property name="passable" type="bool" value="false"/>
   <property name="water" type="bool" value="true"/>
  </properties>
  <image width="64" height="64" source="water.png"/>
 </tile>
 <tile id="2">
  <properties>
   <property name="food" type="int" value="0"/>
   <property name="passable" type="bool" value="true"/>
  </properties>
  <image width="64" height="64" source="town.png"/>
 </tile>
 <tile id="3">
  <image width="64" height="64" source="settler.png"/>
 </tile>
 <tile id="4">
  <image width="64" height="64" source="warrior.png"/>
 </tile>
</tileset>
