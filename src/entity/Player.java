package entity;

import java.awt.AlphaComposite;
//import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import main.GamePanel;
import main.KeyHandler;
import content.PlayerAvatar;

public class Player extends Entity{

//	GamePanel gp;
	KeyHandler keyH;
	
	public final int screenX;
	public final int screenY;
//	public int hasKey = 0;
	int standCounter = 0;
	private int avatarIndex = 0;
	private final BufferedImage[][] walkFrames = new BufferedImage[4][4];
	private final BufferedImage[] idleFrames = new BufferedImage[4];
	
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
	public void setAvatar(int avatarIndex) {
		int avatarCount = PlayerAvatar.values().length;
		avatarIndex = (avatarIndex % avatarCount + avatarCount) % avatarCount;
		this.avatarIndex = avatarIndex;
		loadAvatarImages(PlayerAvatar.values()[avatarIndex]);
	}

	private void loadAvatarImages(PlayerAvatar avatar) {
		String root = avatar.getResourceRoot();
		String[] directions = {"down", "left", "right", "up"};

		for (int directionIndex = 0; directionIndex < directions.length; directionIndex++) {
			String directionName = directions[directionIndex];
			for (int frame = 0; frame < 4; frame++) {
				walkFrames[directionIndex][frame] = setup(
						root + "/walk_" + directionName + "_" + (frame + 1),
						gp.tileSize, gp.tileSize);
			}
			idleFrames[directionIndex] = setup(
					root + "/idle_" + directionName,
					gp.tileSize, gp.tileSize);
		}

		// Keep the inherited image fields synchronized for existing UI code.
		down1 = walkFrames[0][0];
		down2 = walkFrames[0][1];
		left1 = walkFrames[1][0];
		left2 = walkFrames[1][1];
		right1 = walkFrames[2][0];
		right2 = walkFrames[2][1];
		up1 = walkFrames[3][0];
		up2 = walkFrames[3][1];
		getPlayerAttackImage();
	}

	public int getAvatarIndex() {
		return avatarIndex;
	}

	public BufferedImage getPreviewImage() {
		return idleFrames[0];
	}

	public void getPlayerImage() {
		setAvatar(avatarIndex);
	}
	public void getPlayerAttackImage() {
		// Temporary avatar-safe attack animation: use walking frames until dedicated
		// attack sheets are available. This keeps every selected avatar consistent.
		attackU1 = walkFrames[3][1];
		attackU2 = walkFrames[3][2];
		attackD1 = walkFrames[0][1];
		attackD2 = walkFrames[0][2];
		attackR1 = walkFrames[2][1];
		attackR2 = walkFrames[2][2];
		attackL1 = walkFrames[1][1];
		attackL2 = walkFrames[1][2];
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
			return;
		}

		boolean moving = keyH.upPressed || keyH.downPressed
				|| keyH.rightPressed || keyH.leftPressed;

		if(moving || keyH.enterPressed) {
			if(keyH.upPressed) {
				direction = "up";
			} else if(keyH.downPressed) {
				direction = "down";
			} else if(keyH.rightPressed) {
				direction = "right";
			} else if(keyH.leftPressed) {
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
				case "up":    worldY -= speed; break;
				case "down":  worldY += speed; break;
				case "right": worldX += speed; break;
				case "left":  worldX -= speed; break;
				}
			}
		}

		// Enter is a one-frame interaction input.
		keyH.enterPressed = false;

		if(moving) {
			standCounter = 0;
			spriteCounter++;
			if(spriteCounter > 8) {
				spriteNum++;
				if(spriteNum > 4) spriteNum = 1;
				spriteCounter = 0;
			}
		} else {
			standCounter++;
			spriteNum = 1;
			if(standCounter > 20) standCounter = 0;
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
		int tempScreenX = screenX;
		int tempScreenY = screenY;
		BufferedImage image = null;
		boolean moving = keyH.upPressed || keyH.downPressed || keyH.rightPressed || keyH.leftPressed;

		switch(direction) {
		case "up":
			if (!attacking) image = moving ? walkFrames[3][spriteNum - 1] : idleFrames[3];
			else { tempScreenY = screenY - gp.tileSize; image = spriteNum == 1 ? attackU1 : attackU2; }
			break;
		case "down":
			if (!attacking) image = moving ? walkFrames[0][spriteNum - 1] : idleFrames[0];
			else image = spriteNum == 1 ? attackD1 : attackD2;
			break;
		case "right":
			if (!attacking) image = moving ? walkFrames[2][spriteNum - 1] : idleFrames[2];
			else image = spriteNum == 1 ? attackR1 : attackR2;
			break;
		case "left":
			if (!attacking) image = moving ? walkFrames[1][spriteNum - 1] : idleFrames[1];
			else { tempScreenX = screenX - gp.tileSize; image = spriteNum == 1 ? attackL1 : attackL2; }
			break;
		default:
			image = idleFrames[0];
		}

		if (invincible) {
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));
		}
		g2.drawImage(image, tempScreenX, tempScreenY, null);
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
	}
}
