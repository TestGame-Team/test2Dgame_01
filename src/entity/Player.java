package entity;

import java.awt.AlphaComposite;
//import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import main.GamePanel;
import main.KeyHandler;

public class Player extends Entity{

//	GamePanel gp;
	KeyHandler keyH;
	
	public final int screenX;
	public final int screenY;
//	public int hasKey = 0;
	int standCounter = 0;
	
	public Player(GamePanel gp, KeyHandler keyH) {
		
		super(gp);
		
//		this.gp = gp;
		this.keyH = keyH;
		
		screenX = gp.screenWidth / 2 - (gp.tileSize / 2);
		screenY = gp.screenHeight / 2 - (gp.tileSize / 2);
		
		type = 0;
		
		solidArea = new Rectangle();
		solidArea.x = 10;
		solidArea.y = 10;
		solidAreaDefaultX = solidArea.x;
		solidAreaDefaultY = solidArea.y;
		solidArea.width = 30;
		solidArea.height = 30;
		
		attackArea.width = 36;
		attackArea.height = 36;
		
		setDefaultValues();
		getPlayerImage();
		getPlayerAttackImage();
	}
	public void setDefaultValues() {
		
		worldX = gp.tileSize * 23;
		worldY = gp.tileSize * 22;
		speed = 5;
		direction = "down";
		maxLife = 6;
		life = maxLife;
	}
	public void getPlayerImage() {
		
		up1 = setup("/player/boy_up_1", gp.tileSize, gp.tileSize);
		up2 = setup("/player/boy_up_2", gp.tileSize, gp.tileSize);
		down1 = setup("/player/boy_down_1", gp.tileSize, gp.tileSize);
		down2 = setup("/player/boy_down_2", gp.tileSize, gp.tileSize);
		right1 = setup("/player/boy_right_1", gp.tileSize, gp.tileSize);
		right2 = setup("/player/boy_right_2", gp.tileSize, gp.tileSize);;
		left1 = setup("/player/boy_left_1", gp.tileSize, gp.tileSize);
		left2 = setup("/player/boy_left_2", gp.tileSize, gp.tileSize);
	}
	public void getPlayerAttackImage() {
		
		attackU1 = setup("/player/boy_attack_up_1", gp.tileSize, gp.tileSize*2);
		attackU2 = setup("/player/boy_attack_up_2", gp.tileSize, gp.tileSize*2);
		attackD1 = setup("/player/boy_attack_down_1", gp.tileSize, gp.tileSize*2);
		attackD2 = setup("/player/boy_attack_down_2", gp.tileSize, gp.tileSize*2);
		attackR1 = setup("/player/boy_attack_right_1", gp.tileSize*2, gp.tileSize);
		attackR2 = setup("/player/boy_attack_right_2", gp.tileSize*2, gp.tileSize);
		attackL1 = setup("/player/boy_attack_left_1", gp.tileSize*2, gp.tileSize);
		attackL2 = setup("/player/boy_attack_left_2", gp.tileSize*2, gp.tileSize);
	}
	public void update() {
		
		if(invincible) {
			invincibleCounter++;
			if(invincibleCounter > 60) {
				invincible = false;
				invincibleCounter = 0;
			}
		}
		if(attacking) {
			attacking();
		}
		else if(keyH.upPressed == true || keyH.downPressed == true ||
		   keyH.rightPressed == true || keyH.leftPressed == true || keyH.enterPressed == true) {
			if(keyH.upPressed == true) {
				direction = "up";
			}
			else if(keyH.downPressed == true) {
				direction = "down";
			}
			else if(keyH.rightPressed == true) {
				direction = "right";
			}
			else if(keyH.leftPressed == true) {
				direction = "left";
			}
		
		collisionOn = false;
		gp.cChecker.checkTile(this);
		int objIndex = gp.cChecker.checkObject(this, true);
		pickUpObject(objIndex);
		int npcIndex = gp.cChecker.checkEntity(this, gp.npc);
		interactNPC(npcIndex);
		int monsterIndex = gp.cChecker.checkEntity(this, gp.monster);
		interactMonster(monsterIndex);
		gp.eHandler.checkEvent();
		
		if(collisionOn == false && !keyH.enterPressed) {
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
		gp.keyH.enterPressed = false;
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
		}else{
			standCounter++;
			if(standCounter == 20) {
				spriteNum = 1;
				standCounter = 0;
			}
		}
	}
	public void pickUpObject(int i) {
		if(i != 1000) {
			/*
			 * String objName = gp.obj[i].name; switch(objName) { case "Key": gp.playSE(1);
			 * hasKey++; gp.obj[i] = null; gp.ui.showMessage("You found a Key! ! !"); break;
			 * case "Door": if(hasKey > 0) { gp.playSE(3); gp.obj[i] = null;
			 * gp.ui.showMessage("You opened the Door! ! !"); hasKey--; }else {
			 * gp.ui.showMessage("You need a Key!"); } break; case "GBox": gp.ui.gameFinish
			 * = true; gp.stopMusic(); gp.playSE(4); break; case "Boots": gp.playSE(2);
			 * speed += 2; gp.obj[i] = null; gp.ui.showMessage("You got Speed Boost! ! !");
			 * break;
			 */

		}
	}
	public void interactNPC(int i) {
		if(keyH.enterPressed) {
			if(i != 1000) {
				
				gp.gameState = gp.dialogueState;
				gp.npc[i].speak();
			}
			else {
			
				attacking = true;
			}
		}
	}
	public void attacking() {
		
		spriteCounter++;
		if(spriteCounter <= 5) {
			spriteNum = 1;
		}
		else if(spriteCounter > 5 && spriteCounter <= 25) {
			spriteNum = 2;
			
			int currentWorldX = worldX;
			int currentWorldY = worldY;
			int solidAreaWidth = solidArea.width;
			int solidAreaHeight = solidArea.height;
			
			switch(direction) {
			case "up": worldY -= attackArea.height; break;
			case "down": worldY += attackArea.height; break;
			case "right": worldX += attackArea.width; break;
			case "left": worldX -= attackArea.width; break;
			}
			
			solidArea.width = attackArea.width;
			solidArea.height = attackArea.height;
			
			int monsterIndex = gp.cChecker.checkEntity(this, gp.monster);
			damageMonster(monsterIndex);
			
			worldX = currentWorldX;
			worldY = currentWorldY;
			solidArea.width = solidAreaWidth;
			solidArea.height = solidAreaHeight;
		}
		else {
			spriteNum = 1;
			spriteCounter = 0;
			attacking = false;
		}
	}
	public void damageMonster(int i) {

		if(i != 1000) {
			
			if(!gp.monster[i].invincible) {
				
				gp.monster[i].life--;
				gp.monster[i].invincible = true;
				
				if(gp.monster[i].life <= 0) {
					gp.monster[i].non = true;
				}
			}
		}
	}
	public void interactMonster(int i) {

		if(i != 1000) {
			if(!invincible) {
				life -= 1;
				invincible = true;
			}
		}
	}
	public void draw(Graphics2D g2) {
//		g2.setColor(Color.white); g2.fillRect(x, y, gp.tileSize, gp.tileSize);
		
		int tempScreenX = screenX;
		int tempScreenY = screenY;
		
		BufferedImage image = null;
		switch(direction) {
		case "up": 
			if(!attacking) {
				if(spriteNum == 1) {
					image = up1;
				}
				else if(spriteNum == 2) {
					image = up2;
				}
			}
			else if(attacking) {
				tempScreenY = screenY - gp.tileSize;
				if(spriteNum == 1) {
					image = attackU1;
				}
				else if(spriteNum == 2) {
					image = attackU2;
				}
			}
			break;
		case "down":
			if(!attacking) {
				if(spriteNum == 1) {
					image = down1;
				}
				else if(spriteNum == 2) {
					image = down2;
				}
			}
			else if(attacking) {
				if(spriteNum == 1) {
					image = attackD1;
				}
				else if(spriteNum == 2) {
					image = attackD2;
				}
			}
			break;
		case "right":
			if(!attacking) {
				if(spriteNum == 1) {
					image = right1;
				}
				else if(spriteNum == 2) {
					image = right2;
				}
			}
			else if(attacking) {
				if(spriteNum == 1) {
					image = attackR1;
				}
				else if(spriteNum == 2) {
					image = attackR2;
				}
			}
			break;
		case "left":
			if(!attacking) {
				if(spriteNum == 1) {
					image = left1;
				}
				else if(spriteNum == 2) {
					image = left2;
				}
			}
			else if(attacking) {
				tempScreenX = screenX - gp.tileSize;
				if(spriteNum == 1) {
					image = attackL1;
				}
				else if(spriteNum == 2) {
					image = attackL2;
				}
			}
			break;
		}
		if(invincible) {
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));
		}
		g2.drawImage(image, tempScreenX, tempScreenY, null);
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));

	}
}
