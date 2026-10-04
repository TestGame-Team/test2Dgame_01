package main;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Properties;

import entity.Player;

public final class SaveManager {

    private static final int SLOT_COUNT = 3;
    private static final String SAVE_VERSION = "1";

    private final GamePanel gp;
    private final Path saveDirectory;
    private int nextSlot = 1;
    private long saveCounter = 0;

    public SaveManager(GamePanel gp) {
        this.gp = gp;
        saveDirectory = Paths.get(System.getProperty("user.home"), ".test2dgame", "saves");
        loadRotationState();
    }

    public int saveGame() {
        int slot = nextSlot;
        Properties data = new Properties();
        Player p = gp.player;

        saveCounter++;
        data.setProperty("version", SAVE_VERSION);
        data.setProperty("saveNumber", Long.toString(saveCounter));
        data.setProperty("player.worldX", Integer.toString(p.worldX));
        data.setProperty("player.worldY", Integer.toString(p.worldY));
        data.setProperty("player.life", Integer.toString(p.life));
        data.setProperty("player.maxLife", Integer.toString(p.maxLife));
        data.setProperty("player.speed", Integer.toString(p.speed));
        data.setProperty("player.direction", p.direction);
        data.setProperty("savedAt", Long.toString(System.currentTimeMillis()));

        try {
            Files.createDirectories(saveDirectory);
            try (OutputStream out = Files.newOutputStream(slotPath(slot),
                    StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
                data.store(out, "Test2DGame save slot " + slot);
            }

            nextSlot = nextSlot % SLOT_COUNT + 1;
            saveRotationState();
            return slot;
        } catch (IOException e) {
            throw new IllegalStateException("Could not save game to slot " + slot, e);
        }
    }

    public boolean loadGame(int slot) {
        if (!isValidSlot(slot) || !Files.exists(slotPath(slot))) {
            return false;
        }

        Properties data = new Properties();

        try (InputStream in = Files.newInputStream(slotPath(slot))) {
            data.load(in);

            if (!SAVE_VERSION.equals(data.getProperty("version"))) {
                return false;
            }

            Player p = gp.player;
            p.worldX = getInt(data, "player.worldX", p.worldX);
            p.worldY = getInt(data, "player.worldY", p.worldY);
            p.maxLife = getInt(data, "player.maxLife", p.maxLife);
            p.life = Math.max(0, Math.min(
                    getInt(data, "player.life", p.maxLife), p.maxLife));
            p.speed = getInt(data, "player.speed", p.speed);
            p.direction = data.getProperty("player.direction", "down");

            saveCounter = Math.max(
                    saveCounter, getLong(data, "saveNumber", saveCounter));

            return true;
        } catch (IOException | RuntimeException e) {
            return false;
        }
    }

    /** Clears the first occupied slot in slot order: 1, then 2, then 3. */
    public int clearNextSlot() {
        for (int slot = 1; slot <= SLOT_COUNT; slot++) {
            if (hasSave(slot)) {
                try {
                    Files.deleteIfExists(slotPath(slot));
                    return slot;
                } catch (IOException e) {
                    return 0;
                }
            }
        }
        return 0;
    }

    public boolean hasSave(int slot) {
        return isValidSlot(slot) && Files.exists(slotPath(slot));
    }

    public String getSlotLabel(int slot) {
        if (!hasSave(slot)) {
            return "SLOT " + slot + "   - EMPTY";
        }

        Properties data = readSlot(slot);
        long number = getLong(data, "saveNumber", 0);
        return "SLOT " + slot + "   - SAVE #" + number;
    }

    public int getNextSlot() {
        return nextSlot;
    }

    private Properties readSlot(int slot) {
        Properties data = new Properties();

        try (InputStream in = Files.newInputStream(slotPath(slot))) {
            data.load(in);
        } catch (IOException ignored) {
        }

        return data;
    }

    private void loadRotationState() {
        Path rotationPath = saveDirectory.resolve("rotation.dat");

        if (!Files.exists(rotationPath)) {
            return;
        }

        Properties data = new Properties();

        try (InputStream in = Files.newInputStream(rotationPath)) {
            data.load(in);
            nextSlot = getInt(data, "nextSlot", 1);
            saveCounter = getLong(data, "saveCounter", 0);

            if (!isValidSlot(nextSlot)) {
                nextSlot = 1;
            }
        } catch (IOException | RuntimeException ignored) {
            nextSlot = 1;
            saveCounter = 0;
        }
    }

    private void saveRotationState() throws IOException {
        Files.createDirectories(saveDirectory);

        Properties data = new Properties();
        data.setProperty("nextSlot", Integer.toString(nextSlot));
        data.setProperty("saveCounter", Long.toString(saveCounter));

        try (OutputStream out = Files.newOutputStream(
                saveDirectory.resolve("rotation.dat"),
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING)) {
            data.store(out, "Test2DGame save rotation");
        }
    }

    private Path slotPath(int slot) {
        return saveDirectory.resolve("save" + slot + ".dat");
    }

    private boolean isValidSlot(int slot) {
        return slot >= 1 && slot <= SLOT_COUNT;
    }

    private int getInt(Properties data, String key, int fallback) {
        try {
            return Integer.parseInt(data.getProperty(key));
        } catch (Exception e) {
            return fallback;
        }
    }

    private long getLong(Properties data, String key, long fallback) {
        try {
            return Long.parseLong(data.getProperty(key));
        } catch (Exception e) {
            return fallback;
        }
    }
}
