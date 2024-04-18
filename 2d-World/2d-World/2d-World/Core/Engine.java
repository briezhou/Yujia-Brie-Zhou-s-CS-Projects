package byow.Core;

import byow.InputDemo.KeyboardInputSource;
import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.algs4.StdDraw;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Scanner;

import static java.lang.Math.abs;


public class Engine {
    public static final int FRAMEPAUSE = 2000;
    public static final int FONTSIZE = 30;
    public static final int REFRESHTIME = 20;
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = FONTSIZE;
    public static final int MAXROOMSIZE = 6;

    private MyWorld world;
    private TETile[][] worldFrame = new TETile[WIDTH][HEIGHT];

    private StringBuffer inputHistory = new StringBuffer();
    private Avatar avatar;
    private long seed;
    private int lightOn = 1;
    private String block;
    private boolean gameOn;

    /**
     * Method used for exploring a fresh world. This method should handle all inputs,
     * including inputs from the main menu.
     */


    public void interactWithKeyboard() {
        KeyboardInputSource input = new KeyboardInputSource();
        StringBuffer sd = new StringBuffer();
        inputHistory.delete(0, inputHistory.length());
        createFrame();
        drawMainFrame();
        boolean isStart = false;
        boolean numValid = false;
        while (true) {
            char key = input.getNextKey();
            if (key == 'N' || key == 'n') {
                isStart = true;
                drawSeedFrame(sd.toString());
                inputHistory.append(key);
            } else if (key == 'Q' || key == 'q') {
                System.exit(0);
            } else if (key == 'S' || key == 's') {
                if (!isStart) {
                    drawWarningFrameNotStart();
                    StdDraw.pause(FRAMEPAUSE);
                    drawMainFrame();
                } else if (!numValid) {
                    drawWarningFrameStarted();
                    inputHistory.delete(0, inputHistory.length());
                    StdDraw.pause(FRAMEPAUSE);
                    drawMainFrame();
                } else {
                    inputHistory.append(key);
                    parseSeed(inputHistory.toString());
                    clearfile();
                    gameOn = true;
                    lightOn = 1;
                    gameStarter(input);
                    break;
                }
            } else if (key == 'L' || key == 'l') {
                String data = loadFile();
                if (data != null) {
                    clearfile();
                    gameLoader(data, input);
                    break;
                }
            } else if (isNumber(key)) {
                if (!isStart) {
                    drawWarningFrameNotStart();
                    StdDraw.pause(FRAMEPAUSE);
                    isStart = false;
                    drawMainFrame();
                } else {
                    numValid = true;
                    inputHistory.append(key);
                    sd.append(key);
                    drawSeedFrame(sd.toString());
                }
            } else {
                if (!isStart) {
                    drawWarningFrameNotStart();
                } else {
                    drawWarningFrameStarted();
                    inputHistory.delete(0, inputHistory.length());
                    sd.delete(0, sd.length());
                }
                StdDraw.pause(FRAMEPAUSE);
                drawMainFrame();
            }
        }
    }

    private String drawAppearanceFrame() {

        return null;
    }

    private String blockName(TERenderer r) {
        int x = r.MouseListenerX();
        int y = r.MouseListenerY();
        if (outOfFrame(x, y)) {
            return "HUD";
        }
        TETile tile = worldFrame[x][y];
        if (tile.equals(Tileset.FLOOR)) {
            return "Floor";
        } else if (tile.equals(Tileset.AVATAR)) {
            return "Avatar";
        } else if (tile.equals(Tileset.WALL)) {
            return "Wall";
        }
        return "Nothing";
    }

    private boolean outOfFrame(int x, int y) {
        return x >= WIDTH || y >= HEIGHT || x < 0 || y < 0;
    }

    private void createFrame() {
        StdDraw.setCanvasSize(WIDTH * TERenderer.TILE_SIZE, (HEIGHT + 2) * TERenderer.TILE_SIZE);
        Font font = new Font("Monaco", Font.BOLD, FONTSIZE);
        StdDraw.setFont(font);
        StdDraw.setXscale(0, WIDTH);
        StdDraw.setYscale(0, HEIGHT);
        StdDraw.clear(Color.BLACK);
        StdDraw.enableDoubleBuffering();
    }

