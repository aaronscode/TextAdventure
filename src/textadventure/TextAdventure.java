package textadventure;

import org.apache.commons.configuration.ConfigurationException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.NoSuchElementException;

public class TextAdventure extends Component implements KeyListener{
    private static final String configFilePath = "settings.xml";

    private static JFrame container;
    private int fWidth, fHeight;

    private String glyphSpritePath;
    private int tileWidth, tileHeight;

    private SpriteSheet sprites;

    private Configurator configurator;
    private Boolean isRunning;
    private int targetFPS;

    private int charX, charY;

    private TextAdventure() {
        this.parseConfig();
        this.sprites = new SpriteSheet(this.glyphSpritePath, this.tileWidth, this.tileHeight);
        this.charX = this.charY = 0;
    }

    private void parseConfig() {
        Configurator c;
        File config = new File(TextAdventure.configFilePath);

        if(config.exists() && !config.isDirectory()) {
            try {
                c = new Configurator(TextAdventure.configFilePath);
            } catch (ConfigurationException e) {
                e.printStackTrace();
                c = new Configurator();
            }
        } else {
            c = new Configurator();
        }

        this.configurator = c;
        try {
            this.glyphSpritePath = c.getString("glyphSprites");
            this.fWidth = c.getInt("frameWidth");
            this.fHeight = c.getInt("frameHeight");
            this.tileWidth = c.getInt("tileWidth");
            this.tileHeight = c.getInt("tileHeight");
            this.targetFPS = c.getInt("targetFPS");
        } catch (NoSuchElementException e) {
            e.printStackTrace();
        }
    }

    private void saveConfig() {
        try {
            this.configurator.save();
        } catch (ConfigurationException e) {
            // TODO: Handle this better by reverting to default configs
            e.printStackTrace();
            System.exit(0);
        }
    }

    private void gameLoop() {
        isRunning = true;

        long lastLoopTime = System.nanoTime();
        long optimal_time = 1000000000 / targetFPS;
        int fps = 0;
        long lastFpsTime = 0;

        while(isRunning) {
            long now = System.nanoTime();
            long updateLength = now - lastLoopTime;
            lastLoopTime = now;

            double delta = updateLength / (double) optimal_time;
            lastFpsTime += updateLength;
            fps++;
            if(lastFpsTime >= 1000000000) {
                System.out.println("FPS: " + fps);
                lastFpsTime = 0;
                fps = 0;
            }

            this.updateGame(delta);
            this.render();
            try{
                Thread.sleep((lastLoopTime-System.nanoTime() + optimal_time)/1000000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        this.cleanAndExit();
    }

    private void updateGame(double delta) {

    }

    private void render() {
        this.repaint();
    }

    private void cleanAndExit() {
        System.out.println("Bye Bye!");
        this.saveConfig();
        System.exit(0);
    }

    public void paint(Graphics g) {
        /*
        sprites.drawTileNumC(SpriteSheet.T_UP_LEFT_CORNER, 0, 0, Color.white, g);
        sprites.drawTileNumC(SpriteSheet.T_UP_AND_DOWN_ARROW_BAR, 7*this.tileWidth, 7*this.tileHeight, Color.white, g);
        sprites.drawCharC('*', 8*this.tileWidth, 7*this.tileHeight, Color.orange, g);
        */
        sprites.drawChar('X', charX, charY, g);
    }

    public Dimension getPreferredSize() {
        return new Dimension(this.fWidth, this.fHeight);
    }

    // TODO: events should be put into a queue for the update method to parse
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_LEFT) {
            this.charX -= this.tileWidth;
        } else if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
            this.charX += this.tileWidth;
        } else if(e.getKeyCode() == KeyEvent.VK_UP) {
            this.charY -= this.tileHeight;
        } else if(e.getKeyCode() == KeyEvent.VK_DOWN) {
            this.charY += this.tileHeight;
        } else if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            this.cleanAndExit();
        }
    }

    public void keyReleased(KeyEvent e) {

    }
    public void keyTyped(KeyEvent e) {

    }

    public static void main(String[] args) {
        TextAdventure txtA = new TextAdventure();

        container = new JFrame("Text Adventure");
        container.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                super.windowClosing(windowEvent);
                txtA.cleanAndExit();
            }
        });
        container.addKeyListener(txtA);

        container.getContentPane().setBackground(Color.black);
        container.add(txtA);
        container.pack();
        container.setResizable(false);
        container.setVisible(true);

        txtA.gameLoop();
    }
}
