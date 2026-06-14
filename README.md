# Jolepopo Adventure

A 2D pixel-style action game set in medieval times, built in **Java** with **JavaFX**.
The game tells its own story: you command a party of three samurai heroes and must defeat
every monster — culminating in the **Gorgon Boss** — to save the world.

> **Course project** — 2110215 Programming Methodology I, Semester 2 / Year 2024,
> Chulalongkorn University.

**Authors**
- Wattikon Nongnamkhao — 6731349021
- Suwira Pattarawuttinan — 6731364421

---

## Table of Contents
- [Gameplay Overview](#gameplay-overview)
- [Controls](#controls)
- [The Heroes](#the-heroes)
- [Maps & Progression](#maps--progression)
- [Enemies](#enemies)
- [Requirements](#requirements)
- [How to Run](#how-to-run)
- [Project Structure](#project-structure)
- [Architecture](#architecture)
- [Configuration & Tuning](#configuration--tuning)
- [Assets](#assets)
- [Documentation](#documentation)

---

## Gameplay Overview

You control a **team of three characters** (Samurai Melee, Samurai Archer, and Samurai
Commander) and can swap between them at any time. Each hero has a distinct playstyle and
two unique abilities. Monsters chase and attack the active character — if your whole party
dies, it's game over.

Battle maps scroll horizontally and are filled with enemies; recovery maps are static
"safe zones" where you can heal at a **bonfire** and spend upgrades. Reach the far-right
edge of a map and press **ENTER** to travel onward, fighting through to the final boss.

The core loop:

1. **Fight** through monsters on a battle map.
2. **Switch** characters to use the right tool for each enemy.
3. **Recover & upgrade** at bonfires in recovery zones.
4. **Advance** to the next map, all the way to the **Gorgon Boss**.
5. **Win** by defeating the boss — which clears all remaining monsters and projectiles.

---

## Controls

| Key | Action |
| --- | --- |
| `A` / `D` | Move left / right |
| `W` or `SPACE` | Jump |
| `SHIFT` | Dash (costs mana) |
| `K` | Ability One (e.g. attack / shoot) |
| `L` | Ability Two (e.g. defend / support) |
| `TAB` | Switch to the next character (spawns at the current location) |
| `ENTER` | Travel to the next map (when the prompt appears at the far-right edge) |
| `H` | Fully restore health (only near a bonfire) |
| `P` | Open the bonfire upgrade menu (only near a bonfire) |
| `1` / `2` | In the upgrade menu: `1` = +mana regen, `2` = +damage multiplier |
| `E` | Pause — opens the stop menu / return to main menu |
| `M` | Toggle **debug mode** (draws hitboxes and prints system info) |

---

## The Heroes

| Character | Ability One (`K`) | Ability Two (`L`) | Role |
| --- | --- | --- | --- |
| **Samurai Melee** | Sword attack (close range) | Defend | Front-line bruiser |
| **Samurai Archer** | Shoot arrows | Charged **Big Arrow** (high mana cost) | Ranged damage |
| **Samurai Commander** | Bless / support | Curse (affects nearby monsters) | Support / utility |

Switching characters with `TAB` keeps the party's combined survival in mind — each hero has
its own health pool, and projectiles are cleared on switch.

---

## Maps & Progression

The game flows through five map slots (`currentMapIndex` 0–4):

| Map | Type | Contents |
| --- | --- | --- |
| 0 | Battle (scrollable) | Minotaurs |
| 1 | **Recovery** (static) | Bonfire — heal & upgrade |
| 2 | Battle (scrollable) | Skeletons & Skeleton Warriors ("The Forest") |
| 3 | **Recovery** (static) | Bonfire — heal & upgrade |
| 4 | Battle (scrollable) | **Gorgon Boss** + Skeletons, Warriors, Minotaurs, and a meteor shower ("The Boss") |

- **Battle maps** scroll with the camera and require clearing all monsters before you can
  advance from the far-right edge.
- **Recovery maps** are locked-width safe zones containing a bonfire.
- The final map adds a **meteor shower** hazard. Defeating the boss instantly clears all
  monsters and meteors and triggers the ending.

---

## Enemies

| Enemy | Notes |
| --- | --- |
| **Minotaur** | Standard chasing melee monster |
| **Skeleton** | Fast, low-health swarm enemy |
| **Skeleton Warrior** | Tougher variant of the Skeleton |
| **Gorgon Boss** | Final boss with a large health pool; defeating it wins the game |

Damage taken depends on which hero is active (e.g. monsters deal different damage to the
Melee, Archer, and Commander) — see [`GameConfig`](src/config/GameConfig.java).

---

## Requirements

- **JDK 22** (the project is configured for *"jdk-22 with javaFX"*).
- **JavaFX SDK** (the project uses `javafx.application`, `javafx.scene`, `javafx.animation`,
  `javafx.media`, etc.). Use a JavaFX-bundled JDK or add the JavaFX modules to your
  module/class path.
- Audio playback support for MP3/M4A (via `javafx.media`).

---

## How to Run

### Option 1 — Run the prebuilt JAR
A packaged build is included:

```bash
java -jar JAR_FILR_jolepopo_test10.jar
```

> If your JDK doesn't bundle JavaFX, add the JavaFX modules, e.g.:
> ```bash
> java --module-path /path/to/javafx-sdk/lib \
>      --add-modules javafx.controls,javafx.media \
>      -jar JAR_FILR_jolepopo_test10.jar
> ```

### Option 2 — Eclipse
This is an Eclipse project (`.project` / `.classpath` included, project name
*Jolepopo_project*). Import it, ensure the **jdk-22 with javaFX** JRE is configured, and run
[`src/application/Main.java`](src/application/Main.java).

### Option 3 — Command line
Compile the sources in `src/` against the JavaFX libraries and run the entry point
`application.Main`. The `images/`, `sounds/`, and `others/` folders are on the classpath as
resource roots (see [`.classpath`](.classpath)), so keep them alongside the compiled output.

The entry point ([`Main.java`](src/application/Main.java)) launches the **Game Menu**, where
you can choose **START**, **HOW TO PLAY**, or **QUIT**.

---

## Project Structure

```
Jolepopo/
├── src/
│   ├── application/      # Main — JavaFX Application entry point
│   ├── camera/           # Camera — follows the player horizontally
│   ├── config/           # GameConfig — all tunable constants
│   ├── entities/         # Characters, Monsters, Boss
│   │   ├── environment/  #   Bonfire (safe-zone interaction)
│   │   └── projectiles/  #   Arrow, BigArrow, Meteor
│   ├── gui/              # Menus & overlay scenes (menu, story, pause, game over, ending)
│   ├── interfaces/       # Renderable, Updatable, Controllable, Damageable, AbilityCaster
│   ├── logic/            # GameScene (loop), GameLogicManager, PlayerTeamManager
│   ├── ui/               # HUDRenderer — health/mana bars, boss bar
│   └── utils/            # Assets, ButtonUtils, SoundManager
├── images/               # Sprite sheets & UI art (per-entity folders)
├── sounds/               # effects/ and musics/
├── others/               # Misc art (shadows, faces, HUD bars)
├── JAR_FILR_jolepopo_test10.jar   # Prebuilt runnable game
├── UMLjole.png           # UML class diagram
└── Handbook-…​.pdf        # Full design/reference documentation
```

---

## Architecture

The game is built around a JavaFX **scene/overlay** model and a single animation loop.

- **`application.Main`** — a JavaFX `Application` that shows `GameMenu` on the primary stage.
- **`gui.*`** — each screen (`GameMenu`, `StoryPage`, `HowToPlayPage`, `GameStopPage`,
  `GameOverPage`, `EndingPage`) builds a `Scene` and swaps it onto the `Stage`.
- **`logic.GameScene`** — extends `AnimationTimer` and implements `Updatable`; it is the heart
  of the game. Each frame it `update()`s the world (player, camera, monsters, meteors, map
  transitions, bonfire proximity) and `render()`s everything to a `Canvas` via
  `GraphicsContext`. It also owns input handling (key pressed/released) and map progression.
- **`logic.GameLogicManager`** — collision/combat resolution: melee hit detection, arrow and
  big-arrow hits, removing dead monsters (with sound cues), boss-defeat → ending, and player
  death → game over.
- **`logic.PlayerTeamManager`** — holds the three-character party and handles `TAB` switching.
- **`camera.Camera`** — tracks the player's X position on scrollable maps.
- **`entities`** — an abstract **`Character`** (implements `Renderable`, `Damageable`,
  `Controllable`, `AbilityCaster`) with concrete `SamuraiMelee` / `SamuraiArcher` /
  `SamuraiCommander`; an abstract **`Monster`** (implements `Renderable`, `Updatable`,
  `Damageable`) with `Minotaur`, `Skeleton`, `SkeletonWarrior` (extends `Skeleton`), and
  `GorgonBoss`. Projectiles (`Arrow`, `BigArrow`, `Meteor`) and `Bonfire` live in sub-packages.
- **`interfaces`** — small capability contracts:
  `Renderable` (draw), `Updatable` (tick), `Controllable` (movement/dash/jump),
  `Damageable` (take damage / hitbox), `AbilityCaster` (ability one & two).
- **`ui.HUDRenderer`** — draws the active character's health & mana bars and the boss bar.
- **`utils`** — `Assets` (image loading from resources), `ButtonUtils` (image buttons + hover
  effects), `SoundManager` (BGM and sound-effect playback via `javafx.media`).

---

## Configuration & Tuning

All gameplay constants live in [`src/config/GameConfig.java`](src/config/GameConfig.java) —
a single place to tweak balance and feel, including:

- **Screen / map** — `SCREEN_WIDTH` (1244), `SCREEN_HEIGHT` (700), `MAP_WIDTH` (5000),
  `MAP_LOCK_WIDTH` (900), `GROUND_LEVEL` (350).
- **Player movement** — `PLAYER_SPEED`, `DASH_SPEED`, `DASH_DURATION`, `DASH_COOLDOWN`,
  `DASH_MANA_COST`, `GRAVITY`, `JUMP_STRENGTH`.
- **Player stats & damage** — `PLAYER_MAX_HEALTH`, `PLAYER_MAX_MANA`, sword/arrow/big-arrow
  damage and speeds, `MANA_REGEN`, `PLAYER_DAMAGE_MULTIPLIER`.
- **Combat timings** — attack/defend cooldowns and frame intervals.
- **Enemies** — health, speed, attack range, and per-hero damage for Minotaur, Skeleton(s),
  and the Boss; plus `METEOR_SPAWN_INTERVAL`.

> Note: a few fields (`MANA_REGEN`, `PLAYER_DAMAGE_MULTIPLIER`) are mutable at runtime — the
> bonfire upgrade menu increases them when you spend an upgrade.

---

## Assets

- **`images/`** — sprite sheets organized per entity (`samuraiMelee`, `samuraiArcher`,
  `samuraiCommander`, `minotaur`, `gorgon`, `bonfire`, …) plus UI art under `gamemenu`,
  `gameover`, `gamepause`, `letter`, and `map`.
- **`sounds/effects/`** — combat and interaction SFX.
- **`sounds/musics/`** — background tracks per map/scene (menu, fighting, recovery, boss,
  victory).
- **`others/`** — shared art such as shadows and HUD bars.

Resources are loaded through `utils.Assets` and `utils.SoundManager`; the folders are
registered as classpath source roots in [`.classpath`](.classpath).

---

## Documentation

- **`Handbook-1243412-17471434041553.pdf`** — full project documentation: rules, examples,
  scene-by-scene walkthrough, and a complete per-class / per-field / per-method reference.
- **`UMLjole.png`** — the UML class diagram (a readable copy is also bundled in the JAR).

---

*Defeat every monster, survive the meteor shower, and slay the Gorgon Boss to save the world. Good luck!*
