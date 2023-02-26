# Project-Poseidon
## What's Project Poseidon?
A CraftBukkit CB1060 fork for Beta 1.7.3 fixing bugs and adding basic features.<br>
Discord: https://discord.gg/FwKg676

## Want to use Project Poseidon on your server?
Please read the following article before changing over to Project Poseidon: https://github.com/RhysB/Project-Poseidon/wiki/Implementing-Project-Poseidon-In-Production

## Licensing
CraftBukkit and Bukkit are licensed under GNU General Public License v3.0<br>
Any future commits to this repository will remain under the same GNU General Public License v3.0<br>
Libraries in the compiled .jar files distrusted may contain their own licenses.<br>
This project contains decompiled code that is copyrighted by Mojang AB typically under the `net.minecraft.server` package.<br>

## How To Setup - IntelliJ IDEA

1. Clone this project using Git or a desktop client.
2. Open IntelliJ and create a new project in the same directory as the Project Poseidon folder.
   
## Compiling

Compiling is done via maven. To compile a JAR, cd into the Project Poseidon directory and run the following command:

```
mvn clean package
```

You should now have a runnable JAR inside the /target folder!

## Regarding the DMCA of CraftBukkit in 2014
The contributor Wolverness who first contributed on CraftBukkit in February 2012 issued a DMCA against CraftBukkit and other major forks of CraftBukkit. 
This project is based on the following commits:

* CraftBukkit: [54bcd1c1f36691a714234e5ca2f30a20b3ad2816](https://hub.spigotmc.org/stash/projects/SPIGOT/repos/craftbukkit/commits/54bcd1c1f36691a714234e5ca2f30a20b3ad2816) (SpigotMC)
* Bukkit: [3524fde5ffc387ef9e39f6ee7dae83ff4dbf8229](https://github.com/Bukkit/Bukkit/commit/3524fde5ffc387ef9e39f6ee7dae83ff4dbf8229) (GitHub)

The Bukkit and CraftBukkit commits that Project Poseidon is based on are before Wolverness started contributing.

If you were a contributor before these commits please feel free to contact me or open an issue asking for this repository to be taken down.

## MC-DEV
We include files from the mc-dev GitHub repository. This code is automatically generated using minecraft_server.jar and sourced from the Bukkit repositories.
* MC-DEV: [1a792ed860ebe2c6d4c40c52f3bc7b9e0789ca23](https://github.com/Bukkit/mc-dev/commit/1a792ed860ebe2c6d4c40c52f3bc7b9e0789ca23)

If Mojang Studios or someone on their behalf wants to have this repository removed due to including copyrighted Minecraft sources like bukkit/mc-dev, please contact me or make an issue.

## How to setup ModLoaderMP support (NOT WORKING)
Please read the following: https://github.com/RhysB/Project-Poseidon/wiki/Adding-ModLoaderMP