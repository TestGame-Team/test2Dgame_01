package main;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import object.OBJ_Heart;
import entity.Entity;

public class UI {

    GamePanel gp;
    Graphics2D g2;
    Font font1, font2, font3, font4, font5, font6, font7;
    BufferedImage heart_full, heart_half, heart_blank;

    public boolean messageOn = false;
    public boolean gameFinish = false;
    public String message;
    public String currentDialogue;
    public int titleScreenState = 0;
    public int commandNum = 0;
    public int pauseScreenState = 0;
    public int inventoryIndex = 0;
    int msgCounter = 0;

    private final long titleAnimationStart = System.nanoTime();

    public UI(GamePanel gp) {
        this.gp = gp;
        font1 = new Font("Arial", Font.PLAIN, 20);
        font2 = new Font("Book Antiqua", Font.PLAIN, 20);
        font3 = new Font("Segoe Script", Font.PLAIN, 20);
        font4 = new Font("Calibri", Font.PLAIN, 20);
        font5 = new Font("Segoe UI", Font.PLAIN, 20);
        font6 = new Font("Trebuchet MS", Font.PLAIN, 20);

        Entity heart = new OBJ_Heart(gp);
        heart_full = heart.image;
        heart_half = heart.image2;
        heart_blank = heart.image3;
    }

    public void showMessage(String text) {
        message = text;
        messageOn = true;
    }

    public void draw(Graphics2D g2) {
        this.g2 = g2;
        msgCounter = 0;
        g2.setFont(font5);
        g2.setColor(Color.white);

        if (gp.gameState == gp.playState) {
            drawPlayerLife();
        } else if (gp.gameState == gp.pauseState) {
            drawPlayerLife();
            drawPauseScreen();
        } else if (gp.gameState == gp.dialogueState) {
            drawPlayerLife();
            drawDialogueScreen();
        } else if (gp.gameState == gp.titleState) {
            drawTitleScreen();
        } else if (gp.gameState == gp.inventoryState) {
            drawInventoryScreen();
        }
    }


