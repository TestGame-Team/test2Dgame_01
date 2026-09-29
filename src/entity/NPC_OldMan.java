package entity;

import java.util.Random;
import main.GamePanel;

public class NPC_OldMan extends Entity{

	public NPC_OldMan(GamePanel gp) {
		super(gp);
		
		direction = "down";
		speed = 2;
		getImage();
		setDialogue();
	}
	public void getImage() {
		
		up1 = setup("/npc/oldman_up_1");
		up2 = setup("/npc/oldman_up_2");
		down1 = setup("/npc/oldman_down_1");
		down2 = setup("/npc/oldman_down_2");
		right1 = setup("/npc/oldman_right_1");
		right2 = setup("/npc/oldman_right_2");;
		left1 = setup("/npc/oldman_left_1");
		left2 = setup("/npc/oldman_left_2");
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
	public void setDialogue() {
		dialogues[0] = "Hello, Lad.";
		dialogues[1] = "So you've come to this Island\n for the Treasure?";
		dialogues[2] = "I was a great wizard once...\n now I'm a bit old for adventures.";
		dialogues[3] = "Therefore Good luck to you Lad.";
	}
	public void speak() {
		super.speak();
	}
}
