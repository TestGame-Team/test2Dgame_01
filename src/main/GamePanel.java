package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.*;
import javax.swing.JPanel;
import entity.Entity;
import entity.Player;
import tile.TileManager;
import world.WorldManager;

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
    int FPS = 60;
    private int currentFps;
    private int musicVolume = 70;
    private int sfxVolume = 70;

    //SYSTEM
    public CollisionChecker cChecker = new CollisionChecker(this);
    public AssetSetter aSetter = new AssetSetter(this);
    public UI ui = new UI(this);
    public final WorldManager worldManager =
            new WorldManager(maxWorldCol, maxWorldRow, "/maps/big-map_02.txt");
    TileManager tileM = new TileManager(this, worldManager.getMap());
    public final SaveManager saveManager = new SaveManager(this);
    public KeyHandler keyH = new KeyHandler(this);
    public EventHandler eHandler = new EventHandler(this);
    Sound music = new Sound();
    Sound se = new Sound();
    Thread gameThread;

    //ENTITY & OBJECT
    public Player player = new Player(this, keyH);
    public Entity obj[] = new Entity[10];
    public Entity npc[] = new Entity[10];
    public Entity monster[] = new Entity[10];
    ArrayList<Entity> entityList = new ArrayList<>();

    //GAME STATE
    public int gameState;
    public final int titleState = 0;
    public final int playState = 1;
    public final int pauseState = 2;
    public final int dialogueState = 3;

    //DEBUG
    private double renderTimeMs;

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
        aSetter.setMonster();
        // playMusic(0);
        gameState = titleState;
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        double drawInterval = 1000000000 / FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        long timer = 0;
        int drawCount = 0;

        while (gameThread != null) {
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            timer += (currentTime - lastTime);
            lastTime = currentTime;

            if (delta >= 1) {
                update();
                repaint();
                delta--;
                drawCount++;
            }

            if (timer >= 1000000000) {
                currentFps = drawCount;
                drawCount = 0;
                timer = 0;
            }
        }
    }

    public void update() {
        if (gameState == playState) {
            player.update();

            for (int i = 0; i < npc.length; i++) {
                if (npc[i] != null) {
                    npc[i].update();
                }
            }

            for (int i = 0; i < monster.length; i++) {
                if (monster[i] != null) {
                    if (monster[i].alive && !monster[i].non) {
                        monster[i].update();
                    } else {
                        monster[i] = null;
                    }
                }
            }
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        final boolean debug = keyH.debugPressed;
        final long drawStart = debug ? System.nanoTime() : 0L;

        if (gameState == titleState) {
            ui.draw(g2);
        } else {
            //TILE
            tileM.draw(g2);

            //ENTITY
            entityList.add(player);

            for (int i = 0; i < npc.length; i++) {
                if (npc[i] != null) {
                    entityList.add(npc[i]);
                }
            }

            for (int i = 0; i < obj.length; i++) {
                if (obj[i] != null) {
                    entityList.add(obj[i]);
                }
            }

            for (int i = 0; i < monster.length; i++) {
                if (monster[i] != null) {
                    entityList.add(monster[i]);
                }
            }

            //SORT
            Collections.sort(entityList, new Comparator<Entity>() {
                @Override
                public int compare(Entity e1, Entity e2) {
                    return Integer.compare(e1.worldY, e2.worldY);
                }
            });

            //DRAW
            for (int i = 0; i < entityList.size(); i++) {
                entityList.get(i).draw(g2);
            }

            entityList.clear();

            //UI
            ui.draw(g2);
        }

        if (debug) {
            renderTimeMs = (System.nanoTime() - drawStart) / 1_000_000.0;
            ui.drawDebugStats(g2, renderTimeMs, currentFps);
        }

        g2.dispose();
    }

    public int getMusicVolume() { return musicVolume; }
    public int getSfxVolume() { return sfxVolume; }

    public void setMusicVolume(int volume) {
        musicVolume = Math.max(0, Math.min(100, volume));
        music.setVolume(musicVolume / 100.0f);
    }

    public void setSfxVolume(int volume) {
        sfxVolume = Math.max(0, Math.min(100, volume));
        se.setVolume(sfxVolume / 100.0f);
    }

    public void playMusic(int i) {
        music.setVolume(musicVolume / 100.0f);
        music.setFile(i);
        music.play();
        music.loop();
    }

    public void stopMusic() {
        music.stop();
    }

    public void playSE(int i) {
        se.setVolume(sfxVolume / 100.0f);
        se.setFile(i);
        se.play();
    }
}
