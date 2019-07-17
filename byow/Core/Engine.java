package byow.Core;

import byow.InputDemo.InputSource;
import byow.TileEngine.*;
import edu.princeton.cs.introcs.StdDraw;

import java.util.Date;

import byow.InputDemo.KeyboardInput;
import java.util.Random;

import java.awt.Color;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.ObjectOutputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class Engine {
    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 30;
    private static final int KEYBOARD = 0;
    private static final int RANDOM = 1;
    private static final int STRING = 2;
    TERenderer ter = new TERenderer();
    private int playermovex;
    private int playermovey;
    private MapGenerator loadmap;
    private TETile[][] loadworld;
    private ArrayList<Character> charnumber = new ArrayList<>();
    private InputSource inputSource;
    private int mousex; //= (int) StdDraw.mouseX();
    private int mousey; // = (int) StdDraw.mouseY();
    private int mouseEnx;
    private  int mouseEny;
    private int playerencounterx, playerencountery;
    private int totalscore = 0;
    private Encounter encounter;
    private TETile[][] encounterWorld;


    private static void saveMap(MapGenerator map) {
        File f = new File("./save_map.txt");
        try {
            if (!f.exists()) {
                f.createNewFile();
            }
            FileOutputStream fs = new FileOutputStream(f);
            ObjectOutputStream os = new ObjectOutputStream(fs);
            os.writeObject(map);
        } catch (FileNotFoundException e) {
            System.out.println("file not found");
            System.exit(0);
        } catch (IOException e) {
            System.out.println(e);
            System.exit(0);
        }
    }

    private static MapGenerator loadMap() {
        File f = new File("./save_map.txt");
        if (f.exists()) {
            try {
                FileInputStream fs = new FileInputStream(f);
                ObjectInputStream os = new ObjectInputStream(fs);
                return (MapGenerator) os.readObject();
            } catch (FileNotFoundException e) {
                System.out.println("file not found");
                System.exit(0);
            } catch (IOException e) {
                System.out.println(e);
                System.exit(0);
            } catch (ClassNotFoundException e) {
                System.out.println("class not found");
                System.exit(0);
            } /*catch (Exception e) {
                System.out.println(e.getMessage());
                System.exit(0);
            } */
        }
        return new MapGenerator((long) 12324);
    }

    public static void main(String[] args) {
        Engine engine = new Engine();
        //engine.encounter();
        engine.interactWithKeyboard();
        //engine.show(engine.interactWithInputString("Nssss12nmnb
        // mnbk3432Sdhjklkjdsss:qlkjnkkjnkw:wuygbjqn908kkksddddd"));
        //engine.show(engine.interactWithInputString("n121swwwddd"));
    }

    /**
     * Method used for exploring a fresh world. This method should handle all inputs,
     * including inputs from the main menu.
     */

    private boolean checkChar(char c) {
        return (charnumber.contains(c));
    }

    public void interactWithKeyboard() {
        constructcharnumber();
        inputSource = new KeyboardInput();
        while (true) {
            String number = "";
            char c = inputSource.getNextKey();
            if (c == 'N') {
                StdDraw.clear(StdDraw.BLACK);
                StdDraw.text(0.5, 0.5, "Enter number and end with S");
                char num = inputSource.getNextKey();
                while (num == 'S') {
                    StdDraw.clear(StdDraw.BLACK);
                    StdDraw.text(0.5, 0.5, "Please Enter number first");
                    num = inputSource.getNextKey();
                }
                while (num != 'S') {
                    if (checkChar(num)) {
                        String num1 = String.valueOf(num); //convert type char to string
                        number += num1;
                        StdDraw.clear(StdDraw.BLACK);
                        StdDraw.text(0.5, 0.5, "Enter number and end with S");
                    } else {
                        StdDraw.clear(StdDraw.BLACK);
                        StdDraw.text(0.5, 0.5, "Please Enter Number");
                    }
                    num = inputSource.getNextKey();
                }
                Long seed = Long.parseLong(number);
                MapGenerator map = new MapGenerator(seed);
                loadmap = map;
                loadworld = loadmap.getworld();
                playermovex = loadmap.getPlayerx();
                playermovey = loadmap.getPlayery();
                whileloop();
                char direction = inputSource.getNextKey();
                while (direction != ':') {
                    move(direction, loadmap.getworld());
                    ter.renderFrame(loadmap.getworld());
                    whileloop();
                    direction = inputSource.getNextKey();
                }
                direction = inputSource.getNextKey();
                while (direction != 'Q') {
                    direction = inputSource.getNextKey();
                }
                loadmap.updatePlayerx(playermovex);
                loadmap.updatePlayery(playermovey);
                saveMap(loadmap);
                System.exit(0);
            }
            if (c == 'L') {
                loadmap = loadMap();
                loadworld = loadmap.getworld();
                initialize(loadmap.getworld());
                playermovex = loadmap.getPlayerx();
                playermovey = loadmap.getPlayery();
                loadmap.getTer().renderFrame(loadmap.getworld());
                whileloop();
                char direction = inputSource.getNextKey();
                while (direction != ':') {
                    move(direction, loadmap.getworld());
                    loadmap.getTer().renderFrame(loadmap.getworld());
                    whileloop();
                    direction = inputSource.getNextKey();
                }
                direction = inputSource.getNextKey();
                while (direction != 'Q') {
                    direction = inputSource.getNextKey();
                }
                loadmap.updatePlayerx(playermovex);
                loadmap.updatePlayery(playermovey);
                saveMap(loadmap);
                System.exit(0);
            }
            if (c == 'Q') {
                System.exit(0);
            }
        }
    }

    private void encounter () {
        encounterWorld = new TETile[80][40];
        //loadmap = new MapGenerator((long)1235677621);
        encounter = new Encounter(encounterWorld, loadmap.getRandom());
        show(encounterWorld);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.text(60, 5, "Using WASD to move, press Q to end the encounter");
        StdDraw.show();
        StdDraw.pause(1200);
        playerencounterx = encounter.getPlayerx();
        playerencountery = encounter.getPlayery();
        whileloopforEN();
        char direction = inputSource.getNextKey();
        while (direction != 'Q') {
            moveEncount(direction, encounterWorld);
            whileloopforEN();
            ter.renderFrame(encounterWorld);
            direction = inputSource.getNextKey();
        }
        ter.renderFrame(loadmap.getworld());
        encounter.updatePlayerx(playerencounterx);
        encounter.updatePlayery(playerencountery);
    }

    private void whileloop() {
        while (!StdDraw.hasNextKeyTyped()) {
            if ((int) StdDraw.mouseX() < loadmap.getMapwidth() && (int) StdDraw.mouseX() > -1
                    && (int) StdDraw.mouseY() < loadmap.getMapheight() - 1 && (int)
                    StdDraw.mouseY() > -1) {
                if (!loadworld[mousex][mousey].equals(loadworld[(int) StdDraw.mouseX()][(int)
                        StdDraw.mouseY()])) {
                    loadmap.getTer().renderFrame(loadmap.getworld());
                    header(loadmap.getworld());
                }
                header(loadmap.getworld());
            }
        }
    }

    private void whileloopforEN() {
        while (!StdDraw.hasNextKeyTyped()) {
            if ((int) StdDraw.mouseX() < encounter.getMapwidth() && (int) StdDraw.mouseX() > -1
                    && (int) StdDraw.mouseY() < encounter.getMapheight() - 1 && (int)
                    StdDraw.mouseY() > -1) {
                if (!encounterWorld[mouseEnx][mouseEny].equals(encounterWorld[(int) StdDraw.mouseX()][(int)
                        StdDraw.mouseY()])) {
                    ter.renderFrame(encounterWorld);
                    headerforEN(encounterWorld);
                }
                headerforEN(encounterWorld);
            }
        }
    }

    private TETile[][] newgame(char[] chararray, String input) {
        int index = 1;
        String seeds = "";
        while (Character.toUpperCase(chararray[index]) == 'S') {
            index += 1;
        }
        while (Character.toUpperCase(chararray[index]) != 'S') {
            if (charnumber.contains(chararray[index])) {
                seeds += input.substring(index, index + 1);
                index += 1;
            } else {
                index += 1;
            }
        }
        Long seed = Long.parseLong(seeds);
        MapGenerator map = new MapGenerator(seed);
        loadmap = map;
        playermovex = loadmap.getPlayerx();
        playermovey = loadmap.getPlayery();
        if (index == input.length() - 1) {
            saveMap(loadmap);
            return loadmap.getworld();
        }
        while (chararray[index] != ':') {
            move(Character.toUpperCase(chararray[index]), loadmap.getworld());
            index += 1;
            if (index == chararray.length - 1) {
                move(Character.toUpperCase(chararray[index]), loadmap.getworld());
                return loadmap.getworld();
            }
        }
        while (Character.toUpperCase(chararray[index]) != 'Q') {
            index += 1;
            if (index == chararray.length - 1) {
                loadmap.updatePlayerx(playermovex);
                loadmap.updatePlayery(playermovey);
                saveMap(loadmap);
                return loadmap.getworld();
            }
        }
        loadmap.updatePlayerx(playermovex);
        loadmap.updatePlayery(playermovey);
        saveMap(loadmap);
        if (index < input.length()) {
            input = input.substring(index + 1);
            return interactWithInputString(input);
        } else {
            System.exit(0);
        }
        return loadmap.getworld();
    }


    private void header(TETile[][] twod) {
        StdDraw.disableDoubleBuffering();
        Date time = new Date();
        mousex = (int) StdDraw.mouseX();
        mousey = (int) StdDraw.mouseY();
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.text(40.0, 40.0, loadmap.getworld()[mousex][mousey].description());
        StdDraw.text(72.0, 40.0, time.toString());
        StdDraw.text(10.0, 40.0, "Total Score : " + totalscore);
        StdDraw.enableDoubleBuffering();
        ter.renderFrame(loadmap.getworld());
    }

    private void headerforEN(TETile[][] twod) {
        StdDraw.disableDoubleBuffering();
        Date time = new Date();
        mouseEnx = (int) StdDraw.mouseX();
        mouseEny = (int) StdDraw.mouseY();
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.text(40.0, 40.0, encounterWorld[mouseEnx][mouseEny].description());
        StdDraw.text(72.0, 1.0, time.toString());
        StdDraw.text(10.0, 40.0, "Total Score : " + totalscore);
        StdDraw.enableDoubleBuffering();
        ter.renderFrame(encounterWorld);
    }

    //move methods for general maps
    public void moveup(int playerx, int playery, TETile[][] world) {
        if (world[playerx][playery + 1].equals(Tileset.WALL)) {
            return;
        } else if (world[playerx][playery + 1].equals(Tileset.LOCKED_DOOR)) {
            world[playerx][playery + 1] = Tileset.FLOOR;
            encounter();
        } else {
            world[playerx][playery + 1] = Tileset.FLOWER;
            world[playerx][playery] = Tileset.FLOOR;
            playermovey += 1;

        }
    }

    public void movedown(int playerx, int playery, TETile[][] world) {
        if (world[playerx][playery - 1].equals(Tileset.WALL)) {
            return;
        } else if (world[playerx][playery - 1].equals(Tileset.LOCKED_DOOR)) {
            world[playerx][playery - 1] = Tileset.FLOOR;
            encounter();
        } else {
            world[playerx][playery - 1] = Tileset.FLOWER;
            world[playerx][playery] = Tileset.FLOOR;
            playermovey -= 1;
        }
    }

    public void moveleft(int playerx, int playery, TETile[][] world) {
        if (world[playerx - 1][playery].equals(Tileset.WALL)) {
            return;
        } else if (world[playerx - 1][playery].equals(Tileset.LOCKED_DOOR)) {
            world[playerx - 1][playery] = Tileset.FLOOR;
            encounter();
        } else {
            world[playerx - 1][playery] = Tileset.FLOWER;
            world[playerx][playery] = Tileset.FLOOR;
            playermovex -= 1;
        }
    }

    public void moveright(int playerx, int playery, TETile[][] world) {
        if (world[playerx + 1][playery].equals(Tileset.WALL)) {
            return;
        } else if (world[playerx + 1][playery].equals(Tileset.LOCKED_DOOR)) {
            world[playerx + 1][playery] = Tileset.FLOOR;
            encounter();
        } else {
            world[playerx + 1][playery] = Tileset.FLOWER;
            world[playerx][playery] = Tileset.FLOOR;
            playermovex += 1;
        }
    }

    private void move(char direction, TETile[][] world) {
        if (direction == 'W') {
            moveup(playermovex, playermovey, world);
        }
        if (direction == 'A') {
            moveleft(playermovex, playermovey, world);
        }
        if (direction == 'S') {
            movedown(playermovex, playermovey, world);
        }
        if (direction == 'D') {
            moveright(playermovex, playermovey, world);
        }
    }

    //move methods for encounter world
    public void moveupEN(int playerx, int playery, TETile[][] world) {
        if (world[playerx][playery + 1].equals(Tileset.WALL)) {
            return;
        } else if (world[playerx][playery + 1].equals(Tileset.UNKNOWN)) {
            scoreordeath(playerx, playery + 1, encounter);
        }
        world[playerx][playery + 1] = Tileset.FLOWER;
        world[playerx][playery] = Tileset.FLOOR;
        playerencountery += 1;
    }

    private void scoreordeath(int x, int y, Encounter encounter) {
        if (x == encounter.getSixscore().getX() && y == encounter.getSixscore().getY()) {
            totalscore += 6;
        }
        if (x == encounter.getFourscore().getX() && y == encounter.getFourscore().getY()) {
            totalscore += 4;
        }
        if (x == encounter.getEightscore().getX() && y == encounter.getEightscore().getY()) {
            totalscore += 8;
        }
        if (totalscore >= 12) {
            StdDraw.clear(Color.BLACK);
            StdDraw.setPenColor(Color.WHITE);
            StdDraw.text(40.0, 20.0, "You Win!!!! Tell Your " +
                    "Mom You Won The Hardest Game In The World!!");
            StdDraw.show();
            StdDraw.pause(4000);
            System.exit(0);
        }
        if (x == encounter.getDeath().getX() && y == encounter.getDeath().getY()) {
            StdDraw.clear(Color.BLACK);
            StdDraw.setPenColor(Color.WHITE);
            StdDraw.text(40.0, 20.0, "GAME OVER!!!!!");
            //StdDraw.text(40.0, 19.0, "Press E to exit");
            StdDraw.show();
            StdDraw.pause(2500);
            System.exit(0);
        }
    }

    public void movedownEN(int playerx, int playery, TETile[][] world) {
        if (world[playerx][playery - 1].equals(Tileset.WALL)) {
            return;
        } else if (world[playerx][playery - 1].equals(Tileset.UNKNOWN)) {
            scoreordeath(playerx, playery - 1, encounter);
        }
        world[playerx][playery - 1] = Tileset.FLOWER;
        world[playerx][playery] = Tileset.FLOOR;
        playerencountery -= 1;
    }

    public void moveleftEN(int playerx, int playery, TETile[][] world) {
        if (world[playerx - 1][playery].equals(Tileset.WALL)) {
            return;
        } else if (world[playerx - 1][playery].equals(Tileset.UNKNOWN)) {
            scoreordeath(playerx - 1, playery, encounter);
        }
        world[playerx - 1][playery] = Tileset.FLOWER;
        world[playerx][playery] = Tileset.FLOOR;
        playerencounterx -= 1;
    }

    public void moverightEN(int playerx, int playery, TETile[][] world) {
        if (world[playerx + 1][playery].equals(Tileset.WALL)) {
            return;
        } else if (world[playerx + 1][playery].equals(Tileset.UNKNOWN)) {
           scoreordeath(playerx + 1, playery, encounter);
        }
        world[playerx + 1][playery] = Tileset.FLOWER;
        world[playerx][playery] = Tileset.FLOOR;
        playerencounterx += 1;
    }
    private void moveEncount(char direction, TETile[][] world) {
        if (direction == 'W') {
            moveupEN(playerencounterx, playerencountery, world);
        }
        if (direction == 'A') {
            moveleftEN(playerencounterx, playerencountery, world);
        }
        if (direction == 'S') {
            movedownEN(playerencounterx, playerencountery, world);
        }
        if (direction == 'D') {
            moverightEN(playerencounterx, playerencountery, world);
        }
    }

    private void load() {
        StdDraw.clear(StdDraw.BLACK);
        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.text(40, 27, "CS61B: THE GAME");
        StdDraw.text(40, 23, "NEW GAME(N)");
        StdDraw.text(40, 18, "LOAD GAME(L)");
        StdDraw.text(40, 14, "QUIT(Q)");
        StdDraw.show();
    }


    private void constructcharnumber() {
        charnumber.add('0');
        charnumber.add('1');
        charnumber.add('2');
        charnumber.add('3');
        charnumber.add('4');
        charnumber.add('5');
        charnumber.add('6');
        charnumber.add('7');
        charnumber.add('8');
        charnumber.add('9');
    }

    public void initialize(TETile[][] world) {
        StdDraw.setCanvasSize(world.length * 16, world[0].length * 16);
        StdDraw.setXscale(0, world.length);
        StdDraw.setYscale(0, world[0].length);
        StdDraw.clear(Color.BLACK);
        StdDraw.enableDoubleBuffering();
    }

    public void show(TETile[][] world) {
        int numXTiles = world.length;
        int numYTiles = world[0].length;
        StdDraw.clear(new Color(0, 0, 0));
        for (int x = 0; x < numXTiles; x += 1) {
            for (int y = 0; y < numYTiles; y += 1) {
                if (world[x][y] == null) {
                    throw new IllegalArgumentException("Tile at position x=" + x + ", y=" + y
                            + " is null.");
                }
                world[x][y].draw(x, y);
            }
        }
        StdDraw.show();
    }

    /**
     * Method used for autograding and testing your code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The engine should
     * behave exactly as if the user typed these characters into the engine using
     * interactWithKeyboard.
     * <p>
     * Recall that strings ending in ":q" should cause the game to quite save. For example,
     * if we do interactWithInputString("n123sss:q"), we expect the game to run the first
     * 7 commands (n123sss) and then quit and save. If we then do
     * interactWithInputString("l"), we should be back in the exact same state.
     * <p>
     * In other words, both of these calls:
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
        char[] chararray = input.toCharArray();
        constructcharnumber();
        if (Character.toUpperCase(chararray[0]) == 'N') {
            newgame(chararray, input);
        }
        if (Character.toUpperCase(chararray[0]) == 'L') {
            loadmap = loadMap();
            playermovex = loadmap.getPlayerx();
            playermovey = loadmap.getPlayery();
            int index = 1;
            while (chararray[index] != ':') {
                move(Character.toUpperCase(chararray[index]), loadmap.getworld());
                index += 1;
                if (index == chararray.length - 1) {
                    move(Character.toUpperCase(chararray[index]), loadmap.getworld());
                    return loadmap.getworld();
                }
            }
            while (Character.toUpperCase(chararray[index]) != 'Q') {
                index += 1;
                if (index == chararray.length - 1) {
                    loadmap.updatePlayerx(playermovex);
                    loadmap.updatePlayery(playermovey);
                    saveMap(loadmap);
                    return loadmap.getworld();
                }
            }
            loadmap.updatePlayerx(playermovex);
            loadmap.updatePlayery(playermovey);
            saveMap(loadmap);
            if (index < input.length()) {
                input = input.substring(index + 1);
                return interactWithInputString(input);
            } else {
                System.exit(0);
            }
        }
        if (Character.toUpperCase(chararray[0]) == 'Q') {
            input = input.substring(1);
            if (input.length() > 0) {
                return interactWithInputString(input);
            } else {
                System.exit(0);
            }

        }
        TETile[][] finalWorldFrame = loadmap.getworld();
        return finalWorldFrame;
    }
}
