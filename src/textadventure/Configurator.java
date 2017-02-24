package textadventure;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;

import java.io.File;

public class Configurator extends XMLConfiguration {
    static final String DEFAULT_CONFIG_PATH = "settings.xml";
    static final String DEFAULT_GLYPH_SPRITES = "res/CodePageTransparent2x.png";
    static final int DEFAULT_TILE_WIDTH = 18;
    static final int DEFAULT_TILE_HEIGHT = 32;
    static final int DEFAULT_FRAME_WIDTH = 800;
    static final int DEFAULT_FRAME_HEIGHT = 800;
    static final int DEFAULT_TARGET_FPS = 60;

    public Configurator() {
            super();
            this.setFileName(DEFAULT_CONFIG_PATH);
            this.setProperty("glyphSprites", DEFAULT_GLYPH_SPRITES);
            this.setProperty("tileWidth", DEFAULT_TILE_WIDTH);
            this.setProperty("tileHeight", DEFAULT_TILE_HEIGHT);
            this.setProperty("frameWidth", DEFAULT_FRAME_WIDTH);
            this.setProperty("frameHeight", DEFAULT_FRAME_HEIGHT);
            this.setProperty("targetFPS", DEFAULT_TARGET_FPS);

            try {
                this.save();
            } catch (ConfigurationException e) {
                e.printStackTrace();
            }
    }

    public Configurator(String configPath) throws ConfigurationException {
        super(configPath);
    }
}
