# 👁️ Mob Controller

> **Turn into any Minecraft mob and live inside it.**
> Possess it, see through its eyes in first person and move like it —walk, fly, swim,
> attack—. Like changing your skin inside the game.

<p align="center">
  <a href="README.md"><img alt="Español" src="https://img.shields.io/badge/Español-6b7280?style=for-the-badge"></a>
  <a href="README.en.md"><img alt="English" src="https://img.shields.io/badge/English-b9791a?style=for-the-badge"></a>
  <a href="README.fi.md"><img alt="Suomi" src="https://img.shields.io/badge/Suomi-6b7280?style=for-the-badge"></a>
</p>

![Minecraft](https://img.shields.io/badge/Minecraft-1.21.1-6b8f3a)
![Loaders](https://img.shields.io/badge/Loaders-Fabric%20·%20NeoForge%20·%20Forge-b9791a)
![Version](https://img.shields.io/badge/version-1.0.0-2f8f4e)
![Java](https://img.shields.io/badge/Java-21-informational)
![License](https://img.shields.io/badge/license-CC0--1.0-lightgrey)

Mod by **Kalevi Latva-äijö**. A companion to **Mob Director**: where that one *directs* mobs
with commands, **Mob Controller** *turns you into* one. Made to live the scene from the inside
—cinematics, documentaries, events— instead of leaving the mob to its AI.

> 📁 **This repo:** `downloads/` has the **ready-to-play jars** · `source/` has the **code**
> (only if you want to build it yourself).

---

## ✨ What it does

- **Through its eyes.** The camera moves to the mob's head, first person, with **instant**
  mouse look (no network lag).
- **Full control.** WASD moves it, the mouse aims it, it jumps and steps up blocks with the
  game's real physics. While you possess, the mob's **AI turns off**: you're in charge.
- **Flight, swimming and attacks.** Flyers fly and **any mob swims** underwater (space up,
  sneak down). Left-click attacks; the **bear rears up** with a roar.
- **Visible body + HUD.** You see your body and legs in first person, plus a HUD with
  **health, hunger and air bubbles** so you don't drown unnoticed.
- **Clean in and out.** You ride the mob invisibly (no jitter); on exit you appear **next to
  the animal**.

---

## 🎮 Controls

| Key | Effect |
|---|---|
| **WASD** | Move / go where you look |
| **Mouse** | Aim the head |
| **Space** | Jump · **up** in flight/swim |
| **Shift** | Move slowly · **down** in flight/swim |
| **Left click** | The mob **attacks** what's in front (with sound) |
| **R** (hold) | Ready pose · the **bear rears on two legs** with a roar |
| **V** | Exit possession |

**Automatic modes** by mob type: 🐾 ground · 🕊️ flight (flyers) · 🌊 swim (any mob underwater).

### Commands

```
/possess                      possess the mob you're looking at (raycast, 64 blocks)
/possess @e[tag=bear,limit=1] possess by entity selector
/unpossess                    leave the possession
```

> Commands and right-click-to-possess require **OP level 2**.

---

## 📦 Installation (players)

1. Download the jar for **your loader** from the **[`downloads/`](downloads/)** folder or the
   **[Releases]** tab → `mobcontroller-<loader>-1.0.0.jar`.
2. Put it in `.minecraft/mods`. On **Fabric** also add **[Fabric API]**.
3. It must be on the **client** and, in multiplayer, also on the **server**
   (the camera is client-side).
4. Enter a world with **cheats** (OP level 2), look at a mob and use `/possess` — or sneak and
   **right-click** it.

[Releases]: ../../releases
[Fabric API]: https://modrinth.com/mod/fabric-api

---

## 🧩 Loaders

| Loader | Version | Status |
|---|---|---|
| **Fabric** | loader 0.19.3 · Fabric API 0.116.13+1.21.1 | ✅ tested in-game |
| **NeoForge** | 21.1.248 | ◐ compiles & packages · in-game testing pending |
| **Forge** | 1.21.1-52.1.16 | ◐ compiles & packages · in-game testing pending |

---

## 🗺️ Version history (0.1.0 → 1.0.0)

The mod's journey on Fabric to the first stable version, by phase.

| Version | Phase | Changes |
|---|---|---|
| **0.1.0** | POV | Camera to the mob's eyes (`setCameraEntity`); `/possess`, sneak+click, exit with V/`/unpossess`; auto-exit on death/disconnect. |
| **0.2.0** | Control | Full WASD+mouse control via a `DriveInput` packet; AI turned off. *(movement had a bug, fixed in 0.3.0)* |
| **0.3.0** | Control | With NoAI `travel()` doesn't move → **manual movement** via `mob.move()`. Client mixin that cancels the attack (avoids the crash from hitting yourself). |
| **0.4.0** | Action | **Flight/swim** in 3D; **attack** as the mob (click, raycast); **pose** with R (`setAggressive`). |
| **0.5.0** | Polish | **Invisible riding** instead of teleporting (no jitter; you exit next to the animal). Mixin `wantsToStopRiding→false` (sneak doesn't dismount). |
| **0.6.0–0.6.2** | Polish | **Visible body** in first person (head hidden for humanoids); pose and attack animation. Attempts at a clean quadruped view (0.6.2 raised camera, reverted). |
| **0.7.0** | Polish | **Instant camera** (`setRotation` per frame), no "mounted" feel; quadrupeds with a clean view. |
| **0.8.0** | Polish | **Visible legs** for quadrupeds/wolf (hide head+torso via accessors); the **bear rears up** (`setStanding`) + sounds. |
| **0.9.0–0.9.4** | Polish | **Camera in the head** calibrated from the model's real head position (`head.z`); **swim** for all mobs; body aligned instantly to your look; attack sound. |
| **1.0.0** | Stable | **Possession HUD** (health, hunger, air); first stable version. Afterwards: **multiloader** (Fabric + NeoForge + Forge). |

---

## 🛠️ Building from source (developers)

The code lives in **`source/`** — a **multiloader** monorepo: `common/` holds the logic and
`fabric/`, `neoforge/`, `forge/` only each loader's entry point (events + networking) over an
abstraction layer (`Services`/`NetworkPlatform`).

```bash
# Java 21:
export JAVA_HOME=".../.minecraft/runtime/java-runtime-delta/windows/java-runtime-delta"

cd source
./gradlew build              # all three loaders
./gradlew :fabric:build      # or just one (:neoforge:build / :forge:build)
```

Jars land in `source/<loader>/build/libs/mobcontroller-<loader>-1.0.0.jar`.

**Build matrix (MC 1.21.1, JDK 21, Gradle 8.10.2):** Fabric Loom 1.7.4 · NeoForge
ModDevGradle 2.0.144 · Forge ForgeGradle 6.0.54.

### Architecture

```
mob-controller/                    · repository
├── downloads/                     · the 3 ready-to-download jars
├── README.md · README.en.md · README.fi.md · pagina-web.html
└── source/                        · Gradle project (to build)
    ├── common/src/main/java/com/mobcontroller/  · SHARED LOGIC (single source set)
    │     MobController · PossessionManager · PossessCommands · Services · NetworkPlatform
    │     net/(PossessSync · Unpossess · DriveInput · MobAttack · Pose)
    │     client/(ClientCore · ClientBridge · ClientPossession · PossessionHud)
    │     client/mixin/(Minecraft · Camera · LevelRenderer · LivingEntityRenderer + accessors)
    │     mixin/(PlayerMixin · MobAmbientSoundAccessor)
    ├── fabric/   → FabricNetwork + fabric.mod.json + mixins (with refmap)
    ├── neoforge/ → NeoForgeNetwork + neoforge.mods.toml + mixins
    └── forge/    → ForgeNetwork + mods.toml + mixins
```

Uses **official Mojang mappings**. Mixins ship as a per-loader copy (Fabric uses a refmap with
intermediary; NeoForge/Forge use official mappings at runtime, no refmap).

---

## 📄 License and credits

Mod created by **Kalevi Latva-äijö**. Licensed **CC0-1.0** (public domain): use, modify and
share it freely. A companion to **Mob Director**.
