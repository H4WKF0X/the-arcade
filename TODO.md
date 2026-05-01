# TODO.md — The Arcade

> Shared task board. Claude Code reads and updates this. So does the owner.
> Start a Claude Code session with: *"Read CLAUDE.md and TODO.md, then let's work on: [item]"*

---

## 🔧 In Progress

_Nothing yet._

---

## 📋 Up Next (ordered)

### Session 5 — Russian Roulette (`games/roulette/`)
- [ ] Strengthen `GameRegistryTest` — add test that a class with `@ArcadeGame` in the `games` package is discovered; add test that a class without the annotation is excluded (currently vacuous assertions)
- [ ] `RussianRoulette.java` — implements `Game`, annotated with `@ArcadeGame`
- [ ] Six-chamber logic, one bullet, weighted random
- [ ] TUI: cylinder spin animation (Spinner), single keypress trigger, BANG vs click outcome
- [ ] On death: increment `roulette.deaths` in save, return `GameResult.loss("BANG.")`
- [ ] On survive: increment `roulette.survived` in save, return `GameResult.win("*click*")`
- [ ] This is the reference implementation — comments should explain the pattern for future contributors

### Session 6 — Launcher (`core/launcher/`)
- [ ] `Main.java` — entry point, calls `ArcadeLauncher.run()`
- [ ] `ArcadeLauncher.java` — full flow: player name prompt → splash → roulette → menu loop → game dispatch → result display → repeat
- [ ] ASCII art splash screen
- [ ] Death exits the program (after save). Survive continues to menu.
- [ ] Handle `GameResult.quit()` returning to menu vs exiting
- [ ] End-to-end smoke test: launch, type a name, survive roulette (mock?), pick a game, quit

### Session 7 — Polish & Docs
- [ ] Update `README.md` to reflect actual implemented interface signatures
- [ ] Add `CONTRIBUTING.md` (short — just the PR contract from `CLAUDE.md`)
- [ ] Check all core Javadoc is present
- [ ] Run full test suite, fix anything broken
- [ ] Tag `v0.1.0`

---

## 🚧 Blocked / Decisions Needed

_None currently._

---

## 💡 Future / Backlog (no commitment)

- [ ] Leaderboard view in main menu (aggregated scores from save file)
- [ ] Player profile screen (survival rate, games played, etc.)
- [ ] `--list` CLI flag to print registered games and exit
- [ ] Per-game launch arguments passed through context
- [ ] `GameContext.getAudio()` stub for simple tones via Java Sound API
- [ ] Game metadata tags (difficulty, estimated time, genre)
- [ ] Headless/CI test mode that skips raw terminal

---

## ✅ Done

### Session 4 — Core Save System (`core/save/`)
- [x] `PlayerProfile.java` — flat key-value, typed getters with defaults, `set()`; handles Gson Double-for-int coercion
- [x] `SaveFile.java` — players map + global map, Gson serialization
- [x] `SaveManager.java` — interface
- [x] `JsonSaveManager.java` — loads from / writes to `save/arcade_save.json` via Gson; `File` constructor arg for testability
- [x] JUnit 6 tests: `PlayerProfileTest` (9 cases), `SaveManagerTest` (9 cases) — all passing
- [x] Fixed: corrupted JSON file now returns empty save (was leaking `JsonSyntaxException`)
- [x] Added: `getGlobal()` on `JsonSaveManager` (not on interface) for launcher use

### Session 3 — Core Game Interfaces (`core/game/`)
- [x] `ArcadeGame.java` — the annotation
- [x] `GameMetadata.java` — name, description, author
- [x] `GameResult.java` — factory methods + accessors
- [x] `Game.java` — the interface
- [x] `GameContext.java` — SaveManager + playerName + Terminal
- [x] `GameRegistry.java` — scans classpath for `@ArcadeGame`, instantiates, builds list; fallback reads `games/registry.txt`
- [x] JUnit 6 tests: `GameResultTest`, `GameRegistryTest` — all passing
- [x] `SaveManager` + `PlayerProfile` stubs added to unblock compilation (Session 4 replaces them)

### Session 2 — Core TUI (`core/tui/`)
- [x] `Color.java` — ANSI constants + `red()`, `green()`, `yellow()`, `bold()`, `dim()`, `cyan()` helpers
- [x] `Terminal.java` — JLine 3 wrapper: raw mode on/off, `readKey()`, `moveCursor()`, `clearLine()`, `clearScreen()`, `print()`, `println()`
- [x] `MenuOption.java` — record with `title` + `detail`, factory `of()` overloads
- [x] `Menu.java` — renders list, handles UP/DOWN/ENTER/ESC, detail panel on hover, returns selected index or -1 for escape
- [x] `Spinner.java` — braille-frame animated spinner with `start(msg)` / `stop(msg)` / `pause()` static helper
- [x] `MenuDemo.java` — temporary smoke-test main method (spinner + menu end-to-end)
- [x] JUnit 6 tests: `ColorTest` (constants + helpers), `MenuStateTest` (13 cases, headless) — all passing

### Session 1 — Project Skeleton
- [x] Verify `build.gradle.kts` has correct dependencies (JLine 3, Gson, Reflections, JUnit 6 BOM `6.0.0`)
- [x] Add `application` plugin to `build.gradle.kts`, set `mainClass` to `core.launcher.Main`
- [x] Set run task `workingDir = projectDir` in `build.gradle.kts`
- [x] Create all empty packages under `src/main/java/` and `src/test/java/`
- [x] Create `.gitignore` entry for `save/arcade_save.json`
- [x] Create `save/` directory with `.gitkeep`