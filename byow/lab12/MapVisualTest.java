package byow.lab12;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;

public class MapVisualTest {
    public static void main(String[] args) {
        MapGenerator map = new MapGenerator(60, 30);
        map.rectangleRoom(14, 20, 10, 18);
        map.rectangleRoom(3, 10, 3, 8);
        map.draw();
    }

}
