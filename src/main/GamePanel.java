package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;
import entity.Entity;
import entity.Player;
import object.SuperObject;
import tile.TileManager;

public class GamePanel extends JPanel implements Runnable{

	//WORLD SETTINGS
	final int originalTilesize = 16;
	final int scale = 3;
	public final int tileSize = originalTilesize * scale;
	public final int maxScreenCol = 16;
	public final int maxScreenRow = 12;
	public final int screenWidth = maxScreenCol * tileSize;
	public final int screenHeight = maxScreenRow * tileSize;
	public final int maxWorldCol = 50;
	public final int maxWorldRow = 50;
	/*
	 * public final int worldWidth = tileSize * maxWorldCol; public final int
	 * worldHeight = tileSize * maxWorldRow;
	 */
	int FPS = 60;  // Game-Setting
	
	//SYSTEM
	public CollisionChecker cChecker = new CollisionChecker(this);
	public AssetSetter aSetter = new AssetSetter(this);
	public UI ui = new UI(this);
	TileManager tileM = new TileManager(this);
	public KeyHandler keyH = new KeyHandler(this);
	Sound music = new Sound();
	Sound se = new Sound();
	Thread gameThread;
	
	//ENTITY & OBJECT
	public Player player = new Player(this, keyH);   // Player
	public SuperObject obj[] = new SuperObject[10];  // How Many Objects At A Time
	public Entity npc[] = new Entity[10];
	
	
	//GAME STATE
	public int gameState;
	public final int titleState = 0;
	public final int playState = 1;
	public final int pauseState = 2;
	public final int dialogueState = 3;
	
	public GamePanel() {
		
		this.setPreferredSize(new Dimension(screenWidth, screenHeight));
		this.setBackground(Color.black);
		this.setDoubleBuffered(true);
		this.addKeyListener(keyH);
		this.setFocusable(true);
	}
	public void setupGame() {
		
		aSetter.setObject();
		aSetter.setNPC();
//		playMusic(0);
		gameState = titleState;
	}
	public void startGameThread() {
		
		gameThread = new Thread(this);
		gameThread.start();
	}
	@Override
	public void run() {
		double drawInterval = 1000000000/FPS;
		double delta = 0;
		long lastTime = System.nanoTime();
		long currentTime;
		long timer = 0;
		int drawCount = 0;
		while(gameThread != null) {
			currentTime = System.nanoTime();
			delta += (currentTime - lastTime) / drawInterval;
			timer += (currentTime - lastTime);
			lastTime = currentTime;
			if(delta >= 1) {
				update();
				repaint();
				delta--;
				drawCount++;
			}
			if(timer >= 1000000000) {
				System.out.println("FPS : "+drawCount);
				drawCount = 0;
				timer = 0;
			}
			}
	}
	public void update() {
		
		if(gameState == playState) {
			player.update();
			for(int i=0; i<npc.length; i++) {
				if(npc[i] != null) {
					npc[i].update();
				}
			}
		}else if(gameState == pauseState) {
			//PLACEHOLDER
		}
	}
	public void paintComponent(Graphics g) {
		
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;
		//DEBUG
		long drawStart = 0;
		if(keyH.debugPressed) {
			drawStart = System.nanoTime();	
		}
		//GAME STATE
		if(gameState == titleState) {
			ui.draw(g2);
		}else {
			//TILE
			tileM.draw(g2);
			//OBJECT
			for(int i=0; i<obj.length; i++) {
				if(obj[i] != null) {
					obj[i].draw(g2, this);
				}
			}
			//NPC
			for(int i=0; i<npc.length; i++) {
				if(npc[i] != null) {
					npc[i].draw(g2);
				}
			}
			//PLAYER
			player.draw(g2);
			//UI
			ui.draw(g2);
			g2.dispose();
		}
		//DEBUG
		if(keyH.debugPressed) {
			long drawComp = System.nanoTime();
			long drawTime = drawComp - drawStart;
			System.out.println("Draw Time : "+drawTime+"nanoseconds\n");
		}
	}
	public void playMusic(int i) {
		music.setFile(i);
		music.play();
		music.loop();
	}
	public void stopMusic() {
		music.stop();
	}
	public void playSE(int i) {
		se.setFile(i);
		se.play();
	}
}
