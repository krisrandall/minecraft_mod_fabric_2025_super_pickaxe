

# Joschi's Pickaxe

### 2025 Minecraft Super Pickaxe Mod

<img src="src/main/resources/assets/modid/icon.png" />

---

### This repository is to be used with the following tutorial : https://medi-muse.medium.com/minecraft-modding-101-31747d653f00?sk=29d3f691976a481c5ad6455030f31be8

Only after doing the tutorial will you have :

---

A pickaxe with 100x the strength of Diamond

* 100x more durable than diamond
* 10x faster than diamond
* Much stronger attack damage
* Can mine everything (netherite level)
* Highly enchantable
* No repair ingredient needed

Made for Minecraft 1.21.11.

----

## Installation

To run this Mod you must first do the following steps :

1. Install Minecraft 
2. Install Fabric (https://fabricmc.net/use/installer/) (`java -jar fabric-installer-1.0.3.jar`)
3. Install the Fabric API (https://www.curseforge.com/minecraft/mc-mods/fabric-api/download/6136309)
4. Install the Mod Menu Mod (https://modrinth.com/mod/modmenu?version=1.21.4&loader=fabric)

*When getting mods, Minecraft version must be an exact match - and mod platform is “Fabric”.*

*Ensure Fabric version is also the same as your Minecraft version.*

5. **Run Minecraft**


## Modifying this Mod

To modify this mod, first build it and install it just as it is.

First you must install the Java Development Kit (JDK) (https://www.geeksforgeeks.org/download-and-install-java-development-kit-jdk-on-windows-mac-and-linux/) 

These are the steps (in your terminal/console)

First, we will set `MINECRAFT_MOD_FOLDER` for use in all future commands, this is where your Minecraft version is looking for the modfiles to install when it runs.

```
export MINECRAFT_MOD_FOLDER=~/.minecraft/mods
```

(you may need to change the above to suit your environment)

Run these commands from inside of the folder where you have downloaded this mod code.

#### Build Steps

1. Build and install:

```
./gradlew build
cp build/libs/modid-1.0.0.jar $MINECRAFT_MOD_FOLDER
```

2. **Run Minecraft**

#### To make changes

Start with modifying these 2 files:
* `main/java/ExampleMod.java`
* `main/resources/fabric.mod.json`

Repeat the build steps above to see your changes in Minecraft


## License

This template is available under the CC0 license. 
Feel free to learn from it and incorporate it in your own projects.


## Credits & Troubleshooting

This Mod was based on the Fabric example mod (https://github.com/FabricMC/fabric-example-mod/)

You can get it up and running with commands something like this:

```
git clone https://github.com/FabricMC/fabric-example-mod.git my-fabric-mod
cd my-fabric-mod
./gradlew build
# Find the jar file - ls build/libs
cp build/libs/modid-1.0.0.jar /home/{user}/.minecraft/mods
```

This Mod was also helped by Claude AI - and if you have any issues getting it running, or have any questions - I recommend directing them to Claude (https://claude.ai) - supply it with this README information for context.

Note that you can check the logs for your log messages with:

```
cat $MINECRAFT_MOD_FOLDER/../logs/latest.log | grep "Hello Fabric world"
```
