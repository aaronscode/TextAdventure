package textadventure;

import java.awt.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;

public class SpriteSheet {
    BufferedImage tilesImg; // entire image with all character tiles
    BufferedImage[] tiles;  // break down original image into array of individual
    // easily indexible character tiles

    int imgW, imgH; // width and height of image containing character tiles
    int tW, tH; // individual tile width and height
    int numCols, numRows; // number of columns and rows of tiles in the image
    int numTiles; // total number of tiles, numCols * numRows

    static final String DEFAULT_SPRITESHEET_LOC = "res/CodePageTransparent.png";
    static final int DEFAULT_TILE_WIDTH = 9;
    static final int DEFAULT_TILE_HEIGHT = 16;

    /* ---------------------------------------------------------*
     * Constructors                                             *
     * ---------------------------------------------------------*/

    // Default constructor
    public SpriteSheet() {
        this(DEFAULT_SPRITESHEET_LOC);
    }

    public SpriteSheet(String pathToImage) {
        this(pathToImage, DEFAULT_TILE_WIDTH, DEFAULT_TILE_HEIGHT);
    }

    public SpriteSheet(String pathToImage, int tileWidth, int tileHeight) {
        try {
            tilesImg = convertToARGB(ImageIO.read(new File(pathToImage))); // load the sprite sheet with alpha

            // set/calculate dimensions of sprite sheets and sprites
            this.imgW = tilesImg.getWidth();
            this.imgH = tilesImg.getHeight();
            this.tW = tileWidth;
            this.tH = tileHeight;
            this.numCols = this.imgW / this.tW;
            this.numRows = this.imgH / this.tH;
            this.numTiles = numCols * numRows;

            this.tiles = new BufferedImage[this.numTiles]; // allocate an array to hold each sprite from the sheet

            // add each sprite from the sheet to the array of sprites
            for(int i = 0; i < numCols; i++) {
                for(int j = 0; j < numRows; j++) {
                    tiles[(numCols * j) + i] = tilesImg.getSubimage(i * tW,  j * tH, tW,tH);
                }
            }
        } catch (IOException e) {
            System.out.println("Couldn't load the image: " + e);
        }
    }
    /* ------------------------------------------------------------------------------*
    * Drawing methods                                                                *
    * ------------------------------------------------------------------------------*/
    // These methods are all for drawing individual sprites. Since the sprites I'm
    // using are largely type-able characters, I have methods for drawing characters,
    // and strings, but these eventually all funnel down to the draw tile num methods,
    // where the value of each char is converted to an int for indexing the array of
    // sprites.
    //
    // There are several main types of drawing methods. Let's take for example
    // drawChar(), which is the most simple character drawing method. It draws a single
    // character with the default character of white.
    // Adding an 's' to the end of the
    // method name (drawChars, now), means that thing you are drawing an array of things.
    // The drawing methods will automatically space each consecutive sprite one tile-width
    // apart, so that the sprites don't overlap.
    // The next modifier is a 'C' (as in drawCharC), which means the method allows you to
    // specify a color other than white to color the sprite.
    // Logically, then, drawCharsC lets you draw an array of characters all with the same
    // specified color.
    // Finally, an 'sCs' (i.e. drawCharsCs) modifier means you specify a color for each
    // character as an input argument in the form of a Color array.

    public void drawChar(char ch, int x, int y, Graphics g) {
        int ascii = (int) ch;
        drawTileNum(ascii, x, y, g);
    }

    public void drawChars(char[] chs, int x, int y, Graphics g) {
        int len = chs.length;
        for(int i = 0; i < len; i++) {
            drawChar(chs[i], x + this.tW * i, y, g);
        }
    }

    public void drawString(String s, int x, int y, Graphics g) {
        drawChars(s.toCharArray(), x, y, g);
    }

    public void drawTileNum(int tileNum, int x, int y, Graphics g) {
        drawTileNumC(tileNum, x, y, Color.WHITE, g);
    }

    public void drawTileNums(int[] tileNums, int x, int y, Graphics g) {
        int len = tileNums.length;
        for(int i = 0; i < len; i++) {
            drawTileNum(tileNums[i], x + this.tW * i, y, g);
        }
    }

    public void drawCharC(char ch, int x, int y, Color c, Graphics g) {
        int ascii = (int) ch;
        drawTileNumC(ascii, x, y, c, g);
    }

    public void drawCharsC(char[] chs, int x, int y, Color c, Graphics g) {
        int len = chs.length;
        for(int i = 0; i < len; i++) {
            drawCharC(chs[i], x + this.tW * i, y, c, g);
        }
    }

