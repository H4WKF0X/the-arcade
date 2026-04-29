# CONCEPT.md — The Arcade

> *Place this file in the project root, next to `README.md`, `CLAUDE.md`, and `TODO.md`.*

---

## Origin

This project started as a Russian Roulette game written by Maximilian Paesold ([@H4WKF0X](https://github.com/H4WKF0X)) — one of his first ever programs. It was written to understand how function parameters work. The nested `if` statements were architectural. The JOptionPane popups were everywhere. It was handed in to a teacher and the conversation that followed was legendary.

Years later, with more knowledge and worse excuses not to build something, The Arcade was born from that same program.

---

## What This Is

The Arcade is a community Java game collection held together by a shared launcher, a shared terminal UI engine, and a single save file.

Every session starts with Russian Roulette. That is not configurable. That is the point.

If you survive, you pick a game from a menu and play it. The menu is navigable with arrow keys. Hovering over a game shows a short description. The whole thing runs in a CLI that feels like more than a CLI — styled, interactive, no Enter key required.

Anyone can add a game by opening a pull request. The PR will not be reviewed for game quality, game content, game completeness, or game sanity. It will be reviewed for exactly one thing: does it correctly implement the game contract and leave the core alone.

---

## The Rules

### For the Core (`core/`)

The core is the framework. It contains the terminal engine, the menu system, the game interfaces, the registry, and the save file manager. It is maintained by the project owner and reviewed carefully. PRs touching `core/` go through real review.

The core must have tests. JUnit 5 unit tests covering `SaveManager`, `GameRegistry`, and the headless-testable parts of the TUI (rendering logic, menu state, key event handling). The core should not break silently.

### For Games (`games/`)

Open season. A game PR gets merged if:

1. It compiles
2. It implements the `Game` interface
3. It is annotated with `@ArcadeGame` (name, description, author)
4. It does not touch anything in `core/`
5. It does not write to save keys outside its own namespace

That is the entire checklist.

### Hard Lines (the short list of actual no's)

- No content that is grossly offensive or hateful
- No games that require external API keys or network access at runtime
- No games that phone home or collect any data
- No games that write files outside the project directory

Everything else — unfinished games, weird games, Swing windows, JavaFX, ASCII art, tech demos, ironic JOptionPane usage — is welcome.

---

## The Save File

One file. `arcade_save.json`. Auto-created on first run. Gitignored.

The save file is single-player for now — one active profile per installation. All game data lives under that profile. Games namespace their own keys by convention (`mygame.keyname`). There is no enforcement, only trust.

The `SaveManager` is the only permitted interface to this file. No game should ever open or parse the JSON directly.

---

## The Spirit

The project is deliberately low-friction for contributors and high-friction for session starts. You have to play Russian Roulette every time. You might die immediately. That is funny and intended.

The game collection will be random, uneven, and probably include things that have no business being in the same program as each other. That is also intended. The point is the contrast — a single polished launcher shell containing whatever people felt like making.

The origin game stays. The nested-if-statement era is honored. The JOptionPane is a valid UI choice for anyone who wants to pay tribute.

---

## Repository

[https://github.com/H4WKF0X/the-arcade](https://github.com/H4WKF0X/the-arcade)

---

## Build Notes for Contributors

- Java 21, Gradle with Kotlin DSL
- JLine 3 for terminal raw mode and cross-platform ANSI
- Gson for save file serialization
- Reflections library for `@ArcadeGame` classpath scanning (or fallback `registry.txt`)
- JUnit **5** (not 6 — as of writing, JUnit 6 does not exist; the BOM version should be `5.11.4`)
- Games may add their own dependencies freely by editing `build.gradle.kts`
- Run configurations should set working directory to project root so the save file resolves correctly