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

        NPC_OldMan oldMan = new NPC_OldMan(gp);
        oldMan.worldX = gp.tileSize * 21;
        oldMan.worldY = gp.tileSize * 22;
        gp.addNpc(oldMan);
	}
	public void setMonster() {

        M_GreenSlime slimeOne = new M_GreenSlime(gp);
        slimeOne.worldX = gp.tileSize * 11;
        slimeOne.worldY = gp.tileSize * 10;
        gp.addMonster(slimeOne);

        M_GreenSlime slimeTwo = new M_GreenSlime(gp);
        slimeTwo.worldX = gp.tileSize * 11;
        slimeTwo.worldY = gp.tileSize * 11;
        gp.addMonster(slimeTwo);

	}
}
