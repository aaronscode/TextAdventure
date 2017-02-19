package com.aaron;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class TextAdventure extends Component {
    static final Path configFilePath = Paths.get("main.conf");

    static JFrame f;
    int fWidth, fHeight;

    private String GlyphSpritePath;
    private int tileWidth, tileHeight;

    private TextAdventure() {
        this.parseConfig();
    }

    private void parseConfig() {
        List<String> configFile = null;
        try {
            configFile = Files.readAllLines(configFilePath, StandardCharsets.US_ASCII);
        } catch (IOException e) {
            System.out.println("Config File not found");
            System.exit(1);
        }

        for(String configLine : configFile) {
            String[] parts = configLine.split(" ");
            if(parts[0].equals("GlyphSprites")) this.GlyphSpritePath = parts[1];
            else if(parts[0].equals("tileWidth")) this.tileWidth = Integer.parseInt(parts[1]);
            else if(parts[0].equals("tileHeight")) this.tileHeight = Integer.parseInt(parts[1]);
            else if(parts[0].equals("frameWidth")) this.fWidth = Integer.parseInt(parts[1]);
            else if(parts[0].equals("frameHeight")) this.fHeight = Integer.parseInt(parts[1]);
        }
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
