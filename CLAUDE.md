# CLAUDE.md — Implementation Reference

> This file is for Claude. Read it at the start of every session. Update it at the end of every session.
> It is not a README. It is not for contributors. It is the persistent brain of this project.

---

## Project in One Sentence

A Java CLI game launcher that forces Russian Roulette every session, then lets the player pick from a community-contributed game collection via an arrow-key menu. Anyone can add a game via PR with zero review beyond implementing the `Game` interface.

**Repo:** https://github.com/H4WKF0X/the-arcade  
**Owner:** Maximilian Paesold (@H4WKF0X)

---

## Stack & Versions

| Thing | Version / Choice                                                                                 |
|---|--------------------------------------------------------------------------------------------------|
| Java | 21 (Oracle OpenJDK 21.0.1)                                                                       |
| Build | Gradle with Kotlin DSL (`build.gradle.kts`)                                                      |
| Gradle | 9.3.0 via Wrapper                                                                                |
| Terminal engine | JLine 3 (latest stable)                                                                          |
| Save file serialization | Gson                                                                                             |
| Game discovery | Reflections library (`@ArcadeGame` annotation scanning)                                          |
| Testing | JUnit 6 (`6.0.0` BOM — this version exists, do not downgrade)                                    |
| GroupId | `com.mpaesold.arcade` |

---

## Architectural Decisions (Do Not Re-debate Without Flagging)

- **One save file** (`arcade_save.json` in project root, gitignored). Single-player only for now. No multi-profile support yet.
- **`SaveManager` is the only interface to the save file.** No game ever opens the JSON directly.
- **Russian Roulette is mandatory on every launch.** It is not configurable. It is not skippable.
- **`Game` interface has exactly two methods:** `getMetadata()` and `launch(GameContext)`. Do not add more without flagging to owner.
- **`GameContext` exposes three things only:** `SaveManager`, player name (String), and optionally the shared `Terminal`. Nothing else from the outside world.
- **`@ArcadeGame` annotation** drives the game registry. Fallback is `games/registry.txt` (one FQN per line) for environments where classpath scanning is problematic.
- **JLine 3** for all raw terminal input and ANSI output in the core TUI. Games may use or ignore it.
- **Core gets JUnit 6 tests.** Games do not have a testing requirement.
- **`core/` is protected.** No game PR should touch it. Games live under `games/`.

---

## Package Structure

```
src/main/java/
  core/
    launcher/
      Main.java                  ← entry point, boots everything
      ArcadeLauncher.java        ← splash → roulette → menu → game loop
    tui/
      Terminal.java              ← JLine wrapper, raw mode, ANSI, cursor
      Menu.java                  ← arrow-key navigable list component
      MenuOption.java            ← single item: title + detail string
      Spinner.java               ← loading/suspense animation
      Color.java                 ← ANSI color constants + helpers
    game/
      Game.java                  ← THE interface (2 methods)
      GameMetadata.java          ← name, description, author (from annotation)
      GameContext.java           ← what games receive at launch
      GameResult.java            ← what games return (win/loss/score/quit/custom)
      GameRegistry.java          ← discovers + instantiates all @ArcadeGame classes
      ArcadeGame.java            ← the annotation itself
    save/
      SaveManager.java           ← read/write interface to save file
      SaveFile.java              ← full data model (players map + global map)
      PlayerProfile.java         ← per-player key-value store
  games/
    roulette/
      RussianRoulette.java       ← reference implementation + the origin game
      RouletteResult.java        ← (optional helper)

src/test/java/
  core/
    tui/                         ← headless-testable TUI logic
    game/                        ← GameRegistry tests
    save/                        ← SaveManager round-trip tests
```

---

## The Interfaces (Canonical Definitions)

These are the contract. Do not change signatures without flagging.

```java
// Game.java
public interface Game {
    GameMetadata getMetadata();
    GameResult launch(GameContext context);
}

// GameContext.java — constructor-injected by launcher
public class GameContext {
    public SaveManager getSaveManager();
    public String getPlayerName();
    public Terminal getTerminal();   // may be null if game doesn't use TUI
}

// GameResult.java — factory methods only, no public constructor
public class GameResult {
    public static GameResult win(String message);
    public static GameResult loss(String message);
    public static GameResult quit();
    public static GameResult score(int score);
    public static GameResult custom(String status, String message);

    public String getStatus();   // "win" | "loss" | "quit" | "score" | custom
    public String getMessage();  // nullable
    public Integer getScore();   // nullable
}

// @ArcadeGame annotation
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ArcadeGame {
    String name();
    String description();
    String author();
}

// SaveManager.java — key interface
public interface SaveManager {
    PlayerProfile getProfile(String playerName);
    void save();
}

// PlayerProfile.java — flat key-value per player
public class PlayerProfile {
    public String getName();
    public int getInt(String key, int defaultValue);
    public String getString(String key, String defaultValue);
    public boolean getBoolean(String key, boolean defaultValue);
    public void set(String key, Object value);
}
```

---

## Save File Schema

