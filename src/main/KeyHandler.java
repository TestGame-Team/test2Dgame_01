package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener{
	
	GamePanel gp;
	
	public KeyHandler(GamePanel gp) {
		this.gp = gp;
	}
	
	public boolean upPressed, downPressed, rightPressed, leftPressed, debugPressed, enterPressed;

	@Override
	public void keyTyped(KeyEvent e) {}
	@Override
	public void keyPressed(KeyEvent e) {

		int code = e.getKeyCode();
		//PLAY STATE
		if(gp.gameState == gp.playState) {
			if(code == KeyEvent.VK_W) {
				upPressed = true;
			}else if(code == KeyEvent.VK_S) {
				downPressed = true;
			}else if(code == KeyEvent.VK_D) {
				rightPressed = true;
			}else if(code == KeyEvent.VK_A) {
				leftPressed = true;
			}else if(code == KeyEvent.VK_T) {
				debugPressed = true;
			}else if(code == KeyEvent.VK_P) {
					gp.gameState = gp.pauseState;
			}else if(code == KeyEvent.VK_ENTER) {
				enterPressed = true;
			}
		}
		//DIALOGUE STATE
		else if(gp.gameState == gp.dialogueState) {
			if(code == KeyEvent.VK_ENTER)
			gp.gameState = gp.playState;
		}
		//PAUSE STATE
		else if(gp.gameState == gp.pauseState) {
			if(code == KeyEvent.VK_P)
			gp.gameState = gp.playState;
		}
	}
	@Override
	public void keyReleased(KeyEvent e) {

		int code = e.getKeyCode();
		
		if(code == KeyEvent.VK_W) {
			upPressed = false;
		}else if(code == KeyEvent.VK_S) {
			downPressed = false;
		}else if(code == KeyEvent.VK_D) {
			rightPressed = false;
		}else if(code == KeyEvent.VK_A) {
			leftPressed = false;
		}else if(code == KeyEvent.VK_T) {
			debugPressed = false;
		}
	}
}
