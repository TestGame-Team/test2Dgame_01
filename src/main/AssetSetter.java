package main;

import entity.NPC_OldMan;
import monster.M_GreenSlime;


/*import object.OBJ_Boots;
import object.OBJ_Door;
import object.OBJ_Door;
import object.OBJ_GBox;
import object.OBJ_Key;*/

public class AssetSetter {

	GamePanel gp;
	public AssetSetter(GamePanel gp) {
		this.gp = gp;
	}
	public void setObject() {

		
	}
	public void setNPC() {

		gp.npc[0] = new NPC_OldMan(gp);
		gp.npc[0].worldX = gp.tileSize*12;
		gp.npc[0].worldY = gp.tileSize*12;
	}
	public void setMonster() {

		gp.monster[0] = new M_GreenSlime(gp);
		gp.monster[0].worldX = gp.tileSize*11;
		gp.monster[0].worldY = gp.tileSize*10;
		
		gp.monster[1] = new M_GreenSlime(gp);
		gp.monster[1].worldX = gp.tileSize*11;
		gp.monster[1].worldY = gp.tileSize*11;

	}
}