```json
{
  "players": {
    "player1": {
      "name": "player1",
      "roulette.survived": 7,
      "roulette.deaths": 2,
      "mygame.highscore": 420
    }
  },
  "global": {
    "total_sessions": 42
  }
}
```

Games namespace their keys as `gamename.keyname`. Convention only — not enforced by the framework.

---

## Launcher Flow

```
Main.main()
  └─ ArcadeLauncher.run()
       ├─ ask for player name (or load last used)
       ├─ splash screen (ASCII art, brief)
       ├─ RussianRoulette.launch(context)
       │    ├─ DEATH → log to save, print exit message, System.exit(0)
       │    └─ SURVIVE → continue
       ├─ Menu.show(registeredGames)
       │    └─ user picks game (arrow keys + Enter) or quits
       ├─ game.launch(context)  ← blocks until game returns
       ├─ show GameResult message
       └─ back to menu (loop)
```

---

## PR Contract for Games (Enforce This)

A game PR is mergeable if:
1. Compiles with `./gradlew build`
2. Implements `Game` interface
3. Has `@ArcadeGame(name, description, author)`
4. Does not touch `core/`
5. Does not write to save keys outside its own namespace
6. No network calls at runtime
7. No writing files outside the project directory
8. No grossly offensive content

---

## Testing Scope for Core

JUnit 6 tests required for:
- `SaveManager` — round-trip read/write, default values, namespace isolation
- `GameRegistry` — finds annotated classes, handles missing/broken registrations gracefully
- `PlayerProfile` — get/set/default behavior
- `GameResult` — factory methods, field accessors
- `Menu` — state logic (selection index, boundary clamping) — headless, no terminal
- `Terminal` — only the parts testable without a real TTY (ANSI string building, color helpers)

Do NOT try to test actual terminal I/O or raw mode in unit tests.

---

## Current State

> **Update this section at the end of every session.**

- [x] Project created in IntelliJ (Gradle, Kotlin DSL, JDK 21, GroupId `com.mpaesold.arcade`, ArtifactId `the-arcade`)
- [x] `build.gradle.kts` dependencies added + `application` plugin + `mainClass = core.launcher.Main` + `workingDir = projectDir`
- [x] Package structure created (all directories; `.gitkeep` removed from src dirs, kept in `save/`)
- [x] `core/tui/` implemented (`Color`, `Terminal`, `MenuOption`, `Menu`, `Spinner`, `MenuDemo`)
- [x] `core/game/` interfaces implemented (`ArcadeGame`, `GameMetadata`, `GameResult`, `Game`, `GameContext`, `GameRegistry`)
- [ ] `core/save/` implemented (stubs written: `SaveManager` interface + `PlayerProfile` skeleton — Session 4 will flesh out)
- [ ] `core/launcher/` implemented
- [ ] `games/roulette/` implemented
- [x] Core TUI tests written (`ColorTest`, `MenuStateTest` — all passing)
- [x] `core/game/` tests written (`GameResultTest`, `GameRegistryTest` — all passing)
- [ ] End-to-end launch works

**Currently working on:** _nothing — Session 3 complete_  
**Last decision made:** `GameContext` forward-references `core.save.SaveManager`; minimal stubs for `SaveManager` and `PlayerProfile` were added to unblock compilation — they throw `UnsupportedOperationException` and will be replaced in Session 4.  
**Blockers / open questions:** _nothing_

---

## Notes for Next Session

- Next session: Session 4 — `core/save/` (`SaveFile`, full `SaveManager` impl, full `PlayerProfile` impl) + `SaveManager` round-trip tests
- `MenuDemo` in `core/tui/MenuDemo.java` is a temporary smoke-test — delete before v0.1.0

## Windows Terminal Notes (do not re-debate)

These were learned the hard way in Session 2. Do not try to change them.

- **Raw ANSI cursor/screen bytes do NOT work** on Windows via `jline.writer()`. JLine's `WindowsAnsiOutputStream` handles SGR (color) codes via Windows API but passes other sequences through as raw bytes, which appear as text without VT processing.
- **Use `jline.puts(InfoCmp.Capability.*)`** for all cursor/screen control — this routes through JLine's own mechanism and works correctly.
- **Use `clearScreen()` per render cycle** instead of `moveCursor(1,1)` + `clearLine()`. `cursor_address` capability does not work correctly on this setup; `clear_screen` does.
- **UTF-8**: `SetConsoleOutputCP(65001)` + `SetConsoleCP(65001)` called via JNA in `Terminal()` constructor — eliminates need for `chcp 65001`.
- **Arrow keys**: use `BindingReader` + `KeyMap` — manual ESC sequence parsing races and fails on Windows.
- **Run the demo**: `demo.bat` (project root) — builds via `gradlew installDist` then launches with `%JAVA_HOME%\bin\java` directly (bypasses Gradle's stdin pipe). Requires `JAVA_HOME` set to JDK 21 and `%JAVA_HOME%\bin` at the top of the system PATH.
- **`gradlew menuDemo` does NOT work for interactive TUI** — Gradle always forks a daemon that has no console attached, so JLine falls back to dumb terminal. Cannot be fixed from the build script.