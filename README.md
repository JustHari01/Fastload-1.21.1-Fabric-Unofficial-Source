# Fastload Unofficial — Fabric 1.21.1

This is an unofficial community port of [Fastload](https://github.com/BumbleSoftware/Fastload) for Minecraft 1.21.1 and Fabric. It is not an official release and is not affiliated with the original authors. The complete, buildable source for this port is included with the release as `fastload-fabric-1.21.1-unofficial-3.4.0+1.21.1-port.1-source-code.zip`.

The port keeps Fastload's main loading controls:

- Skip the vanilla spawn-chunk wait during world startup.
- Show Fastload's progress screen while client chunks load and build.
- Configure separate chunk-preload radii for singleplayer and multiplayer.
- Optionally skip the terrain screen with `instant_load`.
- Keep world rendering and server resource-pack prompts working during terrain loading.

Configuration is stored in `config/fastload.properties`. Mod Menu 11 or newer is optional and adds an in-game configuration screen.

## Build

Use Java 21 and the included Gradle wrapper:

```powershell
.\gradlew.bat build
```

The distributable and IDE source JAR are written to `build/libs/`; a complete, buildable source bundle is written to `build/distributions/`.

## Validation status

The 1.21.1 source compiles, the remapped Fabric JAR and source bundle build, and a clean Fabric client starts with Fastload loaded. An isolated singleplayer test world reached the Overworld, the integrated server accepted the player, and Fastload completed its initial chunk preload. This verifies the local singleplayer world-entry path only; multiplayer joins, respawn, and server resource-pack prompts remain untested.

## Credits and license

Fastload was created by StockiesLad, JoostMSoftware, AbdElAziz333, kennytv, and VidTu. This port is based on their GPL-3.0-licensed source and is distributed under GPL-3.0-only. The JAR includes `LICENSE_Fastload_Unofficial`; the source bundle includes `LICENSE`.
