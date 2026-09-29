package monster;

import java.util.Random;
import entity.Entity;
import main.GamePanel;

public class M_GreenSlime extends Entity{

	GamePanel gp;
	
	public M_GreenSlime(GamePanel gp) {
		super(gp);
		this.gp = gp;
		name = "Green Slime";
		type = 2;
		speed = 1;
		maxLife = 4;
		life = maxLife;
		
		solidArea.x = 3;
		solidArea.y = 18;
		solidArea.width = 42;
		solidArea.height = 30;
		solidAreaDefaultX = solidArea.x;
		solidAreaDefaultY = solidArea.y;
		
		getImage();
		
	}
	public void getImage() {
		
		up1 = setup("/monster/greenslime_down_1", gp.tileSize, gp.tileSize);
		up2 = setup("/monster/greenslime_down_2", gp.tileSize, gp.tileSize);
		down1 = setup("/monster/greenslime_down_1", gp.tileSize, gp.tileSize);
		down2 = setup("/monster/greenslime_down_2", gp.tileSize, gp.tileSize);
		right1 = setup("/monster/greenslime_down_1", gp.tileSize, gp.tileSize);
		right2 = setup("/monster/greenslime_down_2", gp.tileSize, gp.tileSize);
		left1 = setup("/monster/greenslime_down_1", gp.tileSize, gp.tileSize);
		left2 = setup("/monster/greenslime_down_2", gp.tileSize, gp.tileSize);
	}
	public void setAction() {
		
		actionLockCounter++;
		if(actionLockCounter >= 180) {
			
			Random random = new Random();
			int i = random.nextInt(100)+1;
			if(i <= 25) {
				direction = "up";
			}else if(i > 25 && i <= 50) {
				direction = "right";
			}else if(i > 50 && i <= 75) {
				direction = "down";
			}else if(i > 75 && i <=100) {
				direction = "left";
			}
			actionLockCounter = 0;
		}
	}
}
