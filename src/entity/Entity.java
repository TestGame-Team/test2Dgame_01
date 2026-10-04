package entity;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import content.AssetManager;
import main.GamePanel;

public class Entity {

	GamePanel gp;
	public BufferedImage attackU1, attackU2, attackD1, attackD2, attackR1, attackR2, attackL1, attackL2;
	public BufferedImage up1, up2, down1, down2, right1, right2, left1, left2, image, image2, image3;
	public int solidAreaDefaultX, solidAreaDefaultY;
	public int actionLockCounter = 0;
	public int invincibleCounter = 0;
	public int dialogueIndex = 0;
	public int spriteCounter = 0;
	public int nonCounter = 0;
	public int worldX, worldY;
	public int spriteNum = 1;
	public int speed;
	public int type;			// 0 = player; 1 = npc; 2 = monster;
	public Rectangle solidArea = new Rectangle(0, 0, 45, 45);
	public Rectangle attackArea = new Rectangle(0, 0, 0, 0);
	public boolean collisionOn = false;
	public boolean invincible = false;
	public boolean collision = false;
	public boolean attacking = false;
	public boolean alive = true;
	public boolean non = false;
	public String dialogues[] = new String[20];
	public String direction = "down";
	public String name;
	
	//PLAYER STATUS
	public int maxLife;
	public int life;
	
	public Entity(GamePanel gp) {
		this.gp = gp;
	}
	
	public void setAction() {}
	public void speak() {
		if(dialogues[dialogueIndex] == null) {
			dialogueIndex = 0;
		}
		gp.ui.currentDialogue = dialogues[dialogueIndex];
		dialogueIndex++;
		switch(gp.player.direction) {
		case "up": direction = "down"; break;
		case "down": direction = "up"; break;
		case "left": direction = "right"; break;
		case "right": direction = "left"; break;
		}
	}
	public void update() {
		
		setAction();
		collisionOn = false;
		gp.cChecker.checkTile(this);
		gp.cChecker.checkObject(this, false);
		gp.cChecker.checkEntity(this, gp.npc);
		gp.cChecker.checkEntity(this, gp.monster);
		boolean interactPlayer = gp.cChecker.checkPlayer(this);
		
		if(this.type == 2 && interactPlayer) {
			if(!gp.player.invincible) {
				gp.player.life--;
				gp.player.invincible = true;
			}
		}
		
		if(invincible) {
			invincibleCounter++;
			if(invincibleCounter > 30) {
				invincible = false;
				invincibleCounter = 0;
			}
		}
		
		if(collisionOn == false) {
			switch(direction) {
			case "up"   :
				worldY -= speed;
				break;
			case "down" :
				worldY += speed;
				break;
			case "right":
				worldX += speed;
				break;
			case "left" :
				worldX -= speed;
				break;
			}
		}
    
		spriteCounter++;
		if(spriteCounter > 12) {
			if(spriteNum == 1) {
				spriteNum = 2;
			}
			else if(spriteNum == 2) {
				spriteNum = 1;
			}
		spriteCounter = 0;
			}
		}
	
	public void draw(Graphics2D g2) {
		
		int screenX = worldX - gp.player.worldX + gp.player.screenX;
		int screenY = worldY - gp.player.worldY + gp.player.screenY;
		BufferedImage image = null;
		
		if(worldX + gp.tileSize > gp.player.worldX - gp.player.screenX &&
		   worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
		   worldY + gp.tileSize > gp.player.worldY - gp.player.screenY &&
		   worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) {
			
			switch(direction) {
			case "up": 
				if(spriteNum == 1) {
					image = up1;
				}
				if(spriteNum == 2) {
					image = up2;
				}
				break;
			case "down":
				if(spriteNum == 1) {
					image = down2;
				}
				if(spriteNum == 2) {
					image = down1;
				}
				break;
			case "right":
				if(spriteNum == 1) {
					image = right1;
				}
				if(spriteNum == 2) {
					image = right2;
				}
				break;
			case "left":
				if(spriteNum == 1) {
					image = left1;
				}
				if(spriteNum == 2) {
					image = left2;
				}
				break;
			}
			if(invincible) {
				g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f));
			}
			if(non) {
				nonAnimation(g2);
			}
			g2.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null);
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
		}
	}
	
	public void nonAnimation(Graphics2D g2) {
		
		nonCounter++;
		
		if(nonCounter <= 5) {
			changeAV(g2, 0.4f);		}
		else if(nonCounter > 5 && nonCounter <= 20) {
			changeAV(g2, 1f);		}
		else if(nonCounter > 20 && nonCounter <= 40) {
			changeAV(g2, 0.4f);		}
		else if(nonCounter > 40 && nonCounter <= 60) {
			changeAV(g2, 1f);		}
		else if(nonCounter > 60 && nonCounter <= 70) {
			changeAV(g2, 0.4f);		}
		else {
			non = false;
			alive = false;
		}
	}
	
	public void changeAV(Graphics2D g2, float AV) {
		
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, AV));
	}
	
	public BufferedImage setup(String imagePath, int width, int height) {
        return AssetManager.getImage(imagePath, width, height);
    }
}
