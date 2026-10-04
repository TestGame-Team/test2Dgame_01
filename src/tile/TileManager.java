package tile;

import java.awt.Graphics2D;
import content.AssetManager;
import main.GamePanel;
import world.WorldMap;

public class TileManager {

    private final GamePanel gp;
    public final Tile[] tile;
    private final WorldMap worldMap;

    public TileManager(GamePanel gp, WorldMap worldMap) {
        this.gp = gp;
        this.worldMap = worldMap;
        tile = new Tile[50];
        getTileImage();
    }

    public void getTileImage() {

        // PLACEHOLDER
        setup(0, "grass00", false);
        setup(1, "grass00", false);
        setup(2, "grass00", false);
        setup(3, "grass00", false);
        setup(4, "grass00", false);
        setup(5, "grass00", false);
        setup(6, "grass00", false);
        setup(7, "grass00", false);
        setup(8, "grass00", false);
        setup(9, "grass00", false);
        // PLACEHOLDER

        setup(10, "grass00", false);
        setup(11, "grass01", false);
        setup(12, "water00", true);
        setup(13, "water01", true);
        setup(14, "water02", true);
        setup(15, "water03", true);
        setup(16, "water04", true);
        setup(17, "water05", true);
        setup(18, "water06", true);
        setup(19, "water07", true);
        setup(20, "water08", true);
        setup(21, "water09", true);
        setup(22, "water10", true);
        setup(23, "water11", true);
        setup(24, "water12", true);
        setup(25, "water13", true);
        setup(26, "road00", false);
        setup(27, "road01", false);
        setup(28, "road02", false);
        setup(29, "road03", false);
        setup(30, "road04", false);
        setup(31, "road05", false);
        setup(32, "road06", false);
        setup(33, "road07", false);
        setup(34, "road08", false);
        setup(35, "road09", false);
        setup(36, "road10", false);
        setup(37, "road11", false);
        setup(38, "road12", false);
        setup(39, "earth", false);
        setup(40, "wall", true);
        setup(41, "tree", true);

        // PLACEHOLDER
        setup(42, "tree", true);
        setup(43, "tree", true);
        setup(44, "tree", true);
        setup(45, "tree", true);
        setup(46, "tree", true);
        setup(47, "tree", true);
        setup(48, "tree", true);
        setup(49, "tree", true);
        // PLACEHOLDER
    }

    public void setup(int index, String imageName, boolean collision) {
        tile[index] = new Tile();
        tile[index].image = AssetManager.getImage(
                "/tiles/" + imageName,
                gp.tileSize,
                gp.tileSize
        );
        tile[index].collision = collision;
    }

    public int getTileId(int col, int row) {
        return worldMap.getTileId(col, row);
    }

    public void draw(Graphics2D g2) {
        int startCol = Math.max(
                0,
                (gp.player.worldX - gp.player.screenX) / gp.tileSize
        );
        int endCol = Math.min(
                gp.maxWorldCol - 1,
                (gp.player.worldX + gp.player.screenX) / gp.tileSize + 1
        );
        int startRow = Math.max(
                0,
                (gp.player.worldY - gp.player.screenY) / gp.tileSize
        );
        int endRow = Math.min(
                gp.maxWorldRow - 1,
                (gp.player.worldY + gp.player.screenY) / gp.tileSize + 1
        );

        for (int worldRow = startRow; worldRow <= endRow; worldRow++) {
            for (int worldCol = startCol; worldCol <= endCol; worldCol++) {
                int tileNum = worldMap.getTileId(worldCol, worldRow);
                int worldX = worldCol * gp.tileSize;
                int worldY = worldRow * gp.tileSize;
                int screenX = worldX - gp.player.worldX + gp.player.screenX;
                int screenY = worldY - gp.player.worldY + gp.player.screenY;

                if (tile[tileNum] == null) {
                    throw new IllegalStateException(
                            "Tile " + tileNum + " is not initialized at "
                            + worldCol + "," + worldRow
                    );
                }

                g2.drawImage(tile[tileNum].image, screenX, screenY, null);
            }
        }
    }
}
