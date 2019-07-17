package byow.InputDemo;

import byow.SaveDemo.Editor;
import byow.TileEngine.MapGenerator;
import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.*;
import java.awt.event.*;

import java.io.*;
import java.util.ArrayList;

public class StartInput {
    private static final int KEYBOARD = 0;
    private static final int RANDOM = 1;
    private static final int STRING = 2;
    private int playermovex;
    private int playermovey;
    private MapGenerator loadmap;
    private TETile[][] loadworld;
    private ArrayList<Character> charnumber = new ArrayList<>();
    private InputSource inputSource;
    private int mousex = (int) StdDraw.mouseX();
    private int mousey = (int) StdDraw.mouseY();

    private boolean checkChar(char c) {
        System.out.print(charnumber.get(0));
        return (charnumber.contains(c));
    }

    //constructor
    public StartInput() {
        int inputType = KEYBOARD;

        if (inputType == KEYBOARD) {
            inputSource = new KeyboardInput();
        } else if (inputType == RANDOM) {
            inputSource = new RandomInputSource(50L);
        } else { // inputType == STRING
            inputSource = new StringInputDevice("HELLO MY FRIEND. QUACK QUACK");
        }

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
//        mouseheadtogether();
        while (true) {
            String number = "";
            char c = inputSource.getNextKey();
            //if new game
            if (c == 'N') {
                StdDraw.clear(StdDraw.BLACK);
                StdDraw.text(0.5, 0.5, "Enter number and end with S");
                char num = inputSource.getNextKey();
                if (num == 'S') {
                    StdDraw.clear(StdDraw.BLACK);
                    StdDraw.text(0.5, 0.5, "Please Enter number first");
                    num = inputSource.getNextKey();
                }
                while (num != 'S') {
                    if(checkChar(num)) {
                        String num1 = String.valueOf(num); //conver type char to string
                        number += num1;
                        StdDraw.clear(StdDraw.BLACK);
                        StdDraw.text(0.5, 0.5, "Enter number and end with S");
                    } else {
                        StdDraw.clear(StdDraw.BLACK);
                        StdDraw.text(0.5, 0.5, "Please Enter Number");
                    }
                    num = inputSource.getNextKey();
                }
                //generate map
                Long seed = Long.parseLong(number);
                MapGenerator map = new MapGenerator(seed);
                loadmap = map;
                loadworld = loadmap.getworld();
                playermovex = loadmap.getPlayerx();
                playermovey = loadmap.getPlayery();
                whileloop();
                char direction = inputSource.getNextKey();
                //move using W A S D
                while (direction != ':') {
                    //header(loadmap.getworld());
                    move(direction, loadmap.getworld());
                    loadmap.getTer().renderFrame(loadmap.getworld());
                    whileloop();
                    direction = inputSource.getNextKey();
                }
                direction = inputSource.getNextKey();
                //tell end or not
                while (direction != 'Q') {
                    direction = inputSource.getNextKey();
                }
                //save
                loadmap.updatePlayerx(playermovex);
                loadmap.updatePlayery(playermovey);
                saveMap(loadmap);
                System.exit(0);
            }
            if (c == 'L') {
                loadmap = loadMap();
                initialize(loadmap.getworld());
                playermovex = loadmap.getPlayerx();
                playermovey = loadmap.getPlayery();
                loadmap.getTer().renderFrame(loadmap.getworld());

                char direction = inputSource.getNextKey();
                while (direction != ':') {
                    header(loadmap.getworld());
                    move(direction, loadmap.getworld());
                    loadmap.getTer().renderFrame(loadmap.getworld());

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

    private void load() {
        StdDraw.clear(StdDraw.BLACK);
        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.text(40, 27, "CS61B: THE GAME");
        StdDraw.text(40, 23, "NEW GAME(N)");
        StdDraw.text(40, 18, "LOAD GAME(L)");
        StdDraw.text(40, 14, "QUIT(Q)");
        StdDraw.show();
    }

    private void whileloop() {
        while (!StdDraw.hasNextKeyTyped()) {
            if ((int) StdDraw.mouseX() < loadmap.getMapwidth() && (int) StdDraw.mouseX() > -1
                    && (int) StdDraw.mouseY() < loadmap.getMapheight() && (int) StdDraw.mouseY() > -1) {
                if (! loadworld[mousex][mousey].equals(loadworld[(int) StdDraw.mouseX()][(int) StdDraw.mouseY()])) {
                    loadmap.getTer().renderFrame(loadmap.getworld()); //在下一次按键之前 移动鼠标显示信息
                    header(loadmap.getworld());
                }
            }
        }
    }

    private void header(TETile[][] twod) {
        StdDraw.disableDoubleBuffering();
        mousex = (int) StdDraw.mouseX();
        mousey = (int) StdDraw.mouseY();
        if (mousex < loadmap.getMapwidth() && mousey < loadmap.getMapheight()) {
            StdDraw.setPenColor(Color.WHITE);
            StdDraw.text(40.0, 38.0, loadmap.getworld()[mousex][mousey].description());
        }
        StdDraw.enableDoubleBuffering();
    }




    public void moveup(int playerx, int playery, TETile[][] world) {
        if (world[playerx][playery + 1].equals(Tileset.WALL)) {
            return;
        } else {
            world[playerx][playery + 1] = Tileset.FLOWER;
            world[playerx][playery] = Tileset.FLOOR;
            playermovey += 1;

        }
    }

    public void movedown(int playerx, int playery, TETile[][] world) {
        if (world[playerx][playery - 1].equals(Tileset.WALL)) {
            return;
        } else if (!world[playerx][playery - 1].equals(Tileset.WALL)){
            world[playerx][playery - 1] = Tileset.FLOWER;
            world[playerx][playery] = Tileset.FLOOR;
            playermovey -= 1;
        }
    }

    public void moveleft(int playerx, int playery, TETile[][] world) {
        if (world[playerx - 1][playery].equals(Tileset.WALL)) {
            return;
        } else {
            world[playerx - 1][playery] = Tileset.FLOWER;
            world[playerx][playery] = Tileset.FLOOR;
            playermovex -= 1;
        }
    }

    public void moveright(int playerx, int playery, TETile[][] world) {
        if (world[playerx + 1][playery].equals(Tileset.WALL)) {
            return;
        } else if (!world[playerx + 1][playery].equals(Tileset.WALL)) {
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
        header(loadmap.getworld());
    }


    private static void saveMap(MapGenerator map) {
        File f = new File("./save_map");
        try {
            if (!f.exists()) {
                f.createNewFile();
            }
            FileOutputStream fs = new FileOutputStream(f);
            ObjectOutputStream os = new ObjectOutputStream(fs);
            os.writeObject(map);
        }  catch (FileNotFoundException e) {
            System.out.println("file not found");
            System.exit(0);
        } catch (IOException e) {
            System.out.println(e);
            System.exit(0);
        }
    }




    private static MapGenerator loadMap() {
        File f = new File("./save_map");
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
                world[x][y].draw(x , y);
            }
        }
    }


    public static void main(String[] args) {
        StartInput input = new StartInput();
    }


}
