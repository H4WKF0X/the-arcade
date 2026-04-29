# TODO.md — The Arcade

> Shared task board. Claude Code reads and updates this. So does the owner.
> Start a Claude Code session with: *"Read CLAUDE.md and TODO.md, then let's work on: [item]"*

---

## 🔧 In Progress

_Nothing yet._

---

## 📋 Up Next (ordered)

### Session 1 — Project Skeleton
- [ ] Verify `build.gradle.kts` has correct dependencies (JLine 3, Gson, Reflections, JUnit 6 BOM `6.0.0`)
- [ ] Add `application` plugin to `build.gradle.kts`, set `mainClass` to `core.launcher.Main`
- [ ] Set run configuration working directory to project root (save file path resolution)
- [ ] Create all empty packages under `src/main/java/` and `src/test/java/`
- [ ] Create `.gitignore` entry for `save/arcade_save.json`
- [ ] Create `save/` directory with `.gitkeep`

### Session 2 — Core TUI (`core/tui/`)
- [ ] `Color.java` — ANSI constants + `red()`, `green()`, `yellow()`, `bold()`, `dim()` helpers
- [ ] `Terminal.java` — JLine 3 wrapper: raw mode on/off, `readKey()`, `moveCursor()`, `clearLine()`, `clearScreen()`, `print()`, `println()`
- [ ] `MenuOption.java` — title + detail string, simple record or POJO
- [ ] `Menu.java` — renders list, handles UP/DOWN/ENTER/ESC, shows detail panel on hover, returns selected index or -1 for escape
- [ ] `Spinner.java` — simple animated spinner for suspense moments
- [ ] Write a `MenuDemo` main method (can be deleted later) that shows the menu working end-to-end
- [ ] JUnit 6 tests: `Color`, headless `Menu` state logic, ANSI string helpers

### Session 3 — Core Game Interfaces (`core/game/`)
- [ ] `ArcadeGame.java` — the annotation
- [ ] `GameMetadata.java` — name, description, author; static `fromAnnotation()` helper
- [ ] `GameResult.java` — factory methods + accessors
- [ ] `Game.java` — the interface
- [ ] `GameContext.java` — SaveManager + playerName + Terminal
- [ ] `GameRegistry.java` — scans classpath for `@ArcadeGame`, instantiates, builds list; fallback reads `games/registry.txt`
- [ ] JUnit 6 tests: `GameResult` factories, `GameRegistry` with a fake annotated class

### Session 4 — Core Save System (`core/save/`)
- [ ] `PlayerProfile.java` — flat key-value, typed getters with defaults, `set()`
- [ ] `SaveFile.java` — players map + global map, Gson serialization
- [ ] `SaveManager.java` — interface
- [ ] `SaveManagerImpl.java` — loads from / writes to `arcade_save.json` via Gson, creates file if missing
- [ ] JUnit 6 tests: round-trip write/read, default values, multiple profiles, file-not-found bootstrap

### Session 5 — Russian Roulette (`games/roulette/`)
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

_Nothing yet._