    public void drawInventoryScreen() {
        g2.setColor(new Color(5, 10, 16, 205));
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

        g2.setFont(font2.deriveFont(Font.BOLD, 38F));
        String title = "INVENTORY";
        g2.setColor(new Color(225, 244, 248));
        g2.drawString(title, getXforCenteredText(title), gp.tileSize + 8);

        java.util.List<inventory.Inventory.Stack> stacks = gp.player.inventory.getStacks();
        final int columns = 5;
        final int rows = 4;
        final int cellSize = Math.max(42, Math.min(gp.tileSize + 4, (gp.screenWidth - gp.tileSize * 4) / columns));
        final int gridWidth = columns * cellSize;
        final int gridHeight = rows * cellSize;
        final int gridX = (gp.screenWidth - gridWidth) / 2;
        final int gridY = gp.tileSize * 2;

        // Borderless Terraria-style slots: subtle filled cells, no drawn outlines.
        for (int slot = 0; slot < columns * rows; slot++) {
            int col = slot % columns;
            int row = slot / columns;
            int x = gridX + col * cellSize;
            int y = gridY + row * cellSize;
            boolean selected = slot == inventoryIndex;
            g2.setColor(selected ? new Color(92, 160, 170, 205) : new Color(205, 230, 235, 28));
            g2.fillRect(x + 2, y + 2, cellSize - 4, cellSize - 4);

            if (slot < stacks.size()) {
                inventory.Inventory.Stack stack = stacks.get(slot);
                drawInventoryItemIcon(stack.item.id, x + 8, y + 7, cellSize - 16);
                if (stack.getQuantity() > 1) {
                    String amount = Integer.toString(stack.getQuantity());
                    g2.setFont(font5.deriveFont(Font.BOLD, 13F));
                    int amountWidth = g2.getFontMetrics().stringWidth(amount);
                    int tx = x + cellSize - amountWidth - 5;
                    int ty = y + cellSize - 6;
                    g2.setColor(new Color(0, 0, 0, 210));
                    g2.drawString(amount, tx + 1, ty + 1);
                    g2.setColor(Color.WHITE);
                    g2.drawString(amount, tx, ty);
                }
                if (stack.isEquipped()) {
                    g2.setFont(font5.deriveFont(Font.BOLD, 10F));
                    g2.setColor(new Color(255, 230, 130));
                    g2.drawString("E", x + 5, y + 13);
                }
            }
        }

        int selectedIndex = stacks.isEmpty() ? -1 : Math.max(0, Math.min(inventoryIndex, stacks.size() - 1));
        int panelX = gp.tileSize * 2;
        int panelY = gridY + gridHeight + 12;
        int panelWidth = gp.screenWidth - panelX * 2;
        int helpY = gp.screenHeight - gp.tileSize / 2;

        if (selectedIndex >= 0) {
            inventory.Inventory.Stack selected = stacks.get(selectedIndex);
            g2.setColor(new Color(12, 22, 29, 205));
            g2.fillRoundRect(panelX, panelY, panelWidth, Math.max(92, helpY - panelY - 22), 14, 14);

            g2.setFont(font5.deriveFont(Font.BOLD, 21F));
            g2.setColor(new Color(235, 247, 250));
            String itemTitle = selected.item.name + (selected.isEquipped() ? "  [EQUIPPED]" : "");
            g2.drawString(itemTitle, panelX + 14, panelY + 27);

            g2.setFont(font5.deriveFont(Font.PLAIN, 15F));
            g2.setColor(new Color(198, 219, 225));
            drawWrappedText(selected.item.description, panelX + 14, panelY + 50, panelWidth - 28, 19);

            g2.setFont(font5.deriveFont(Font.BOLD, 14F));
            g2.setColor(new Color(150, 220, 230));
            String action = selected.item.equippable
                    ? "ENTER  Equip / Unequip     D  Drop stack"
                    : "Not equippable     D  Drop stack";
            g2.drawString(action, panelX + 14, helpY - 36);
        } else {
            g2.setFont(font5.deriveFont(Font.PLAIN, 17F));
            g2.setColor(new Color(198, 219, 225));
            String empty = "No items yet — your next adventure starts here.";
            g2.drawString(empty, getXforCenteredText(empty), panelY + 36);
        }

        g2.setFont(font5.deriveFont(Font.PLAIN, 14F));
        g2.setColor(new Color(190, 218, 225));
        String help = "W/S or ↑/↓ Select     I/ESC Close";
        g2.drawString(help, getXforCenteredText(help), gp.screenHeight - 10);

        if (messageOn && message != null) {
            g2.setFont(font5.deriveFont(Font.BOLD, 16F));
            g2.setColor(new Color(255, 220, 150));
            g2.drawString(message, getXforCenteredText(message), gridY + gridHeight + 8);
        }
    }

    private void drawInventoryItemIcon(String itemId, int x, int y, int size) {
        Graphics2D icon = (Graphics2D) g2.create();
        try {
            if ("rusty_sword".equals(itemId)) {
                icon.setColor(new Color(225, 231, 224));
                Polygon blade = new Polygon(
                        new int[] {x + size * 3 / 5, x + size * 4 / 5, x + size / 3},
                        new int[] {y + size / 10, y + size / 5, y + size * 3 / 4}, 3);
                icon.fillPolygon(blade);
                icon.setColor(new Color(108, 126, 130));
                icon.drawPolygon(blade);
                icon.setColor(new Color(168, 111, 65));
                icon.drawLine(x + size / 3, y + size * 3 / 4, x + size / 5, y + size * 9 / 10);
                icon.setColor(new Color(224, 184, 113));
                icon.drawLine(x + size / 5, y + size * 7 / 10, x + size / 2, y + size * 4 / 5);
            } else if ("potion".equals(itemId)) {
                icon.setColor(new Color(180, 211, 220));
                icon.fillRoundRect(x + size * 2 / 5, y + size / 10, size / 5, size / 5, 4, 4);
                icon.setColor(new Color(230, 235, 226));
                icon.fillRoundRect(x + size / 4, y + size / 4, size / 2, size * 3 / 5, 9, 9);
                icon.setColor(new Color(70, 195, 157));
                icon.fillRoundRect(x + size / 4 + 3, y + size / 2, size / 2 - 6, size / 3, 7, 7);
                icon.setColor(new Color(240, 255, 248, 190));
                icon.drawLine(x + size / 3, y + size / 3, x + size / 3, y + size / 2);
            } else if ("old_coin".equals(itemId)) {
                icon.setColor(new Color(180, 124, 43));
                icon.fillOval(x + size / 6, y + size / 6, size * 2 / 3, size * 2 / 3);
                icon.setColor(new Color(255, 218, 104));
                icon.drawOval(x + size / 5, y + size / 5, size * 3 / 5, size * 3 / 5);
                icon.setFont(font5.deriveFont(Font.BOLD, Math.max(10F, size * 0.45F)));
                icon.drawString("$", x + size * 3 / 10, y + size * 7 / 10);
            } else {
                icon.setColor(new Color(200, 220, 225));
                icon.fillOval(x + size / 4, y + size / 4, size / 2, size / 2);
            }
        } finally {
            icon.dispose();
        }
    }

