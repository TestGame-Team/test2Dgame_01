package main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
/*import java.awt.image.BufferedImage;
import java.text.DecimalFormat;
import object.OBJ_Key;*/

public class UI {

	GamePanel gp;
	Graphics2D g2;
	Font font1, font2, font3, font4, font5, font6, font7;
	/*
	 * BufferedImage keyImage; DecimalFormat dFormat = new DecimalFormat("#0.00");
	 * double playTime = 0;
	 */
	public boolean messageOn = false;
	public boolean gameFinish = false;
	public String message;
	public String currentDialogue;
	public int titleScreenState = 0;
	public int commandNum = 0;
	public int msgCounter = 0;

	
	public UI(GamePanel gp) {
		this.gp = gp;
		font1 = new Font("Arial", Font.PLAIN, 20);
		font2 = new Font("Book Antiqua", Font.PLAIN, 20);
		font3 = new Font("Segoe Script", Font.PLAIN, 20);
		font4 = new Font("Calibri", Font.PLAIN, 20);
		font5 = new Font("Segoe UI", Font.PLAIN, 20);
		font6 = new Font("Trebuchet MS", Font.PLAIN, 20);
//		OBJ_Key key = new OBJ_Key(gp);
//		keyImage = key.image;
	}
	public void showMessage(String text) {
		message = text;
		messageOn = true;
	}
	public void draw(Graphics2D g2) {
		/*
		 * if(gameFinish) { g2.setFont(font2); g2.setColor(Color.yellow);
		 * 
		 * String text1 = "Congratulations! Game Completed!"; String text2 =
		 * "You found the Treasure!!!"; int textLength =
		 * (int)g2.getFontMetrics().getStringBounds(text1, g2).getWidth(); int x =
		 * gp.screenWidth / 2 - (textLength / 2); int y = gp.screenHeight / 2 -
		 * (gp.tileSize * 3);
		 * 
		 * g2.drawString(text1, x, y); g2.drawString(text2, x+gp.tileSize*1,
		 * y+gp.tileSize*1); g2.setFont(font1);
		 * g2.drawString("Total time : "+dFormat.format(playTime), x+gp.tileSize*3,
		 * y+gp.tileSize*2); gp.gameThread = null;
		 * 
		 * }else{
		 * 
		 * g2.setFont(font1); g2.setColor(Color.white); g2.drawImage(keyImage,
		 * gp.tileSize/2, gp.tileSize/2, gp.tileSize/2, gp.tileSize/2, null);
		 * g2.drawString("x "+gp.player.hasKey, 52, 38);
		 * 
		 * playTime += (double)1/60; g2.drawString("Time : "+dFormat.format(playTime),
		 * gp.tileSize*12, 38);
		 * 
		 * if(messageOn) { g2.setFont(g2.getFont().deriveFont(15));
		 * g2.drawString(message, 100, 100);
		 * 
		 * msgCounter++; if(msgCounter > 120) { msgCounter = 0; messageOn = false; } } }
		 */
		this.g2 = g2;
		g2.setFont(font5);
		g2.setColor(Color.white);
		if(gp.gameState == gp.playState) {
			
			//PLACEHOLDER
		}else if(gp.gameState == gp.pauseState) {
			drawPauseScreen();
		}else if(gp.gameState == gp.dialogueState) {
			drawDialogueScreen();
		}else if(gp.gameState == gp.titleState) {
			drawTitleScreen();
		}
	}
	public void drawPauseScreen() {
		
		String text = "PAUSED";
		g2.setFont(g2.getFont().deriveFont(90F));
		int x = getXforCenteredText(text);
		int y = gp.screenHeight/2;
		g2.drawString(text, x, y);
	}
	public int getXforCenteredText(String text) {
		int length = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
		int x = gp.screenWidth/2 - length/2;
		return x;
	}
	public void drawDialogueScreen() {
		
		int x = gp.tileSize*2 ;
		int y = gp.tileSize/2 ;
		int width = gp.screenWidth - (gp.tileSize*4) ;
		int height = gp.tileSize*4 ;
		drawSubWindow(x, y, width, height);
		
		g2.setFont(font3);
		g2.setColor(Color.white);
		x += gp.tileSize;
		y += gp.tileSize;
		for(String line : currentDialogue.split("\n")) {
			g2.drawString(line, x, y);
			y += 40;
		}		
	}
	public void drawSubWindow(int x, int y, int width, int height) {
		
		Color c = new Color(0,0,0,150);
		g2.setColor(c);
		g2.fillRoundRect(x, y, width, height, 36, 36);
	}
	public void drawTitleScreen() {
		if(titleScreenState == 0) {
			//TITLE NAME
			g2.setColor(new Color(50, 100, 50));
			g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
			//background and text
			g2.setFont(font6.deriveFont(Font.BOLD, 75F));
			g2.setColor(Color.yellow);
			String text = "Blue Boy Adventure";
			int x = getXforCenteredText(text);
			int y = gp.tileSize*3;
			//text shadow effect
			g2.drawString(text, x+2, y+3);
			g2.setColor(Color.cyan);
			g2.drawString(text, x, y);
			//background image
			x = gp.screenWidth/2 - (gp.tileSize*2)/2;
			y += gp.tileSize;
			g2.drawImage(gp.player.down1, x, y, gp.tileSize*2, gp.tileSize*2, null);
			//menu
			g2.setFont(font5.deriveFont(Font.BOLD, 45F));
			g2.setColor(Color.white);
			text = "NEW GAME";
			x = getXforCenteredText(text);
			y += gp.tileSize*3.8;
			g2.drawRect(gp.tileSize, gp.tileSize*7, gp.tileSize*14, gp.tileSize*4);
			g2.drawString(text, x, y);
			if(commandNum == 0) {
				g2.drawString(">", x-gp.tileSize, y); //can use drawImage instead of drawString
			}
			text = "LOAD GAME";
			g2.drawString(text, x, y+gp.tileSize);
			if(commandNum == 1) {
				g2.drawString(">", x-gp.tileSize, y+gp.tileSize); //can use drawImage instead of drawString
			}
			text = "OPTIONS";
			g2.drawString(text, x, y+gp.tileSize*2);
			if(commandNum == 2) {
				g2.drawString(">", x-gp.tileSize, y+gp.tileSize*2); //can use drawImage instead of drawString
			}
			text = "QUIT";
			g2.drawString(text, x, y+gp.tileSize*3);
			if(commandNum == 3) {
				g2.drawString(">", x-gp.tileSize, y+gp.tileSize*3); //can use drawImage instead of drawString
			}
		}
		else if(titleScreenState == 1) {
			//CLASS SELECTION
			g2.setColor(Color.white);
			g2.setFont(font6.deriveFont(42F));
			String text = "Select your class!";
			int x = getXforCenteredText(text);
			int y = gp.tileSize*2;
			g2.drawString(text, x, y);
			g2.drawString("Fighter", x, y+gp.tileSize*2);
			if(commandNum == 0) {
				g2.drawString(">", x-gp.tileSize, y+gp.tileSize*2);
			}
			g2.drawString("Ninja", x, y+gp.tileSize*3);
			if(commandNum == 1) {
				g2.drawString(">", x-gp.tileSize, y+gp.tileSize*3);
			}
			g2.drawString("Mage", x, y+gp.tileSize*4);
			if(commandNum == 2) {
				g2.drawString(">", x-gp.tileSize, y+gp.tileSize*4);
			}
			g2.drawString("Back", x, y+gp.tileSize*6);
			if(commandNum == 3) {
				g2.drawString(">", x-gp.tileSize, y+gp.tileSize*6);
			}
		}
	}
}
