package content;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import main.UtilityTool;

/**
 * Central image resource cache.
 *
 * Gameplay classes request assets by resource path instead of loading
 * image files themselves. Loaded/scaled images are cached and reused.
 */
public final class AssetManager {

    private static final Map<String, BufferedImage> IMAGE_CACHE = new HashMap<>();
    private static final UtilityTool UTILITY_TOOL = new UtilityTool();

    private AssetManager() {
    }

    public static BufferedImage getImage(String path, int width, int height) {
        String key = path + "|" + width + "x" + height;

        BufferedImage cached = IMAGE_CACHE.get(key);
        if (cached != null) {
            return cached;
        }

        BufferedImage image;
        try {
            image = ImageIO.read(AssetManager.class.getResourceAsStream(path + ".png"));
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load image: " + path, e);
        }

        if (image == null) {
            throw new IllegalArgumentException("Image resource not found: " + path + ".png");
        }

        BufferedImage scaled = UTILITY_TOOL.scaleImage(image, width, height);
        IMAGE_CACHE.put(key, scaled);
        return scaled;
    }

    public static int cachedImageCount() {
        return IMAGE_CACHE.size();
    }
}