    private void drawWrappedText(String text, int x, int y, int maxWidth, int lineHeight) {
        if (text == null || text.isEmpty()) return;
        StringBuilder line = new StringBuilder();
        for (String word : text.split("\\s+")) {
            String candidate = line.length() == 0 ? word : line + " " + word;
            if (g2.getFontMetrics().stringWidth(candidate) > maxWidth && line.length() > 0) {
                g2.drawString(line.toString(), x, y);
                y += lineHeight;
                line.setLength(0);
                line.append(word);
            } else {
                if (line.length() > 0) line.append(' ');
                line.append(word);
            }
        }
        if (line.length() > 0) g2.drawString(line.toString(), x, y);
    }

    public void drawDebugStats(Graphics2D g2, double renderTimeMs, int fps) {
        g2.setFont(font1);
        g2.setColor(Color.white);

        String text = String.format("FPS: %d   Render: %.3f ms", fps, renderTimeMs);
        g2.setColor(Color.white);
        g2.drawString(text, 8, gp.screenHeight - 8);
    }

    public void drawPauseScreen() {
        g2.setColor(new Color(0, 0, 0, 155));
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

        g2.setFont(font2.deriveFont(Font.BOLD, 58F));
        String title = pauseScreenState == 0 ? "PAUSED"
                : pauseScreenState == 1 ? "OPTIONS" : "HELP";
        int titleX = getXforCenteredText(title);
        g2.setColor(new Color(220, 245, 250));
        g2.drawString(title, titleX, gp.tileSize * 2);

        g2.setFont(font5.deriveFont(Font.BOLD, 28F));

        if (pauseScreenState == 0) {
            drawPauseMenuItem("RESUME", 0);
            drawPauseMenuItem("OPTIONS", 1);
            drawPauseMenuItem("SAVE GAME", 2);
            drawPauseMenuItem("HELP", 3);
            drawPauseMenuItem("EXIT TO MAIN MENU", 4);
        } else if (pauseScreenState == 1) {
            drawCenteredPauseText("MUSIC VOLUME: " + gp.getMusicVolume() + "%");
            drawCenteredPauseText("SFX VOLUME: " + gp.getSfxVolume() + "%");
            drawCenteredPauseText("LEFT/RIGHT adjusts volume");
            drawCenteredPauseText("ESC to return");
        } else {
            drawCenteredPauseText("W A S D  - Move");
            drawCenteredPauseText("ENTER   - Interact");
            drawCenteredPauseText("P       - Resume");
            drawCenteredPauseText("ESC     - Back");
            drawCenteredPauseText("T       - Debug");
        }

        if (messageOn) {
            g2.setFont(font5.deriveFont(Font.BOLD, 22F));
            int messageX = getXforCenteredText(message);
            int messageY = gp.screenHeight - gp.tileSize;
            g2.setColor(new Color(180, 235, 245));
            g2.drawString(message, messageX, messageY);
        }
    }

    private void drawPauseMenuItem(String text, int index) {
        int y = gp.tileSize * 4 + index * gp.tileSize;
        int x = getXforCenteredText(text);

        if (commandNum == index) {
            g2.setColor(new Color(120, 215, 235));
            g2.drawString(">", x - gp.tileSize, y);
            g2.setColor(Color.white);
        } else {
            g2.setColor(new Color(225, 235, 240));
        }

        g2.drawString(text, x, y);
    }

    private void drawCenteredPauseText(String text) {
        int y = gp.tileSize * 4 + msgCounter;
        g2.setColor(new Color(235, 245, 250));
        g2.drawString(text, getXforCenteredText(text), y);
        msgCounter += gp.tileSize;
    }

    public int getXforCenteredText(String text) {
        int length = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        return gp.screenWidth / 2 - length / 2;
    }

