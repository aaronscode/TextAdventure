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

    private ImageFont sprites;

    private TextAdventure() {
        this.parseConfig();
        // TODO: Refactor all of this
        this.sprites = new ImageFont(this.glyphSpritePath);
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
        this.glyphSpritePath = c.getString("glyphSprites");
        this.fWidth = c.getInt("frameWidth");
        this.fHeight = c.getInt("frameHeight");
        this.tileWidth = c.getInt("tileWidth");
        this.tileHeight = c.getInt("tileHeight");
        /*
        List<String> configFile = null;
        try {
            configFile = Files.readAllLines(configFilePath, StandardCharsets.US_ASCII);
        } catch (IOException e) {
            System.out.println("Config File not found");
            System.exit(1);
        }

        for(String configLine : configFile) {
            String[] parts = configLine.split(" ");
            else if(parts[0].equals("tileWidth")) this.tileWidth = Integer.parseInt(parts[1]);
        }
        */
    }

    public void paint(Graphics g) {
        sprites.drawTileNumC(ImageFont.T_UP_LEFT_CORNER, 0, 0, Color.white, g);
    }

    public Dimension getPreferredSize() {
        return new Dimension(this.fWidth, this.fHeight);
    }

    public static void main(String[] args) {
        f = new JFrame("Text Adventure");

        f.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                super.windowClosing(windowEvent);
                System.exit(0);
            }
        });

	    TextAdventure txtA = new TextAdventure();

	    f.getContentPane().setBackground(Color.black);
	    f.add(txtA);
	    f.pack();
	    f.setResizable(false);
	    f.setVisible(true);
    }
}
