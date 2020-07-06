# Project-Poseidon
## What is Project Poseidon
A CraftBukkit CB1060 fork for Beta 1.7.3 fixing bugs and adding basic features.

Discord: https://discord.gg/FwKg676

## Licensing
Craft Bukkit and Bukkit are licensed under GNU General Public License v3.0

Any future commits to this repository will remain under the same GNU General Public License v3.0

Libraries in the compiled .jar files distrusted may contain their own licenses.

This project contains decompiled code that is copyrighted by Mojang AB typically under the package net.minecraft.server.

## How To Setup And Compile - IntelliJ IDEA

1. Clone this project using Git or a desktop client.
2. Open IntelliJ and create a new project in the same directory as the Project Poseidon src folder.
3. Download or create a libraries jar as shown in the section [How to get a libraries jar](#how-to-get-a-libraries-jar)
4. Add the downloaded libraries jar as a library
5. Create a artifact in Project Structure including the dependency
6. Click Include in project build
7. Build > Build Project. 

You should now have a runnable JAR!

## How to get a libraries jar
You can either download the libraries jar or extract it manually from a CB1060-CB1092 jar.

* You can download the libraries jar from the following method: https://www.dropbox.com/s/qs110g2juivinp8/CraftBukkit-b1.7.3-libs.jar?dl=1
* You can remove Bukkit, CraftBukkit and MC-DEV files from an existing CB1060-CB1092 jar. This jar should then only contain the libraries of CraftBukkit

## Regarding the DMCA of CraftBukkit in 2014
The contributor Wolverness who first contributed on CraftBukkit in February 2012 issued a DMCA against CraftBukkit and other major forks of CraftBukkit. 
This project is based on the following commits:

* CraftBukkit: 54bcd1c1f36691a714234e5ca2f30a20b3ad2816 (https://hub.spigotmc.org/stash/projects/SPIGOT/repos/craftbukkit/commits/54bcd1c1f36691a714234e5ca2f30a20b3ad2816) 
* Bukkit: 3524fde5ffc387ef9e39f6ee7dae83ff4dbf8229 (https://github.com/Bukkit/Bukkit/commit/3524fde5ffc387ef9e39f6ee7dae83ff4dbf8229)

The Bukkit and CraftBukkit commits that Project Poseidon is based on are before Wolverness started contributing.

If you were a contributor before these commits please feel free to contact me or open an issue asking for this repository to be taken down.

## MC-DEV
We include files from the mc-dev Github repository. This code is automatically generated using minecraft_server.jar and sourced from the Bukkit repositories.
* MC-DEV: 1a792ed860ebe2c6d4c40c52f3bc7b9e0789ca23 (https://github.com/Bukkit/mc-dev/commit/1a792ed860ebe2c6d4c40c52f3bc7b9e0789ca23)

If Mojang or a Mojang employee wants to have this repository removed due to including Minecraft source like bukkit/mc-dev, please contact me or make an issue.
