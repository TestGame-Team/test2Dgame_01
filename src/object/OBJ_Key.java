package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_Key extends Entity{

	public OBJ_Key(GamePanel gp) {
		
		super(gp);
		name = "Key";
		image = setup("/objects/key", gp.tileSize, gp.tileSize);
	}
}