    public void drawCharsCs(char[] chs, int x, int y, Color[] cs, Graphics g) {
        int len = chs.length;
        for(int i = 0; i < len; i++) {
            drawCharC(chs[i], x + this.tW * i, y, cs[i], g);
        }
    }

    public void drawStringC(String s, int x, int y, Color c, Graphics g) {
        drawCharsC(s.toCharArray(), x, y, c, g);
    }

    public void drawStringCs(String s, int x, int y, Color[] cs, Graphics g) {
        drawCharsCs(s.toCharArray(), x, y, cs, g);
    }

    public void drawTileNumC(int tileNum, int x, int y, Color c, Graphics g) {
        BufferedImage coloredTile = tiles[tileNum];
        for(int i = 0; i < this.tW; i++) {
            for(int j = 0; j < this.tH; j++) {
                Color originalColor = new Color(coloredTile.getRGB(i, j), true);
                Color nc = new Color(c.getRed(), c.getGreen(), c.getBlue(), originalColor.getAlpha());
                coloredTile.setRGB(i, j, nc.getRGB());
            }
        }
        g.drawImage(tiles[tileNum], x, y, null);
    }

    public void drawTileNumsC(int[] tileNums, int x, int y, Color c, Graphics g) {
        int len = tileNums.length;
        for(int i = 0; i < len; i++) {
            drawTileNumC(tileNums[i], x + this.tW * i, y, c, g);
        }
    }

    public void drawTileNumsCs(int[] tileNums, int x, int y, Color[] cs, Graphics g) {
        int len = tileNums.length;
        for(int i = 0; i < len; i++) {
            drawTileNumC(tileNums[i], x + this.tW * i, y, cs[i], g);
        }
    }

    /* ------------------------------------------------------------------------------*
    * Getters / Setters                                                              *
    * ------------------------------------------------------------------------------*/

    public int tileWidth()  { return this.tW; }
    public int tileHeight() { return this.tH; }

    // from stack overflow, makes sure we get an image with an alpha channel.
    public static BufferedImage convertToARGB(BufferedImage image) {
        BufferedImage newImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = newImage.createGraphics();
        g.drawImage(image, 0, 0, null);
        g.dispose();
        return newImage;
    }

    /* ------------------------------------------------------------------------------ *
     * Tile ID constants                                                              *
     * ------------------------------------------------------------------------------ */
    // These are really only usefull if you're using codepage 437 as your sprite sheet
    // (see https://en.wikipedia.org/wiki/Code_page_437)
    // TODO: I plan on refactoring these out into their own class at some point.
    public static final int T_BLANK = 0;
    public static final int T_SMILEY = 1;
    public static final int T_INVERTED_SMILEY = 2;
    public static final int T_HEART = 3;
    public static final int T_DIAMOND = 4;
    public static final int T_SPADE = 5;
    public static final int T_CLUB = 6;
    public static final int T_PEG = 7;
    public static final int T_PEG_HOLE = 8;
    public static final int T_TUBE = 9;
    public static final int T_TUBE_HOLE = 10;
    public static final int T_MALE = 11;
    public static final int T_FEMALE = 12;
    public static final int T_SIXTEENTH_NOTE = 13;
    public static final int T_DOUBLE_SIXTEENTH_NOTE = 14;
    public static final int T_SNOWFLAKE = 15;
    public static final int T_RIGHT_TRAINGLE = 16;
    public static final int T_LEFT_TRIANGLE = 17;
    public static final int T_UP_AND_DOWN_ARROW = 18;
    public static final int T_DOUBLE_EXCLAMATION = 19;
    public static final int T_PARAGRAPH = 20;
    public static final int T_SECTION = 21;
    public static final int T_BOTTOM_BLOCK = 22;
    public static final int T_UP_AND_DOWN_ARROW_BAR = 23;
    public static final int T_UP_ARROW = 24;
    public static final int T_DOWN_ARROW = 25;
    public static final int T_RIGHT_ARROW = 26;
    public static final int T_LEFT_ARROW = 27;
    public static final int T_RIGHT_ANGLE = 28;
    public static final int T_LEFT_AND_RIGHT_ARROW = 29;
    public static final int T_UP_TRIANGLE = 30;
    public static final int T_DOWN_TRIANGLE = 31;
    // [insert alpha numberic characters that you can actually type here]
    public static final int T_VERT_BAR = 179;
    public static final int T_UP_RIGHT_CORNER = 191;
    public static final int T_BOT_LEFT_CORNER = 192;
    public static final int T_HORIZONTAL_BAR = 196;
    public static final int T_BOT_RIGHT_CORNER = 217;
    public static final int T_UP_LEFT_CORNER = 218;
}
