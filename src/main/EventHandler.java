package main;

public class EventHandler {

	GamePanel gp;
	EventRect eventRect[][];
	
	int prevEventX, prevEventY;
	boolean safeEvent = true;
	
	public EventHandler(GamePanel gp) {
		this.gp = gp;
		eventRect = new EventRect[gp.maxWorldCol][gp.maxWorldRow];
		int col = 0, row = 0;
		while(col < gp.maxWorldCol && row < gp.maxWorldRow) {
			
			eventRect[col][row] = new EventRect();
			eventRect[col][row].x = 23;
			eventRect[col][row].y = 23;
			eventRect[col][row].width = 2;
			eventRect[col][row].height = 2;
			eventRect[col][row].eventRectDefaultX = eventRect[col][row].x;
			eventRect[col][row].eventRectDefaultY = eventRect[col][row].y;
			
			col++;
			if(col == gp.maxWorldCol) {
				col = 0; row++;
			}
		}
	}
	public void checkEvent() {
		
		int x = Math.abs(gp.player.worldX - prevEventX);
		int y = Math.abs(gp.player.worldY - prevEventY);
		int s = Math.max(x, y);
		
		if(s > gp.tileSize) {
			safeEvent = true;
		}
		if(safeEvent) {
			if(hit(29, 21, "right") == true || hit(29, 21, "left") == true) {
				damagePit(29, 21, gp.dialogueState);
			}else if(hit(23, 12, "up") == true) {
				healPool(23, 12, gp.dialogueState);
			}else if(hit(27, 16, "right") == true) {
				teleport(27, 16, gp.dialogueState);
			}
		}
	}
	public boolean hit(int col, int row, String reqDir) {
		
		boolean hit = false;
		
		gp.player.solidArea.x += gp.player.worldX;
		gp.player.solidArea.y += gp.player.worldY;
		eventRect[col][row].x += col*gp.tileSize;
		eventRect[col][row].y += row*gp.tileSize;

		if(gp.player.solidArea.intersects(eventRect[col][row]) && !eventRect[col][row].eventDone) {
			if(gp.player.direction.equals(reqDir) || reqDir.equals("any")) {
				hit = true;
				prevEventX = gp.player.worldX;
				prevEventY = gp.player.worldY;
			}
		}
		
		gp.player.solidArea.x = gp.player.solidAreaDefaultX;
		gp.player.solidArea.y = gp.player.solidAreaDefaultY;
		eventRect[col][row].x = eventRect[col][row].eventRectDefaultX;
		eventRect[col][row].y = eventRect[col][row].eventRectDefaultY;
		
		return hit;
	}
	public void damagePit(int col, int row, int gameState) {
		gp.gameState = gameState;
		gp.ui.currentDialogue = "You Fall into a Pit!!!";
		gp.player.life--;
//		eventRect[col][row].eventDone = true;
		safeEvent = false;
	}
	public void healPool(int col, int row, int gameState) {
		if(gp.keyH.enterPressed == true) {
			gp.gameState = gameState;
			gp.ui.currentDialogue = "You have been healed!!!";
			gp.player.life = gp.player.maxLife;
		}
	}
	public void teleport(int col, int row, int gameState) {
		gp.gameState = gameState;
		gp.ui.currentDialogue = "Teleport!!!";
		gp.player.worldX = gp.tileSize*37;
		gp.player.worldY = gp.tileSize*10;
	}
	
}
