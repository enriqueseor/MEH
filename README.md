# Map Editor of Happiness (MEH)

![MEH_mainScreen](resources/MEH_main_screen.png)

MEH is a Generation III Map Editor for Pokemon games for the GameBoy Advance:
- Fire Red
- Leaf Green
- Ruby
- Sapphire
- Emerald

## Table of Contents

- [About](#about)
- [Improvements](#improvements)
- [Dependencies](#dependencies)

## About

This is a fork from a [project](https://github.com/shinyquagsire23/MEH) developed a while ago by other people. My goal
is to update it and continue it's develop as far as I can.

## Improvements

I added GBAUtils and dsdecmp dependencies locally and ini4j dependency (to it's lastest version from 2015) externally 
with a maven repository at it's lastest version, allowing java to been upgrade from 1.6 to 21.

I also have removed day/night mode and the loading map speed in all the editor have rocket, and also all useless
resources, classes, etc.

## To-Do

My goal is that MEH can be as good as [Advance-Map](http://ampage.no-ip.info/index.php?seite=advancemap) but being
open source.

In this moment we have somethings to improve:

- Better init
- Wild Pokemon edition 

### Dependencies

- [GBAUtils](https://github.com/shinyquagsire23/GBAUtils)