# Test2DGame

A Java 21 2D game project built with Swing/AWT and managed as an Eclipse Java project.

## Run locally

1. Import this repository into Eclipse using **File → Import → Existing Projects into Workspace**.
2. Ensure the project uses Java 21 (the checked-in Eclipse classpath targets JavaSE-21).
3. Run `src/main/Main.java` as **Java Application** (main class: `main.Main`).

The `src` and `res` folders are configured as source folders. Keep `res` on the classpath because the game loads maps and images as classpath resources.

## Project guide

See [Content and Asset Handoff](docs/CONTENT_PIPELINE.md) for the current folder layout, tile IDs, map format, asset-loading rules, NPC/monster placement, team ownership, and a safe Eclipse test checklist.

## Important

The `Rad/` directory is an asset staging area. It is not currently configured as a runtime classpath folder, so placing an image there does not automatically make it loadable by the game.