    private void drawWarningFrameStarted() {
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.WHITE);
        Font fontBig = new Font("Monaco", Font.BOLD, FONTSIZE);
        StdDraw.setFont(fontBig);
        StdDraw.text(this.WIDTH / 2, this.HEIGHT / 2, "Please Enter a Valid Seed!");
        StdDraw.show();
    }

    private void drawWarningFrameNotStart() {
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.WHITE);
        Font fontBig = new Font("Monaco", Font.BOLD, FONTSIZE);
        StdDraw.setFont(fontBig);
        StdDraw.text(this.WIDTH / 2, this.HEIGHT / 2, "Type N,L or Q Please!");
        StdDraw.show();
    }

    private boolean isNumber(char key) {
        return key >= '0' && key <= '9';
    }

    private void drawSeedFrame(String s) {
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.WHITE);
        Font fontBig = new Font("Monaco", Font.BOLD, FONTSIZE);
        StdDraw.setFont(fontBig);
        StdDraw.text(this.WIDTH / 2, this.HEIGHT / 2 + 2, "Input the SEED Please:");
        StdDraw.text(this.WIDTH / 2, this.HEIGHT / 2 - 2, s);
        StdDraw.show();
    }

    private void gameLoader(String input, KeyboardInputSource keyBoardInput) {
        gameOn = true;
        lightOn = 1;
        inputHistory.delete(0, inputHistory.length());
        inputHistory.append(input);
        int a = input.indexOf("S");
        int b = input.indexOf("s");
        int index;
        if (a != -1) {
            index = a;
        } else {
            index = b;
        }
        seed = Long.parseLong(input.substring(1, index));
        world = new Beijing(seed, WIDTH, HEIGHT, MAXROOMSIZE);
        avatar = new Avatar(world, seed);
        ter.initialize(WIDTH, HEIGHT + 2);
        while (index < input.length()) {
            moveHandler(input.charAt(index));
            index++;
        }
        while (index < input.length()) {
            moveHandler(input.charAt(index));
            index++;
        }
        refreshFrame();
        while (gameOn) {
            if (keyBoardInput.possibleNextInput()) {
                char newKey = keyBoardInput.getNextKey();
                gameQuitter(newKey, keyBoardInput);
                if (!gameOn) {
                    break;
                }
                inputHistory.append(newKey); // add character to history
                moveHandler(newKey);
            }
            refreshFrame();
            StdDraw.pause(REFRESHTIME);
        }
        interactWithKeyboard();
    }

    private void drawMainFrame() {
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.WHITE);
        Font fontBig = new Font("Monaco", Font.BOLD, FONTSIZE);
        StdDraw.setFont(fontBig);
        StdDraw.text(this.WIDTH / 2, this.HEIGHT / 2 + 6, "BYOW");
        StdDraw.text(this.WIDTH / 2, this.HEIGHT / 2 + 1, "New Game (N)");
        StdDraw.text(this.WIDTH / 2, this.HEIGHT / 2 - 3, "Load Game (L)");
        StdDraw.text(this.WIDTH / 2, this.HEIGHT / 2 - 7, "Quit (Q)");
        StdDraw.show();
    }

    private void gameStarter(KeyboardInputSource input) {
        world = new Beijing(seed, WIDTH, HEIGHT, MAXROOMSIZE);
        avatar = new Avatar(world, seed);
        ter.initialize(WIDTH, HEIGHT + 2);
        gameOn = true;
        refreshFrame();
        while (gameOn) {
            if (input.possibleNextInput()) {
                char newKey = input.getNextKey();
                gameQuitter(newKey, input);
                if (!gameOn) {
                    break;
                }
                inputHistory.append(newKey); // add character to history
                moveHandler(newKey);
            }
            refreshFrame();
            StdDraw.pause(REFRESHTIME);
        }
        interactWithKeyboard();
    }

    private long parseSeed(String input) {
        int a = input.indexOf("S");
        int b = input.indexOf("s");
        int index;
        if (a != -1) {
            index = a;
        } else {
            index = b;
        }
        seed = Long.parseLong(input.substring(1, index));
        return seed;
    }

    private void gameQuitter(char newKey, KeyboardInputSource input) {
        if (newKey == ':') {
            char q = input.getNextKey();
            if (q == 'Q' || q == 'q') {
                gameOn = false;
                try {
                    saveFile();
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private void moveHandler(char newkey) {
        if (newkey == 'w' || newkey == 'W') {
            moveUp();
        }
        if (newkey == 'a' || newkey == 'A') {
            moveLeft();
        }
        if (newkey == 's' || newkey == 'S') {
            moveDown();
        }
        if (newkey == 'd' || newkey == 'D') {
            moveRight();
        }
        if (newkey == 'L' || newkey == 'l') {
            lightSwitch();
        }
    }

    private void lightSwitch() {
        lightOn = -lightOn;
        refreshFrame();
    }

    private void moveRight() {
        if (canMoveRight()) {
            avatar.moveRight();
        }
        refreshFrame();
    }

    private boolean canMoveRight() {
        return world.whatBlock(avatar.getX() + 1, avatar.getY()) == 1
                || world.whatBlock(avatar.getX() + 1, avatar.getY()) == 3;
    }

    private void moveDown() {
        if (canMoveDown()) {
            avatar.moveDown();
        }
        refreshFrame();
    }

    private boolean canMoveDown() {
        return world.whatBlock(avatar.getX(), avatar.getY() - 1) == 1
                || world.whatBlock(avatar.getX(), avatar.getY() - 1) == 3;
    }

    private void moveLeft() {
        if (canMoveLeft()) {
            avatar.moveLeft();
        }
        refreshFrame();
    }

    private boolean canMoveLeft() {
        return world.whatBlock(avatar.getX() - 1, avatar.getY()) == 1
                || world.whatBlock(avatar.getX() - 1, avatar.getY()) == 3;
    }

    private void moveUp() {
        if (canMoveUp()) {
            avatar.moveUp();
        }
        refreshFrame();
    }

    private boolean canMoveUp() {
        return world.whatBlock(avatar.getX(), avatar.getY() + 1) == 1
                || world.whatBlock(avatar.getX(), avatar.getY() + 1) == 3;
    }

    /**
     * Method used for autograding and testing your code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww"). The engine should
     * behave exactly as if the user typed these characters into the engine using
     * interactWithKeyboard.
     * <p>
     * Recall that strings ending in ":q" should cause the game to quite save. For example,
     * if we do interactWithInputString("n123sss:q"), we expect the game to run the first
     * 7 commands (n123sss) and then quit and save. If we then do
     * interactWithInputString("l"), we should be back in the exact same state.
     * <p>
     * In other words, running both of these:
     * - interactWithInputString("n123sss:q")
     * - interactWithInputString("lww")
     * <p>
     * should yield the exact same world state as:
     * - interactWithInputString("n123sssww")
     *
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */


    public TETile[][] interactWithInputString(String input) {
        // passed in as an argument, and return a 2D tile representation of the
        // world that would have been drawn if the same inputs had been given
        // to interactWithKeyboard().
        //
        // See proj3.byow.InputDemo for a demo of how you can make a nice clean interface
        // that works for many input types.
        String data = input;
        inputHistory.delete(0, inputHistory.length());

        if (input.charAt(0) == 'L' || input.charAt(0) == 'l') {
            String s = loadFile();
            data = s.substring(0, s.length() - 2) + input.substring(1);
        }
        clearfile();
        inputHistory.append(data);

        int a = data.indexOf("S");
        int b = data.indexOf("s");
        int index;
        if (a != -1) {
            index = a;
        } else {
            index = b;
        }
        seed = Long.parseLong(data.substring(1, index));

        //create world with seed
        //MyWorld testWorld = new Beijing(seed, WIDTH, HEIGHT, MAXROOMSIZE);
        world = new Beijing(seed, WIDTH, HEIGHT, MAXROOMSIZE);
        //get world frame from MyWorld
        //TETile[][] finalWorldFrame = new TETile[WIDTH][HEIGHT];
        //Avatar avatar = new Avatar(testWorld, seed);
        avatar = new Avatar(world, seed);
        //refreshFrame(testWorld.getWorld(), finalWorldFrame, avatar);
        ter.initialize(WIDTH, HEIGHT + 2);
        refreshFrame();
        //printworld(testWorld);
        while (index < data.length()) {
            if (data.charAt(index) == ':') {
                try {
                    saveFile();
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
            moveHandler(data.charAt(index));
            index++;
        }
        return worldFrame;
    }

    private void refreshFrame() {
        int x = avatar.getX();
        int y = avatar.getY();
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                if (lightOn == 1) {
                    if (world.whatBlock(i + 1, j + 1) == 0) {
                        worldFrame[i][j] = Tileset.NOTHING;
                    }
                    if (world.whatBlock(i + 1, j + 1) == 1) {
                        worldFrame[i][j] = Tileset.FLOOR;
                    }
                    if (world.whatBlock(i + 1, j + 1) == 2) {
                        worldFrame[i][j] = Tileset.WALL;
                    }
                    if (world.whatBlock(i + 1, j + 1) == 3) {
                        worldFrame[i][j] = Tileset.TREE;
                    }
                } else {
                    if (abs(i + 1 - x) + abs(j + 1 - y) <= 5) {
                        if (world.whatBlock(i + 1, j + 1) == 0) {
                            worldFrame[i][j] = Tileset.NOTHING;
                        }
                        if (world.whatBlock(i + 1, j + 1) == 1) {
                            worldFrame[i][j] = Tileset.FLOOR;
                        }
                        if (world.whatBlock(i + 1, j + 1) == 2) {
                            worldFrame[i][j] = Tileset.WALL;
                        }
                        if (world.whatBlock(i + 1, j + 1) == 3) {
                            worldFrame[i][j] = Tileset.TREE;
                        }
                    } else {
                        worldFrame[i][j] = Tileset.NOTHING;
                    }
                }
            }
        }
        worldFrame[avatar.getX() - 1][avatar.getY() - 1] = Tileset.AVATAR;
        block = blockName(ter);
        ter.renderFrame(worldFrame, block);
    }


    public void saveFile() throws FileNotFoundException {
        File saved = new File("saved.txt");
        Scanner data = new Scanner(saved);
        if (data.hasNextLine()) {
            String existingFile = new Scanner(saved).nextLine();
            try {
                saved.createNewFile();
                FileWriter write = new FileWriter("saved.txt");
                write.write(existingFile + inputHistory);
                write.close();
            } catch (IOException e) {
                System.out.print("error while writing new data to existing file");
            }
        } else {
            try {
                saved.createNewFile();
                FileWriter write = new FileWriter("saved.txt");
                write.write(String.valueOf(inputHistory));
                write.close();
            } catch (IOException e) {
                System.out.print("error while writing new data to existing file");
            }
        }
    }

    public String loadFile() {
        try {
            File saved = new File("saved.txt");
            String data = new Scanner(saved).nextLine();
            return data;
        } catch (IOException e) {
            System.out.println("error");
        } catch (NoSuchElementException e) {
            drawWarningFrameNoFile();
            StdDraw.pause(FRAMEPAUSE);
            drawMainFrame();
        }
        return null;
    }

    private void drawWarningFrameNoFile() {
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.WHITE);
        Font fontBig = new Font("Monaco", Font.BOLD, FONTSIZE);
        StdDraw.setFont(fontBig);
        StdDraw.text(this.WIDTH / 2, this.HEIGHT / 2, "There is No Game to load!");
        StdDraw.show();
    }

    private void clearfile() {
        try {
            FileWriter write = new FileWriter("saved.txt");
            write.write("");
            write.close();
        } catch (IOException e) {
            System.out.print("error clearing the file");
        }
    }

    private TETile[][] getWorldframe() {
        return worldFrame;
    }

    private static void printWorld(MyWorld testWorld) {
        int[][] world = testWorld.getWorld();
        for (int i = 0; i < world.length; i++) {
            for (int j = 0; j < world[i].length; j++) {
                System.out.print(world[i][j] + " ");
            }
            System.out.println();
        }
    }

}
