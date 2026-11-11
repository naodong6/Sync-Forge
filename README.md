![Logo](media/logo.png)

# Sync (NeoForge 1.21.1)
[![CurseForge](https://img.shields.io/badge/dynamic/json?color=%23f16436&label=CurseForge&query=title&url=https%3A%2F%2Fapi.cfwidget.com%2F515365&cacheSeconds=3600)](https://www.curseforge.com/minecraft/mc-mods/sync-fabric-rereported)
[![GitHub license](https://img.shields.io/github/license/pawjwp/sync-fabric.svg?cacheSeconds=36000)](https://github.com/pawjwp/sync-fabric?tab=License-1-ov-file)

[Sync (Forge)](https://github.com/pawjwp/sync-fabric) is an unofficial port of an [unofficial port](https://modrinth.com/mod/sync-fabric-reported) of an [unofficial reimplementation](https://modrinth.com/mod/sync-fabric) of the original [Sync](https://github.com/iChun/Sync) mod, originally developed by [iChun](https://github.com/iChun). This version was created for the [Crash Landing Remake](https://modrinth.com/modpack/crash-landing-remake) modpack and fixes a couple of significant bugs (see versions page).

This repository contains a community port of the Forge edition to Minecraft 1.21.1 and NeoForge.

The original description says it all:

> Sync provides clones, or as we like to call it, "shells". These shells are basically a new individual, with their own inventory, experience level, and even gamemode.
> \
> \
> However, what they lack, is a mind to control them. That’s where the player comes in. Each shell is biometrically tied to the player who’s sample is used to create it, and will allow the player to “sync” their mind to the other shell, essentially creating multiple player instances.

----

## How to play

 1. You need to craft a `shell constructor` and place it down.
 2. Then you need to provide it with a genetic sample (just `right-click` it).\
 **⚠️WARNING: with the default config, this action will KILL you!** In order to create a shell with full health, the constructor must absorb 20HP *(40 for Hardcore players)*. If you don't want to die, you can eat a golden apple to increase your maximum health, or you can hold a totem of undying during the process *(which is the only option for Hardcore players)*.
 3. The shell constructor needs power to work, so put a `treadmill` next to it *(it should touch any side of any part of the shell constructor)* and lure a `pig` or a `wolf` to its center to start generating piggawatts.\
 You should end up with something like this:
 ![Example of working shell constructor](media/shell_constructor-showcase.png)\
 <sub>You don't really need a comparator, it's here just to demonstrate that you can determine progress of the shell construction process via strength of the comparator output.</sub>
 4. Once your new shell is constructed, you need to craft a `shell storage` and place it down.
 5. Supply it with `redstone power`.
 6. Once doors of the shell storage are open, you can `walk into it`.
 7. You'll see a radial `menu` that displays your shells:
 ![Menu example](media/menu-showcase.png)
 8. `Select` the shell you want to transfer your mind into, and enjoy the process!

 ## Notes

  - You can color-code shells stored in shell storages. Just right-click a shell storage with dye.
  - Syncing works cross dimensional, and should support custom dimensions.
  - If you die while using a shell, you'll be immediately synced with your original body *(if you still have one; otherwise your mind will be transferred to a random shell)*.
  - Death of a shell doesn't increase your death counter.
  - Shell can be equipped (or unequipped) with armor, tools, etc. via hoppers connected to a corresponding shell container.
  - Shell storage should be constantly supplied with power in order to keep stored shell alive (configurable).
    - Shell storage can be powered by redstone, if the `shellStorageAcceptsRedstone` option is set to `true`.
    - Shell storage can be powered by any valid energy source (e.g., treadmills, machinery from popular tech mods, etc.).
  - It's possible to measure a shell container's state with a comparator.
    - You can determine progress of the shell construction process via strength of the comparator output.
    - You can measure the fullness of a shell's inventory via strength of the comparator output.
    - You can change a comparator output type by right-clicking on a shell container with a wrench.
  - Shell storage and shell constructor are pretty fragile, so don't try to mine them without `silk touch` enchantment.

## Crafting recipes

#### Sync Core:

![Sync Core: Daylight Detector + Lapis Block + Daylight Detector + Quartz + Ender Pearl + Quartz + Emerald + Redstone Block + Emerald](media/sync_core-recipe.png)

#### Sync Core (for Hardcore players):

![Sync Core (Hardcore): Daylight Detector + Lapis Block + Daylight Detector + Quartz + Nether Star + Quartz + Emerald + Redstone Block + Emerald](media/sync_core-recipe-hardcore.png)

P.S. - If you aren't brave enough to fight the Wither in Hardcore, you can use [this datapack](media/simplified_hardcore_recipe_datapack.zip) *(it will bring back the original Sync Core recipe)*. Please read [this article](https://minecraft.fandom.com/wiki/Tutorials/Installing_a_data_pack), if you don't know how to do it.

#### Shell Constructor:

![Shell Constructor: Gray Concrete + Sync Core + Gray Concrete + Glass Pane + Glass Pane + Glass Pane + Gray Concrete + Redstone + Gray Concrete](media/shell_constructor-recipe.png)

#### Shell Storage:

![Shell Storage: Gray Concrete + Sync Core + Gray Concrete + Glass Pane + Iron Block + Glass Pane + Gray Concrete + Heavy Weighted Pressure Plate + Gray Concrete](media/shell_storage-recipe.png)

#### Treadmill:

![Treadmill: Air + Air + Daylight Detector + Gray Carpet + Gray Carpet + Iron Bars + Gray Concrete + Gray Concrete + Redstone](media/treadmill-recipe.png)

----

## Config

The mod is highly configurable. The config is located at `./config/sync-common.toml` by default looks like this:

```
#Sync Configuration
[general]

	#Shell Construction Settings
	[general.construction]
		#Enable instant shell construction (creative mode-like)
		enableInstantShellConstruction = false
		#Warn player instead of killing them on sync failure
		warnPlayerInsteadOfKilling = false
		#Damage dealt by fingerstick (0-100)
		#Range: 0.0 ~ 100.0
		fingerstickDamage = 20.0
		#Damage dealt by fingerstick in hardcore mode (0-100)
		#Range: 0.0 ~ 100.0
		hardcoreFingerstickDamage = 40.0
		#Item required to construct a new shell (format: 'modid:itemname', e.g., 'minecraft:ender_pearl')
		#Leave empty to disable item requirement
		shellConstructionRequiredItem = ""
		#Number of items consumed when constructing a shell
		#Range: 1 ~ 64
		shellConstructionItemCount = 1
		#Should the required item be consumed in creative mode?
		consumeItemInCreative = false
		#Custom error message when missing required item (use %s for item name, %d for count)
		missingItemMessage = "You need %s x%d to construct a new shell!"

	#Shell Storage Settings
	[general.storage]
		#Energy capacity of shell constructor
		#Range: 1000 ~ 9223372036854775807
		shellConstructorCapacity = 256000
		#Energy capacity of shell storage
		#Range: 10 ~ 9223372036854775807
		shellStorageCapacity = 320
		#Energy consumption per tick for shell storage
		#Range: 1 ~ 1000
		shellStorageConsumption = 16
		#Whether shell storage accepts redstone power
		shellStorageAcceptsRedstone = true
		#Maximum ticks shell storage can run without power
		#Range: 0 ~ 1200
		shellStorageMaxUnpoweredLifespan = 20

	#Energy Generation
	[general.energy]
		#Entity energy output mapping (format: 'modid:entity=energyAmount')
		energyMap = ["minecraft:chicken=2", "minecraft:pig=16", "minecraft:player=20", "minecraft:wolf=22", "minecraft:villager=25", "minecraft:creeper=80", "minecraft:enderman=160"]

	#Gameplay Settings
	[general.gameplay]
		#Priority for shell selection (NATURAL, NEAREST, or color names)
		#Allowed Values: WHITE, ORANGE, MAGENTA, LIGHT_BLUE, YELLOW, LIME, PINK, GRAY, LIGHT_GRAY, CYAN, PURPLE, BLUE, BROWN, GREEN, RED, BLACK, NEAREST, NATURAL
		syncPriority = "NATURAL"

	#Tools
	[general.tools]
		#Item to use as wrench (format: 'modid:item')
		wrench = "minecraft:stick"

	#Client Settings
	[general.client]
		#Automatically update translations
		updateTranslationsAutomatically = false
		#Enable camera animation when switching between shells
		enableShellSwitchAnimation = true
		#Enable camera animation when respawning into a shell after death
		enableDeathRespawnAnimation = true

	#Easter Eggs
	[general.easter_eggs]
		#Enable Technoblade easter egg
		enableTechnobladeEasterEgg = true
		#Render Technoblade's cape
		renderTechnobladeCape = false
		#Allow Technoblade announcements
		allowTechnobladeAnnouncements = true
		#Allow Technoblade quotes
		allowTechnobladeQuotes = true
		#Delay between Technoblade quotes (in ticks)
		#Range: 200 ~ 72000
		technobladeQuoteDelay = 1800
		#UUIDs of players to treat as Technoblade
		technobladeUuids = []
```

| Name | Description | Default value |
| ---- | ----------- | ------------- |
| `enableInstantShellConstruction` | If this option is enabled, creative-like shells will be constructed immediately, without the use of energy | `false` |
| `warnPlayerInsteadOfKilling` | If this option is enabled, a player won't be killed by a shell constructor if they don't have enough health to create a new shell | `false` |
| `fingerstickDamage` | The amount of damage that a shell constructor will deal to a player when they try to create a new shell | `20.0` |
| `hardcoreFingerstickDamage` | The amount of damage that a shell constructor will deal to a player in the Hardcore mode when they try to create a new shell | `40.0` |
| `shellConstructorCapacity` | The amount of energy required to construct a new shell | `256000` |
| `shellStorageCapacity` | Determines capacity of a shell storage's inner battery | `320` |
| `shellStorageConsumption` | Energy consumption of a shell storage's life support systems (per tick) | `16` |
| `shellStorageAcceptsRedstone` | If this option is enabled, a shell storage can be powered by redstone | `true` |
| `shellStorageMaxUnpoweredLifespan` | Determines how many ticks a shell can survive without a power supply connected to the corresponding shell storage | `20` |
| `energyMap` | Specifies a list of entities that can produce energy via treadmills | [...](#user-content-config) |
| `preserveOrigins` | If this option is enabled, all user shells will share the same [origins](https://www.curseforge.com/minecraft/mc-mods/origins) | `false` |
| `syncPriority` | The order of shell selection for synchronization in case of death <br><br>Available `priority` values:<br><ul><li>`NATURAL` - non-artificial shells are prioritized</li><li>`NEAREST` - nearest shells are prioritized</li><li>`WHITE`, `ORANGE`, `MAGENTA`, `LIGHT_BLUE`, `YELLOW`, `LIME`, `PINK`, `GRAY`, `LIGHT_GRAY`, `CYAN`, `PURPLE`, `BLUE`, `BROWN`, `GREEN`, `RED`, `BLACK` - shells of the given color are prioritized</li><ul> | `[{ "priority": "NATURAL" }]` |
| `wrench` | Identifier of an item that can be used as a wrench in order to change a shell constructor's state | `minecraft:stick` |
| `updateTranslationsAutomatically` | If this option is enabled, translations will be updated every time the game is launched | `false` |

You can edit any of these values directly in the config file or via [ModMenu](https://github.com/TerraformersMC/ModMenu).

----

## Translations

[![Crowdin](https://badges.crowdin.net/sync-fabric/localized.svg)](https://crowdin.com/project/sync-fabric)

[Sync (Fabric)](https://github.com/pawjwp/sync-fabric) makes use of crowd sourced translations.

You can help translate the mod to additional languages here: [crowdin.com](https://crowdin.com/project/sync-fabric).

----

## Known Issues

 - Dynamic shell texturing does not work with Iris ([#25](https://github.com/Kir-Antipov/sync-fabric/issues/25))

----

## Installation

Requirements:
 - Minecraft `1.21.1`
 - NeoForge `>=21.1.0`

You can download the mod from:

 - [CurseForge](https://www.curseforge.com/minecraft/mc-mods/sync-forge)
 - [GitHub Actions](https://github.com/pawjwp/sync-fabric/actions/workflows/build-artifacts.yml) *(these builds may be unstable, but they represent the actual state of the development)*

## Developers

`build.gradle`:

```groovy
repositories {
    maven {
        name = "Modrinth"
        url = "https://api.modrinth.com/maven"
        content {
            includeGroup "maven.modrinth"
        }
    }
}

dependencies {
    modImplementation "maven.modrinth:sync-fabric:${project.sync_version}"
}
```

`gradle.properties`:

```properties
sync_version=/* latest version goes here */
```

There's [no documentation](https://www.commitstrip.com/en/2021/11/10/no-documentation/) for the project's [api](https://github.com/Kir-Antipov/sync-fabric/tree/1.17.x/dev/src/main/java/stable/kir/sync/api) yet, but you can find some useful examples in the [`compat` package](https://github.com/Kir-Antipov/sync-fabric/tree/1.17.x/stable/src/main/java/dev/kir/sync/compat).

## Building from sources

Requirements:
 - JDK `21`

### Linux/MacOS

```bash
git clone https://github.com/VTSumik/Sync-Forge.git
cd Sync-Forge

chmod +x ./gradlew
./gradlew build
cd build/libs
```
### Windows

```powershell
git clone https://github.com/VTSumik/Sync-Forge.git
cd Sync-Forge

.\gradlew.bat build
cd build\libs
```

----

## License

Created by [Kir_Antipov](https://github.com/Kir-Antipov), licensed under MIT license.\
Originally made by [iChun](https://github.com/iChun) under GNU LGPLv3 license.

```
Copyright (C) 2021 Kir_Antipov

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```

[Sync (Fabric)](https://github.com/Kir-Antipov/sync-fabric) was originally licensed under GNU LGPLv3 license to conform to [the original mod](https://github.com/iChun/Sync), but since the project is not a port, but a reimplementation with its own unique codebase, I got permission from [iChun](https://github.com/iChun) to change the license to MIT.

You can find more about the MIT license on [this website](https://choosealicense.com/licenses/mit/).

<a href="https://www.curseforge.com/minecraft/mc-mods/fabric-api" target="_blank"><img alt="Fabric API - Mods - Minecraft - CurseForge" src="https://i.imgur.com/Ol1Tcf8.png" height="48"></a>
