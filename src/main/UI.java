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
        }
    }

    public void drawDebugStats(Graphics2D g2, double renderTimeMs) {
        g2.setFont(font1);
        g2.setColor(Color.white);

        String text = String.format("Render: %.3f ms", renderTimeMs);
        int x = 8;
        int y = gp.screenHeight - 8;

        g2.setColor(new Color(0, 0, 0, 150));
        g2.fillRoundRect(x - 4, y - 18, 118, 22, 8, 8);

        g2.setColor(Color.white);
        g2.drawString(text, x, y - 2);
    }

    public void drawPauseScreen() {
        String text = "PAUSED";
        g2.setFont(g2.getFont().deriveFont(90F));
        int x = getXforCenteredText(text);
        int y = gp.screenHeight / 2;
        g2.drawString(text, x, y);
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
            //TITLE
            g2.setFont(font6.deriveFont(Font.BOLD, 75F));
            String text = "Blue Boy Adventure";
            int x = getXforCenteredText(text);
            int y = gp.tileSize * 3;

            g2.setColor(new Color(30, 45, 55, 120 + (int) (pulse * 50)));
            g2.drawString(text, x + 4, y + 5);

            g2.setColor(new Color(220, 245, 255));
            g2.drawString(text, x, y);

            //CHARACTER
            x = gp.screenWidth / 2 - (gp.tileSize * 2) / 2;
            y += gp.tileSize + bob;
            g2.drawImage(gp.player.down1, x, y, gp.tileSize * 2, gp.tileSize * 2, null);

            //MENU
            g2.setFont(font5.deriveFont(Font.BOLD, 45F));
            g2.setColor(new Color(245, 250, 255));

            text = "NEW GAME";
            x = getXforCenteredText(text);
            y = gp.tileSize * 7;

            g2.drawRoundRect(gp.tileSize, gp.tileSize * 7 - 18,
                    gp.tileSize * 14, gp.tileSize * 4 + 12, 18, 18);

            drawTitleMenuItem(text, x, y, 0, pulse);
            drawTitleMenuItem("LOAD GAME", x, y + gp.tileSize, 1, pulse);
            drawTitleMenuItem("OPTIONS", x, y + gp.tileSize * 2, 2, pulse);
            drawTitleMenuItem("QUIT", x, y + gp.tileSize * 3, 3, pulse);
        } else if (titleScreenState == 1) {
            //CLASS SELECTION
            g2.setColor(Color.white);
            g2.setFont(font6.deriveFont(42F));
            String text = "Select your class!";
            int x = getXforCenteredText(text);
            int y = gp.tileSize * 2;

            g2.drawString(text, x, y);
            drawTitleMenuItem("Fighter", x, y + gp.tileSize * 2, 0, pulse);
            drawTitleMenuItem("Ninja", x, y + gp.tileSize * 3, 1, pulse);
            drawTitleMenuItem("Mage", x, y + gp.tileSize * 4, 2, pulse);
            drawTitleMenuItem("Back", x, y + gp.tileSize * 6, 3, pulse);
        }
    }

    private void drawTitleMenuItem(String text, int x, int y, int index, float pulse) {
        g2.drawString(text, x, y);

        if (commandNum == index) {
            int alpha = 120 + (int) (pulse * 100);
            g2.setColor(new Color(150, 235, 255, alpha));
            g2.drawString(">", x - gp.tileSize, y);
            g2.setColor(new Color(245, 250, 255));
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
