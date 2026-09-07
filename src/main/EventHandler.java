package main;

import java.awt.Rectangle;

public class EventHandler {

	GamePanel gp;
	Rectangle eventRect;
	int eventRectDefaultX, eventRectDefaultY;
	
	public EventHandler(GamePanel gp) {
		this.gp = gp;
		eventRect = new Rectangle();
		eventRect.x = 23;
		eventRect.y = 23;
		eventRect.width = 2;
		eventRect.height = 2;
		eventRectDefaultX = eventRect.x;
		eventRectDefaultY = eventRect.y;
	}
	public void checkEvent() {
		if(hit(27, 14, "left") == true) {
			damagePit(gp.dialogueState);
		}else if(hit(23, 12, "up") == true) {
			healPool(gp.dialogueState);
		}else if(hit(27, 16, "right") == true) {
			teleport(gp.dialogueState);
		}
	}
	public boolean hit(int eventCol, int eventRow, String reqDir) {
		
		boolean hit = false;
		
		gp.player.solidArea.x += gp.player.worldX;
		gp.player.solidArea.y += gp.player.worldY;
		eventRect.x += eventCol*gp.tileSize;
		eventRect.y += eventRow*gp.tileSize;

		if(gp.player.solidArea.intersects(eventRect)) {
			if(gp.player.direction.equals(reqDir) || reqDir.equals("any")) {
				hit = true;
			}
		}
		
		gp.player.solidArea.x = gp.player.solidAreaDefaultX;
		gp.player.solidArea.y = gp.player.solidAreaDefaultY;
		eventRect.x = eventRectDefaultX;
		eventRect.y = eventRectDefaultY;
		
		return hit;
	}
	public void damagePit(int gameState) {
		gp.gameState = gameState;
		gp.ui.currentDialogue = "You Fall into a Pit!!!";
		gp.player.life--;
		
	}
	public void healPool(int gameState) {
		if(gp.keyH.enterPressed == true) {
			gp.gameState = gameState;
			gp.ui.currentDialogue = "You have been healed!!!";
			gp.player.life = gp.player.maxLife;
		}
	}
	public void teleport(int gameState) {
		gp.gameState = gameState;
		gp.ui.currentDialogue = "Teleport!!!";
		gp.player.worldX = gp.tileSize*37;
		gp.player.worldY = gp.tileSize*10;
	}
	
}
