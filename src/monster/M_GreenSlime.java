package monster;

import java.util.Random;

import entity.Entity;
import main.GamePanel;

public class M_GreenSlime extends Entity{

	public M_GreenSlime(GamePanel gp) {
		super(gp);
		name = "Green Slime";
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
		
		up1 = setup("/monster/greenslime_down_1");
		up2 = setup("/monster/greenslime_down_2");
		down1 = setup("/monster/greenslime_down_1");
		down2 = setup("/monster/greenslime_down_2");
		right1 = setup("/monster/greenslime_down_1");
		right2 = setup("/monster/greenslime_down_2");
		left1 = setup("/monster/greenslime_down_1");
		left2 = setup("/monster/greenslime_down_2");
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
