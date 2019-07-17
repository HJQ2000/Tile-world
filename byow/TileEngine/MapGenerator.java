package byow.TileEngine;


import edu.princeton.cs.introcs.StdDraw;

import java.awt.*;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Random;

public class MapGenerator implements Serializable {
    //private static final long serialVersionUID = 6529685098267757690L;
    private TERenderer ter;
    private int mapwidth;
    private int mapheight;
    private Random random;
    private int size;
    private TETile[][] world;
    private int ulx;
    private int uly;
    private int lrx;
    private int lry;
    private int roomNum = 0;
    private Rectangle playerInitialRoom;
    private int playerx;
    private int playery;
    private UnionFind rooms;
    private HashMap<Integer, Rectangle> recmap;

    public MapGenerator(Long seed) {
        ter = new TERenderer();
        mapwidth = 80;
        mapheight = 40;
        random = new Random(seed);
        ter.initialize(mapwidth, mapheight + 1);
        size = 10;
        world = new TETile[mapwidth][mapheight + 1];
        recmap = new HashMap<>();
        for (int i = 0; i < mapwidth; i++) {
            for (int j = 0; j < mapheight + 1; j++) {
                world[i][j] = Tileset.NOTHING;
            }
        }
        ulx = random.nextInt(60) + 10;
        lry = random.nextInt(20) + 10;
        int temp = random.nextInt(6) + 3;
        int temp2 = random.nextInt(6) + 3;
        lrx = ulx + temp;
        uly = lry + temp2;
        Rectangle rec0 = new Rectangle(ulx, uly, lrx, lry, world);
        recmap.put(roomNum, rec0);
        roomNum += 1;


        for (int i = 0; i < 25; i++) {
            ulx = random.nextInt(80) + 2;
            lry = random.nextInt(40) + 2;
            temp = random.nextInt(3) + 3;
            temp2 = random.nextInt(3) + 3;
            if (ulx + temp < 78 && lry + temp2 < 38) {
                lrx = ulx + temp;
                uly = lry + temp2;
                if (!checkoverlap(ulx, lry, lrx, uly)) {
                    Rectangle rec1 = new Rectangle(ulx, uly, lrx, lry, world);
                    recmap.put(roomNum, rec1);
                    int index = random.nextInt(roomNum);
                    roomNum += 1;
                    rec0 = recmap.get(index);
                    connect(rec0, rec1);
                }
            }
        }
        for (int i = 1; i < mapwidth - 1; i++) {
            for (int j = 1; j < mapheight - 1; j++) {
                if (world[i][j].equals(Tileset.NOTHING)) {
                    if (buildwalls(i, j)) {
                        world[i][j] = Tileset.WALL;
                    }
                }
            }
        }

        playerInitialRoom = recmap.get(random.nextInt(roomNum));
        playerx = random.nextInt(1 + playerInitialRoom.getLrx()
                - playerInitialRoom.getUlx()) + playerInitialRoom.getUlx();
        playery = random.nextInt(1 + playerInitialRoom.getUly()
                - playerInitialRoom.getLry()) + playerInitialRoom.getLry();
        world[playerx][playery] = Tileset.FLOWER;

        for (int i = 0; i < 6; i++) {
            gengerateEncounter();
        }


        ter.renderFrame(world);

    }

    private void gengerateEncounter() {
        Rectangle encounterRoom = recmap.get(random.nextInt(roomNum));
        int encounterx = random.nextInt(1 + encounterRoom.getLrx()
                - encounterRoom.getUlx()) + encounterRoom.getUlx();
        int encountery = random.nextInt(1 + encounterRoom.getUly()
                - encounterRoom.getLry()) + encounterRoom.getLry();
        if (encounterx != playerx || encountery != playery) {
            world[encounterx][encountery] = Tileset.LOCKED_DOOR;
        }
    }

    public static void main(String[] args) {
        MapGenerator map = new MapGenerator((long) 761239);
    }

    public int getPlayerx() {
        return playerx;
    }

    public void updatePlayerx(int x) {
        playerx = x;
    }

    public void updatePlayery(int y) {
        playery = y;
    }

    public int getPlayery() {
        return playery;
    }

    public TETile[][] getworld() {
        return world;
    }

    public TERenderer getTer() {
        return ter;
    }

    public int getMapwidth() {
        return mapwidth;
    }

    public int getMapheight() {
        return mapheight;
    }

    public Random getRandom() { return random; }

    private boolean buildwalls(int x, int y) {
        for (int i = x - 1; i <= x + 1; i++) {
            for (int j = y - 1; j <= y + 1; j++) {
                if (world[i][j].equals(Tileset.FLOOR)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean checkoverlap(int x1, int y1, int x2, int y2) {
        for (int i = x1; i < x2; i++) {
            for (int j = y1; j < y2; j++) {
                if (!world[i][j].equals(Tileset.NOTHING)
                        && (world[i + 1][j].equals(Tileset.FLOOR)
                        || world[i - 1][j].equals(Tileset.FLOOR))
                        && (world[i][j + 1].equals(Tileset.FLOOR))
                        || world[i][j - 1].equals(Tileset.FLOOR)) {
                    return true;
                }
            }
        }
        return false;
    }




    /* public boolean straightconnect(int startx, int starty, int endx, int endy) {

        if (rec1.getLrx() < rec2.getUlx() && rec1.getLry() > rec2.getUly()) {
            return false;
        }
        if (rec1.getUlx() > rec2.getLrx() && rec1.getUly() < rec2.getLry()) {
            return false;
        }
        if (rec1.getUlx() > rec2.getLrx() && rec1.getLry() > rec2.getUly()) {
            return false;
        }
        if (rec1.getLrx() < rec2.getUlx() && rec1.getUly() < rec2.getLry()) {
            return false;
        }
        return true; */

    public void connect(Rectangle rec1, Rectangle rec2) {
        int startx = random.nextInt(rec1.getLrx() - rec1.getUlx() + 1) + rec1.getUlx();
        int starty = random.nextInt(rec1.getUly() - rec1.getLry() + 1) + rec1.getLry();
        int endx = random.nextInt(rec2.getLrx() - rec2.getUlx() + 1) + rec2.getUlx();
        int endy = random.nextInt(rec2.getUly() - rec2.getLry() + 1) + rec2.getLry();
        //turn right
        if (endx >= startx) {
            for (int i = startx; i <= endx; i++) {
                world[i][starty] = Tileset.FLOOR;
            }
            //turn up
            if (endy >= starty) {
                for (int i = starty; i <= endy; i++) {
                    world[endx][i] = Tileset.FLOOR;
                }
            } else {
                for (int i = starty; i >= endy; i--) {
                    world[endx][i] = Tileset.FLOOR;
                }
            }
        }

        //turn left
        if (endx < startx) {
            for (int i = endx; i <= startx; i++) {
                world[i][starty] = Tileset.FLOOR;
            }
            //turn up
            if (endy >= starty) {
                for (int i = starty; i <= endy; i++) {
                    world[endx][i] = Tileset.FLOOR;
                }
            } else {
                for (int i = starty; i >= endy; i--) {
                    world[endx][i] = Tileset.FLOOR;
                }
            }
        }

    }
}