    public void drawDialogueScreen() {
        int x = gp.tileSize * 2;
        int y = gp.tileSize / 2;
        int width = gp.screenWidth - (gp.tileSize * 4);
        int height = gp.tileSize * 4;

        drawSubWindow(x, y, width, height);

        g2.setFont(font3);
        g2.setColor(Color.white);
        x += gp.tileSize;
        y += gp.tileSize;

        for (String line : currentDialogue.split("\n")) {
            g2.drawString(line, x, y);
            y += 40;
        }
    }

    public void drawSubWindow(int x, int y, int width, int height) {
        g2.setColor(new Color(0, 0, 0, 150));
        g2.fillRoundRect(x, y, width, height, 36, 36);
    }

    public void drawPlayerLife() {
        int x = gp.tileSize / 2;
        int y = gp.tileSize / 2;

        for (int pl = 0; pl < gp.player.maxLife / 2; pl++) {
            g2.drawImage(heart_blank, x, y, null);

            if (gp.player.life >= (pl * 2) + 2) {
                g2.drawImage(heart_full, x, y, null);
            } else if (gp.player.life == (pl * 2) + 1) {
                g2.drawImage(heart_half, x, y, null);
            }

            x += gp.tileSize;
        }
    }

    public void drawTitleScreen() {
        double time = (System.nanoTime() - titleAnimationStart) / 1_000_000_000.0;
        float pulse = (float) (0.5 + 0.5 * Math.sin(time * 2.0));
        int bob = (int) (Math.sin(time * 2.2) * 4);

        drawTitleBackground(time);

        if (titleScreenState == 0) {
            // TITLE
            g2.setFont(font2.deriveFont(Font.BOLD, 68F));
            String text = "Blue Boy Adventure";
            int x = getXforCenteredText(text);
            int y = gp.tileSize * 3;

            g2.setColor(new Color(0, 10, 15, 150));
            g2.drawString(text, x + 5, y + 6);

            g2.setColor(new Color(175, 225, 235, 80 + (int) (pulse * 45)));
            g2.drawString(text, x - 1, y - 1);

            g2.setColor(new Color(235, 250, 255));
            g2.drawString(text, x, y);

            // CHARACTER
            x = gp.screenWidth / 2 - (gp.tileSize * 2) / 2;
            y += gp.tileSize + bob;
            g2.drawImage(gp.player.down1, x, y, gp.tileSize * 2, gp.tileSize * 2, null);

            // MAIN MENU
            final int menuFontSize = 31;
            final int menuTop = (gp.tileSize * 7);
            final int menuStep = gp.tileSize - 2;

            g2.setFont(font5.deriveFont(Font.BOLD, menuFontSize));
            g2.setColor(new Color(245, 250, 255));

            int menuHeight = menuStep * 4 + 40;
            int menuX = gp.tileSize * 2;
            int menuY = menuTop - 5;
            int menuWidth = gp.screenWidth - (gp.tileSize * 4);

            g2.setColor(new Color(5, 15, 20, 120));
            g2.fillRoundRect(menuX, menuY, menuWidth, menuHeight, 20, 20);

            g2.setColor(new Color(190, 230, 235, 170));
            g2.drawRoundRect(menuX, menuY, menuWidth, menuHeight, 20, 20);

            g2.setColor(new Color(245, 250, 255));

            drawTitleMenuItem("NEW GAME", getXforCenteredText("NEW GAME"), menuTop + menuStep * 1, 0, pulse);
            drawTitleMenuItem("LOAD GAME", getXforCenteredText("LOAD GAME"), menuTop + menuStep * 2, 1, pulse);
            drawTitleMenuItem("OPTIONS", getXforCenteredText("OPTIONS"), menuTop + menuStep * 3, 2, pulse);
            drawTitleMenuItem("EXIT", getXforCenteredText("EXIT"), menuTop + menuStep * 4, 3, pulse);

        } else if (titleScreenState == 2) {
            // LOAD GAME
            g2.setFont(font2.deriveFont(Font.BOLD, 46F));
            String text = "Load Game";
            int x = getXforCenteredText(text);
            int y = gp.tileSize * 2;

            g2.setColor(new Color(0, 10, 15, 150));
            g2.drawString(text, x + 4, y + 5);

            g2.setColor(new Color(220, 245, 250));
            g2.drawString(text, x, y);

            g2.setFont(font5.deriveFont(Font.BOLD, 27F));
            for (int i = 0; i < 3; i++) {
                String slotText = gp.saveManager.getSlotLabel(i + 1);
                int slotY = y + gp.tileSize * (2 + i);
                drawTitleMenuItem(slotText, getXforCenteredText(slotText),
                        slotY, i, pulse);
            }

            String clearText = "Clear Slot";
            int clearY = y + gp.tileSize * 5;
            drawTitleMenuItem(clearText, getXforCenteredText(clearText), clearY, 3, pulse);

            String backText = "Back";
            int backY = y + gp.tileSize * 6;
            drawTitleMenuItem(backText, getXforCenteredText(backText),
                    backY, 4, pulse);

        } else if (titleScreenState == 3) {
            g2.setFont(font2.deriveFont(Font.BOLD, 46F));
            String text = "Options";
            int x = getXforCenteredText(text);
            int y = gp.tileSize * 2;
            g2.setColor(new Color(0, 10, 15, 150));
            g2.drawString(text, x + 4, y + 5);
            g2.setColor(new Color(220, 245, 250));
            g2.drawString(text, x, y);
            g2.setFont(font5.deriveFont(Font.BOLD, 27F));
            String musicText = "Music Volume: " + gp.getMusicVolume() + "%";
            String sfxText = "SFX Volume: " + gp.getSfxVolume() + "%";
            drawTitleMenuItem(musicText, getXforCenteredText(musicText), y + gp.tileSize * 2, 0, pulse);
            drawTitleMenuItem(sfxText, getXforCenteredText(sfxText), y + gp.tileSize * 3, 1, pulse);
            drawTitleMenuItem("Back", getXforCenteredText("Back"), y + gp.tileSize * 5, 2, pulse);
            g2.setFont(font5.deriveFont(Font.PLAIN, 18F));
            String hint = "Use LEFT/RIGHT to adjust volume";
            g2.drawString(hint, getXforCenteredText(hint), y + gp.tileSize * 6);
        } else if (titleScreenState == 1) {
            // CLASS SELECTION
            g2.setFont(font2.deriveFont(Font.BOLD, 46F));
            String text = "Choose Your Class";
            int x = getXforCenteredText(text);
            int y = gp.tileSize * 2;

            g2.setColor(new Color(0, 10, 15, 150));
            g2.drawString(text, x + 4, y + 5);

            g2.setColor(new Color(220, 245, 250));
            g2.drawString(text, x, y);

            g2.setFont(font5.deriveFont(Font.BOLD, 30F));
            g2.setColor(new Color(245, 250, 255));

            int classStep = gp.tileSize;
            int classX = getXforCenteredText("Fighter");
            int classY = y + gp.tileSize * 2;

            drawTitleMenuItem("Fighter", classX, classY, 0, pulse);
            drawTitleMenuItem("Ninja", classX, classY + classStep, 1, pulse);
            drawTitleMenuItem("Mage", classX, classY + classStep * 2, 2, pulse);
            drawTitleMenuItem("Back", classX, classY + classStep * 4, 3, pulse);
        }
    }

