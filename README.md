# hop-plugins
> **_NOTE:_**  
> Latest Update: 2025-01-23 \
> Hop Version 2.15.0

[![Hop external plugins build](https://github.com/project-hop/hop-plugins/actions/workflows/maven.yml/badge.svg)](https://github.com/project-hop/hop-plugins/actions/workflows/maven.yml)

a collection of plugins that can be used with but can't or won't be shipped with Apache Hop 

## Building

Simply run 

`mvn clean install`

You will find the plugin zip files in the various target folders of 

`assemblies/plugins/*`

for example:

`assemblies/plugins/exceloutput/target/hop-external-plugins-assemblies-exceloutput-2.15.0.zip`

## Installing

To install a plugin, unzip the plugin assembly zip file in the plugins/ folder of your Apache Hop client installation.

