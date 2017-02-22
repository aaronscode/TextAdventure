package textadventure;

import org.apache.commons.configuration.ConfigurationException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

public class TextAdventure extends Component {
    static final String configFilePath = "settings.xml";

    static JFrame f;
    int fWidth, fHeight;

    private String glyphSpritePath;
    private int tileWidth, tileHeight;

    private SpriteSheet sprites;

    private Configurator configurator;

    private TextAdventure() {
        this.parseConfig();
        this.sprites = new SpriteSheet(this.glyphSpritePath, this.tileWidth, this.tileHeight);
    }

    private void parseConfig() {
        Configurator c;
        File config = new File(this.configFilePath);

        if(config.exists() && !config.isDirectory()) {
            try {
                c = new Configurator(this.configFilePath);
            } catch (ConfigurationException e) {
                e.printStackTrace();
                c = new Configurator();
            }
        } else {
            c = new Configurator();
        }
        this.configurator = c;
        this.glyphSpritePath = c.getString("glyphSprites");
        this.fWidth = c.getInt("frameWidth");
        this.fHeight = c.getInt("frameHeight");
        this.tileWidth = c.getInt("tileWidth");
        this.tileHeight = c.getInt("tileHeight");
    }

    public void saveConfig() {
        try {
            this.configurator.save();
        } catch (ConfigurationException e) {
            e.printStackTrace();
        }
    }

    public void paint(Graphics g) {
        sprites.drawTileNumC(SpriteSheet.T_UP_LEFT_CORNER, 0, 0, Color.white, g);
        sprites.drawTileNumC(SpriteSheet.T_UP_AND_DOWN_ARROW_BAR, 7*this.tileWidth, 7*this.tileHeight, Color.white, g);
        sprites.drawCharC('*', 8*this.tileWidth, 7*this.tileHeight, Color.orange, g);
    }

    public Dimension getPreferredSize() {
        return new Dimension(this.fWidth, this.fHeight);
    }

    public static void main(String[] args) {
        f = new JFrame("Text Adventure");

        TextAdventure txtA = new TextAdventure();

        f.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                super.windowClosing(windowEvent);
                System.exit(0);
            }
        });


	    f.getContentPane().setBackground(Color.black);
	    f.add(txtA);
	    f.pack();
	    f.setResizable(false);
	    f.setVisible(true);
    }
}
