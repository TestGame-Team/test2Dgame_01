# Content and Asset Handoff

This guide describes the **current** project structure and the safest way to add content without changing engine code unnecessarily. It is intentionally based on the code that currently exists; it does not claim that a JSON-driven content editor or automatic sprite importer is already implemented.

## 1. Eclipse project setup

- Use **Java 21**.
- Import the repository into Eclipse as an **Existing Projects into Workspace** project.
- The checked-in `.classpath` already marks `src` and `res` as source folders and uses `bin` as the output folder.
- Run `main.Main` (source file: `src/main/Main.java`).
- If Eclipse shows stale errors after pulling, use **Project → Clean**, then refresh the project.

Do not remove the `res` source-folder entry: image and map resources are loaded from the classpath.

## 2. Where things belong

| Content | Current location | Notes |
|---|---|---|
| Java engine/game code | `src/` | Packages include `main`, `entity`, `monster`, `object`, `tile`, `world`, `inventory`, and `content`. |
| Tile images | `res/tiles/` | Loaded by `AssetManager` using a classpath resource path. |
| Maps | `res/maps/` | Plain-text tile-ID grids. |
| Player/NPC/monster/object images | Existing subfolders under `res/` | Keep paths and filenames consistent with the relevant Java loader. |
| New art supplied by Rad | `Rad/` | This is a staging folder, **not currently a classpath source folder**. Files placed here are not automatically available to the runtime. |

### Integrating art from `Rad/`

1. Keep the original files in `Rad/` as the handoff/source copy.
2. Agree on the final filename and animation-frame naming with the person integrating the character.
3. Copy the runtime-ready files into the matching existing folder under `res/` (or add a deliberate resource folder to Eclipse's build path as a separate, tested change).
4. Update the relevant Java loader to request the exact classpath path and filename.
5. Run the game and verify the asset actually appears. A file existing in Git does not by itself mean Java can load it.

Avoid renaming or moving existing working assets unless every code reference has been checked.

## 3. Tile maps

The active map is selected in `src/main/GamePanel.java`:

```java
new WorldManager(maxWorldCol, maxWorldRow, "/maps/big-map_02.txt");
```

The current world is **50 columns × 50 rows**. A map file is whitespace-separated integer tile IDs, one row per line. Keep exactly 50 values on each of the 50 rows. The loader currently rejects missing rows or rows with too few values; it does not provide a visual map editor.

### Current tile IDs

These IDs are defined in `src/tile/TileManager.java`. The map uses the ID to select an image and collision setting.

| ID | Image in `res/tiles/` | Collision |
|---:|---|---|
| 10 | `grass00.png` | No |
| 11 | `grass01.png` | No |
| 12–25 | `water00.png`–`water13.png` | Yes |
| 26–38 | `road00.png`–`road12.png` | No |
| 39 | `earth.png` | No |
| 40 | `wall.png` | Yes |
| 41 | `tree.png` | Yes |

IDs 0–9 and 42–49 are currently placeholder entries. Do not use them for new map design unless the tile definitions are deliberately updated and tested first. Tile IDs are part of the map format: changing an ID's meaning changes every map cell that uses it.

## 4. Adding or changing NPCs and monsters

Current placement is hard-coded in `src/main/AssetSetter.java`:

- `setNPC()` creates one `NPC_OldMan` at tile coordinate (21, 22).
- `setMonster()` creates two `M_GreenSlime` instances at (11, 10) and (11, 11).
- `setObject()` is currently empty.

Coordinates are tile coordinates multiplied by `gp.tileSize` (48 pixels). The NPC, monster, and object arrays start with 10 slots for compatibility, then grow automatically when full. Register new instances through `gp.addNpc(entity)`, `gp.addMonster(entity)`, or `gp.addObject(entity)`. These methods use an empty slot first and expand only when needed. Set each entity's `worldX` and `worldY` in pixels before registering it; for tile coordinates, multiply the column and row by `gp.tileSize` (48).

For now, the safest workflow is to make placement changes in `AssetSetter.java`, not to modify the game loop. Example:

```java
M_GreenSlime slime = new M_GreenSlime(gp);
slime.worldX = 12 * gp.tileSize;
slime.worldY = 8 * gp.tileSize;
gp.addMonster(slime);
```

The existing update, collision, and draw loops continue to work with the expanded arrays. A data-file-driven spawn system is a separate feature and is not required to add more entities.

## 5. Image loading rules

`src/content/AssetManager.java` loads images from the Java classpath, appends `.png`, scales them to the requested dimensions, and caches the scaled result.

For example, a request for `/tiles/grass00` resolves to:

```
res/tiles/grass00.png
```

Use a leading slash in resource paths, omit the `.png` suffix when calling `AssetManager.getImage`, and check exact capitalization. Keep image dimensions/animation frame layout consistent with the loader that consumes them.

## 6. Who owns what

- **Engine/backend:** preserve game loop, movement, collision, interactions, inventory, save/load, resource loading, and integration boundaries. Avoid broad refactors while content is being added.
- **Rad (art/content):** provide final sprites, tiles, and other visual assets; include the intended filename, frame order, and target in-game role with each handoff.
- **Sourav (integration/deployment):** take a bounded task such as integrating the agreed asset set, checking the deployment/demo path, and preparing the final run/submission checklist. Keep engine changes separate unless explicitly coordinated.
- **Whole team:** test the same latest branch/commit in Eclipse before the final submission.

## 7. Safe change and test checklist

For each content change:

1. Pull the latest agreed branch and note the commit being tested.
2. Keep the change focused; do not combine asset renames, map redesign, and engine refactors in one commit.
3. Check that resource filenames and capitalization match the Java paths exactly.
4. For map edits, confirm 50 rows and 50 tile IDs per row; use only initialized tile IDs.
5. In Eclipse, run **Project → Clean**, then run `main.Main`.
6. Verify the title screen, starting a game, player movement, map rendering, collision, NPC/monster visibility, pause/inventory, and save/load if those flows are touched.
7. If something breaks, report the commit hash, exact steps, and the full first exception/compiler error.

## 8. Current limitations to keep in mind

- Tile definitions and entity spawn positions are currently Java-coded, not loaded from a general content configuration file.
- The `Rad/` folder is currently a staging area and is not listed as a source folder in `.classpath`.
- The current map loader expects a fixed-size text grid.
- This guide documents the current working arrangement; it does not replace a local Eclipse run or teammate review.
