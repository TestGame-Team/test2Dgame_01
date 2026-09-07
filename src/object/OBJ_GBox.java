package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_GBox extends Entity{

	public OBJ_GBox(GamePanel gp) {
		
		super(gp);
		name = "Box";
		image = setup("/objects/chest");
		collision = true;
	}
}