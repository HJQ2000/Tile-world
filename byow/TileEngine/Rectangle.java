package byow.TileEngine;


import java.io.Serializable;

public class Rectangle implements Serializable {
    private int ulx;
    private int uly;
    private int lrx;
    private int lry;
    private TETile[][] myworld;

    public Rectangle(int upperleftx, int upperllefty, int lowerightx, int lowerrighty,
                     TETile[][] world) {
        ulx = upperleftx;
        uly = upperllefty;
        lrx = lowerightx;
        lry = lowerrighty;
        myworld = world;
        for (int i = upperleftx; i <= lowerightx; i++) {
            for (int j = lowerrighty; j <= upperllefty; j++) {
                myworld[i][j] = Tileset.FLOOR;
            }
        }

    }

    public int getUlx() {
        return this.ulx;
    }
    public int getUly() {
        return this.uly;
    }
    public int getLrx() {
        return this.lrx;
    }
    public int getLry() {
        return this.lry;
    }


}