    private void drawTitleMenuItem(String text, int x, int y, int index, float pulse) {
        if (commandNum == index) {
            int alpha = 120 + (int) (pulse * 100);

            g2.setColor(new Color(120, 215, 235, alpha));
            g2.drawString(">", x - gp.tileSize, y);

            g2.setColor(new Color(255, 255, 255));
            g2.drawString(text, x, y);

            int textWidth = g2.getFontMetrics().stringWidth(text);
            g2.setColor(new Color(170, 235, 245, 150 + (int) (pulse * 70)));
            g2.drawLine(x, y + 5, x + textWidth, y + 5);
        } else {
            g2.setColor(new Color(225, 235, 240));
            g2.drawString(text, x, y);
        }
    }

    private void drawTitleBackground(double time) {
        int width = gp.screenWidth;
        int height = gp.screenHeight;

        int shift = (int) ((Math.sin(time * 0.35) + 1.0) * width * 0.08);

        GradientPaint gradient = new GradientPaint(
                0, 0, new Color(8, 22, 30),
                width, height, new Color(35, 70, 52)
        );

        g2.setPaint(gradient);
        g2.fillRect(0, 0, width, height);

        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.10f));
        g2.setColor(new Color(150, 230, 210));

        for (int i = 0; i < 5; i++) {
            int size = gp.tileSize * (3 + i);
            int x = (int) ((time * (8 + i * 3) + i * 170) % (width + size)) - size;
            int y = height / 6 + i * gp.tileSize + shift;
            g2.fillOval(x, y, size, size);
        }

        g2.setComposite(AlphaComposite.SrcOver);
    }
}
