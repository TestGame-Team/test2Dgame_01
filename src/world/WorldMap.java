package world;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Holds map tile data independently from tile rendering.
 * The current text-map format stays unchanged so existing maps remain usable.
 */
public final class WorldMap {

    private final int width;
    private final int height;
    private final int[][] tileIds;

    public WorldMap(int width, int height) {
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException("Map dimensions must be positive.");
        }

        this.width = width;
        this.height = height;
        this.tileIds = new int[width][height];
    }

    public void load(String resourcePath) {
        try (InputStream input = WorldMap.class.getResourceAsStream(resourcePath)) {
            if (input == null) {
                throw new IllegalArgumentException("Map resource not found: " + resourcePath);
            }

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(input))) {
                for (int row = 0; row < height; row++) {
                    String line = reader.readLine();

                    if (line == null) {
                        throw new IllegalStateException(
                                "Map ended early at row " + row + ": " + resourcePath);
                    }

                    String[] values = line.trim().split("\\s+");

                    if (values.length < width) {
                        throw new IllegalStateException(
                                "Map row " + row + " has " + values.length
                                + " tiles; expected " + width + ".");
                    }

                    for (int col = 0; col < width; col++) {
                        tileIds[col][row] = Integer.parseInt(values[col]);
                    }
                }
            }
        } catch (IOException | NumberFormatException e) {
            throw new IllegalStateException("Failed to load map: " + resourcePath, e);
        }
    }

    public int getTileId(int col, int row) {
        if (col < 0 || col >= width || row < 0 || row >= height) {
            throw new IndexOutOfBoundsException(
                    "Map coordinate outside bounds: " + col + "," + row);
        }

        return tileIds[col][row];
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
