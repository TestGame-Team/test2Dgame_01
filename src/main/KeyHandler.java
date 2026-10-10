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
		//TITLE STATE
		if(gp.gameState == gp.titleState) {
			if(gp.ui.titleScreenState == 0) {
				if(code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
					gp.ui.commandNum--;
					if(gp.ui.commandNum < 0) gp.ui.commandNum = 3;
				}else if(code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
					gp.ui.commandNum++;
					if(gp.ui.commandNum > 3) gp.ui.commandNum = 0;
				}else if(code == KeyEvent.VK_ENTER) {
					if (gp.ui.commandNum == 0) {
						gp.ui.titleScreenState = 1;
						gp.ui.commandNum = 0;
					}else if(gp.ui.commandNum == 1) {
						gp.ui.titleScreenState = 2;
						gp.ui.commandNum = 0;
					}else if(gp.ui.commandNum == 2) {
						gp.ui.titleScreenState = 3;
						gp.ui.commandNum = 0;
					}else if(gp.ui.commandNum == 3) {
						System.exit(0);
					}
				}/*
				 * else if(code == KeyEvent.VK_RIGHT || code == KeyEvent.VK_LEFT) { //DEBUG
				 * System.out.println("NO issues here!"); }
				 */
			}
			else if(gp.ui.titleScreenState == 3) {
				if(code == KeyEvent.VK_W || code == KeyEvent.VK_UP) gp.ui.commandNum = (gp.ui.commandNum + 2) % 3;
				else if(code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) gp.ui.commandNum = (gp.ui.commandNum + 1) % 3;
				else if(code == KeyEvent.VK_LEFT || code == KeyEvent.VK_RIGHT) {
					int change = code == KeyEvent.VK_RIGHT ? 10 : -10;
					if(gp.ui.commandNum == 0) gp.setMusicVolume(gp.getMusicVolume() + change);
					else if(gp.ui.commandNum == 1) gp.setSfxVolume(gp.getSfxVolume() + change);
				} else if(code == KeyEvent.VK_ENTER && gp.ui.commandNum == 2) {
					gp.ui.titleScreenState = 0; gp.ui.commandNum = 2;
				} else if(code == KeyEvent.VK_ESCAPE) {
					gp.ui.titleScreenState = 0; gp.ui.commandNum = 2;
				}
			}
			else if(gp.ui.titleScreenState == 2) {
				if(code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
					gp.ui.commandNum--;
					if(gp.ui.commandNum < 0) gp.ui.commandNum = 4;
				}else if(code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
					gp.ui.commandNum++;
					if(gp.ui.commandNum > 4) gp.ui.commandNum = 0;
				}else if(code == KeyEvent.VK_ENTER) {
					if(gp.ui.commandNum <= 2) {
						if(gp.saveManager.loadGame(gp.ui.commandNum + 1)) {
							gp.ui.titleScreenState = 0;
							gp.ui.commandNum = 0;
							gp.ui.messageOn = false;
							gp.gameState = gp.playState;
						}else {
							gp.ui.showMessage("That save slot is empty or invalid.");
						}
					}else if(gp.ui.commandNum == 3) {
						int clearedSlot = gp.saveManager.clearNextSlot();
						if(clearedSlot > 0) gp.ui.showMessage("Cleared Slot " + clearedSlot);
						else gp.ui.showMessage("All save slots are already empty.");
					}else {
						gp.ui.titleScreenState = 0;
						gp.ui.commandNum = 0;
						gp.ui.messageOn = false;
					}
				}
			}
			else if(gp.ui.titleScreenState == 1) {
				if(code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
					gp.ui.commandNum--;
					if(gp.ui.commandNum < 0) gp.ui.commandNum = 3;
				}else if(code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
					gp.ui.commandNum++;
					if(gp.ui.commandNum > 3) gp.ui.commandNum = 0;
				}else if(code == KeyEvent.VK_ENTER) {
					if (gp.ui.commandNum == 0) {System.out.println("Fighter Selected!"); gp.gameState = gp.playState;}
					if(gp.ui.commandNum == 1) {System.out.println("Ninja Selected!"); gp.gameState = gp.playState;}
					if(gp.ui.commandNum == 2) {System.out.println("Mage Selected!"); gp.gameState = gp.playState;}
					if (gp.ui.commandNum == 3) {gp.ui.titleScreenState = 0;	gp.ui.commandNum = 0; gp.stopMusic();}
				}
			}
		}
		
		//PLAY STATE
		else if(gp.gameState == gp.playState) {
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
			if(code == KeyEvent.VK_P) {
				gp.gameState = gp.playState;
			} else if(code == KeyEvent.VK_ESCAPE) {
				if(gp.ui.pauseScreenState != 0) {
					gp.ui.pauseScreenState = 0;
					gp.ui.commandNum = 0;
				} else {
					gp.gameState = gp.playState;
				}
			} else if(gp.ui.pauseScreenState == 1 && (code == KeyEvent.VK_LEFT || code == KeyEvent.VK_RIGHT)) {
				int change = code == KeyEvent.VK_RIGHT ? 10 : -10;
				if(gp.ui.commandNum == 0) gp.setMusicVolume(gp.getMusicVolume() + change);
				else if(gp.ui.commandNum == 1) gp.setSfxVolume(gp.getSfxVolume() + change);
			} else if(code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
				gp.ui.commandNum--;
				if(gp.ui.commandNum < 0) gp.ui.commandNum = gp.ui.pauseScreenState == 1 ? 1 : 4;
			} else if(code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
				gp.ui.commandNum++;
				if(gp.ui.commandNum > (gp.ui.pauseScreenState == 1 ? 1 : 4)) gp.ui.commandNum = 0;
			} else if(code == KeyEvent.VK_ENTER) {
				if(gp.ui.pauseScreenState == 0) {
					if(gp.ui.commandNum == 0) {
						gp.gameState = gp.playState;
					} else if(gp.ui.commandNum == 1) {
						gp.ui.pauseScreenState = 1;
						gp.ui.commandNum = 0;
					} else if(gp.ui.commandNum == 2) {
						int slot = gp.saveManager.saveGame();
						gp.ui.showMessage("Game saved to Slot " + slot);
					} else if(gp.ui.commandNum == 3) {
						gp.ui.pauseScreenState = 2;
						gp.ui.commandNum = 0;
					} else if(gp.ui.commandNum == 4) {
						gp.stopMusic();
						gp.ui.titleScreenState = 0;
						gp.ui.pauseScreenState = 0;
						gp.ui.commandNum = 0;
						gp.gameState = gp.titleState;
					}
				} else if(gp.ui.pauseScreenState == 1) {
					// Adjust volume with LEFT/RIGHT; ESC returns to pause menu.
				} else if(gp.ui.pauseScreenState == 2) {
					// Help screen is informational.
				}
			}
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
