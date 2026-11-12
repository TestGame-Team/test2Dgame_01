# Test2DGame

A Java 21 2D game project built with Swing/AWT and managed as an Eclipse Java project.

## Run locally

1. Import this repository into Eclipse using **File → Import → Existing Projects into Workspace**.
2. Ensure the project uses Java 21 (the checked-in Eclipse classpath targets JavaSE-21).
3. Run `src/main/Main.java` as **Java Application** (main class: `main.Main`).

The `src` and `res` folders are configured as source folders. Keep `res` on the classpath because the game loads maps and images as classpath resources.

## Controls

- **WASD / Arrow Keys:** Move; navigate the inventory while it is open.
- **I:** Open or close the inventory.
- **Enter:** Interact with objects; use or equip the selected inventory item.
- **X:** Drop the selected inventory stack.
- **Esc:** Close the inventory or back out of menus.

## Project guide

See [Content and Asset Handoff](docs/CONTENT_PIPELINE.md) for the current folder layout, tile IDs, map format, asset-loading rules, NPC/monster placement, team ownership, and a safe Eclipse test checklist.

## Important

The `Rad/` directory is an asset staging area. It is not currently configured as a runtime classpath folder, so placing an image there does not automatically make it loadable by the game.

## Working with AI coding assistants (ChatGPT, Claude, Copilot, etc.)

**Read this section and `docs/CONTENT_PIPELINE.md` before asking an AI assistant to change this project.** Give the assistant the repository context first: this is a Java 21, Eclipse-managed, Swing/AWT 2D game. The source code is under `src/`; runtime images and map files are classpath resources under `res/`. The project is already playable, so treat the existing engine as a working foundation—not a blank starter project.

### Copy this project brief into your AI assistant

> You are helping with an existing Java 21 2D game built with Swing/AWT and Eclipse. Before proposing or editing anything, inspect the current repository files and read `README.md` plus `docs/CONTENT_PIPELINE.md`. Do not assume a feature, class, asset, folder, tile ID, or API exists just because it would be convenient. Verify names, signatures, resource paths, and current behavior in the code.
>
> Preserve the working game. Prefer the smallest focused change that achieves the requested task. Do not rewrite the game loop, movement, collision, inventory, map loading, or resource-loading architecture for a content task. Do not rename or move existing assets, change tile-ID meanings, alter map dimensions, or replace working mechanics unless the task explicitly requires it and the team agrees. Never silently remove existing functionality.
>
> For NPCs, monsters, and objects, use the existing registration methods `gp.addNpc(entity)`, `gp.addMonster(entity)`, and `gp.addObject(entity)`; do not assume the arrays have a fixed capacity or write directly to a hard-coded array index. Set entity world coordinates in pixels; for tile coordinates multiply the column and row by `gp.tileSize` (currently 48). Check `AssetSetter.java` and the entity constructors before adding a spawn.
>
> Load runtime images from `res/` using the existing classpath/resource conventions. The `Rad/` directory is only an asset staging area; files placed there are not automatically available at runtime. Check `AssetManager.java` and the existing loader before choosing a resource path. Preserve exact filename capitalization and the expected frame naming/layout.
>
> Map files are fixed-size text grids for the current loader: 50 rows by 50 tile IDs per row. Check `TileManager.java` for initialized IDs before using one. Do not invent tile IDs or assume the project has a visual map editor or data-driven spawn editor.
>
> Keep ownership boundaries in mind: Rad owns map design, tiles, sprites, NPC/monster art, and content placement. Sourav coordinates integration, UI/presentation work, and deployment/packaging. Do not take over another person's area or make broad engine changes without coordinating with the team.
>
> Before editing, explain briefly which files need changing and why. Make only the requested focused changes. After editing, summarize every changed file, explain how to test it in Eclipse, and clearly disclose anything you could not run or verify. Never claim the project compiles or runs unless you actually tested it. If repository context is incomplete, ask for the missing files or inspect the repository instead of guessing.

### Rules for AI-generated changes

- Ask the assistant to inspect the latest `main` before making recommendations; do not rely on an old chat's memory of the repository.
- Keep changes small and review the diff before accepting them. Avoid unrelated cleanup, dependency changes, new frameworks, or speculative optimizations.
- Do not let an AI assistant overwrite local work, force-reset the repository, or change branches as a shortcut. Preserve uncommitted work and coordinate Git operations with the team.
- Have the person responsible for the affected area review the change. Run **Project → Clean** and launch `main.Main` in Eclipse after pulling changes.
- If something fails, give the assistant the exact error, stack trace, changed files, and commit being tested. Ask it to diagnose the actual cause before suggesting another edit.

**Golden rule:** use AI to accelerate understanding and implementation, not to bypass understanding, code review, or testing. The team—not the AI—owns the final changes.

