package main;

public class EventHandler {

    private final GamePanel gp;
    private final EventRect[][] eventRect;

    private int prevEventX;
    private int prevEventY;
    private boolean safeEvent = true;

    public EventHandler(GamePanel gp) {
        this.gp = gp;
        eventRect = new EventRect[gp.maxWorldCol][gp.maxWorldRow];

        for (int col = 0; col < gp.maxWorldCol; col++) {
            for (int row = 0; row < gp.maxWorldRow; row++) {
                eventRect[col][row] = new EventRect();
                eventRect[col][row].setBounds(23, 23, 2, 2);
                eventRect[col][row].eventRectDefaultX = 23;
                eventRect[col][row].eventRectDefaultY = 23;
            }
        }
    }

    public void checkEvent() {
        int xDistance = Math.abs(gp.player.worldX - prevEventX);
        int yDistance = Math.abs(gp.player.worldY - prevEventY);
        if (Math.max(xDistance, yDistance) > gp.tileSize) {
            safeEvent = true;
        }

        if (!safeEvent) {
            return;
        }

        // Existing world events.
        if (hit(29, 21, "right") || hit(29, 21, "left")) {
            damagePit();
            return;
        }

        if (hit(23, 12, "up")) {
            healPool();
            return;
        }

        // Temporary map cycle using the existing 50x50 placeholder maps.
        if (gp.activeMapResource.equals("/maps/big-map_02.txt")
                && hit(27, 16, "any") && gp.keyH.enterPressed) {
            gp.changeMap("/maps/big-map_03.txt", 27, 16);
            return;
        }

        if (gp.activeMapResource.equals("/maps/big-map_03.txt")
                && hit(27, 16, "any") && gp.keyH.enterPressed) {
            gp.changeMap("/maps/big-map_04.txt", 25, 25);
            return;
        }

        if (gp.activeMapResource.equals("/maps/big-map_04.txt")
                && hit(25, 25, "any") && gp.keyH.enterPressed) {
            gp.changeMap("/maps/big-map_02.txt", 27, 16);
        }
    }

    private boolean hit(int col, int row, String requiredDirection) {
        boolean collided = false;

        gp.player.solidArea.x += gp.player.worldX;
        gp.player.solidArea.y += gp.player.worldY;
        EventRect target = eventRect[col][row];
        target.x += col * gp.tileSize;
        target.y += row * gp.tileSize;

        if (gp.player.solidArea.intersects(target) && !target.eventDone
                && (gp.player.direction.equals(requiredDirection)
                    || "any".equals(requiredDirection))) {
            collided = true;
            prevEventX = gp.player.worldX;
            prevEventY = gp.player.worldY;
        }

        gp.player.solidArea.x = gp.player.solidAreaDefaultX;
        gp.player.solidArea.y = gp.player.solidAreaDefaultY;
        target.x = target.eventRectDefaultX;
        target.y = target.eventRectDefaultY;
        return collided;
    }

    public void resetAfterMapChange() {
        prevEventX = gp.player.worldX;
        prevEventY = gp.player.worldY;
        safeEvent = false;
    }

    private void damagePit() {
        gp.gameState = gp.dialogueState;
        gp.ui.currentDialogue = "You Fall into a Pit!!!";
        gp.player.life--;
        safeEvent = false;
    }

    private void healPool() {
        if (gp.keyH.enterPressed) {
            gp.gameState = gp.dialogueState;
            gp.ui.currentDialogue = "You have been healed!!!";
            gp.player.life = gp.player.maxLife;
            safeEvent = false;
        }
    }

}
