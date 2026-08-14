package main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;
import object.OBJ_Key;

public class UI {

	GamePanel gp;
	Font font1, font2;
	BufferedImage keyImage;
	public boolean messageOn = false;
	public boolean gameFinish = false;
	public String message;
	double playTime = 0;
	int msgCounter = 0;
	DecimalFormat dFormat = new DecimalFormat("#0.00");
	
	public UI(GamePanel gp) {
		this.gp = gp;
		font1 = new Font("Arial", Font.PLAIN, 20);
		font2 = new Font("Arial", Font.BOLD, 30);
		OBJ_Key key = new OBJ_Key(gp);
		keyImage = key.image;
	}
	public void showMessage(String text) {
		message = text;
		messageOn = true;
	}
	public void draw(Graphics2D g2) {
		if(gameFinish) {
			
			g2.setFont(font2);
			g2.setColor(Color.yellow);
			
			String text1 = "Congratulations! Game Completed!";
			String text2 = "You found the Treasure!!!";
			int textLength = (int)g2.getFontMetrics().getStringBounds(text1, g2).getWidth();
			int x = gp.screenWidth / 2 - (textLength / 2);
			int y = gp.screenHeight / 2 - (gp.tileSize * 3);
			
			g2.drawString(text1, x, y);
			g2.drawString(text2, x+gp.tileSize*1, y+gp.tileSize*1);
			g2.setFont(font1);
			g2.drawString("Total time : "+dFormat.format(playTime), x+gp.tileSize*3, y+gp.tileSize*2);
			gp.gameThread = null;
			
		}else{

			g2.setFont(font1);
			g2.setColor(Color.white);
			g2.drawImage(keyImage, gp.tileSize/2, gp.tileSize/2, gp.tileSize/2, gp.tileSize/2, null);
			g2.drawString("x "+gp.player.hasKey, 52, 38);
			
			playTime += (double)1/60;
			g2.drawString("Time : "+dFormat.format(playTime), gp.tileSize*12, 38);
		
			if(messageOn) {
				g2.setFont(g2.getFont().deriveFont(15));
				g2.drawString(message, 100, 100);
			
				msgCounter++;
				if(msgCounter > 120) {
					msgCounter = 0;
					messageOn = false;
				}
			}
		}
	}
}
