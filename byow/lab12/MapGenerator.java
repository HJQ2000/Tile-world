package byow.lab12;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

public class MapGenerator {

    // initialize the tile rendering engine with a window of size WIDTH x HEIGHT
    TERenderer ter = new TERenderer();
    int width;
    int height;
    TETile[][] world;

    public MapGenerator(int w, int h) {
        this.width = w;
        this.height = h;
        ter.initialize(width, height);
        world = new TETile[width][height];
        // initialize tiles
        for (int x = 0; x < width; x += 1) {
            for (int y = 0; y < height; y += 1) {
                world[x][y] = Tileset.NOTHING;
            }
        }
    }

    public void rectangleRoom(int x1, int x2, int y1, int y2){
        // fills in a block 14 tiles wide by 4 tiles tall
        for (int x = x1; x < x2; x += 1) {
            for (int y = y1; y < y2; y += 1) {
                world[x][y] = Tileset.WALL;
            }
        }
        //fills in the inner room
        for (int x = x1+1; x < x2-1; x += 1) {
            for (int y = y1+1; y < y2-1; y += 1) {
                world[x][y] = Tileset.FLOOR;
            }
        }
    }


    public void draw() {
        // draws the world to the screen
        ter.renderFrame(world);
    }


}


