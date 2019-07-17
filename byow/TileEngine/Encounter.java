package byow.TileEngine;

import edu.princeton.cs.introcs.StdDraw;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class Encounter {

    private TETile[][] encounterWorld;
    private int mapwidth;
    private int mapheight;
    private TERenderer ter;
    private Random rrr;
    private int playerx, playery;
    private Mine fourscore, sixscore, eightscore, death;
    private int fourx, foury, sixx, sixy, eightx, eighty;

    public Encounter(TETile[][] world, Random random) {

        ter = new TERenderer();
        rrr = random;
        mapwidth = 80;
        mapheight = 40;
        ter.initialize(mapwidth, mapheight + 1);
        for (int i = 0; i < mapwidth; i++) {
            for (int j = 0; j < mapheight; j++) {
                world[i][j] = Tileset.NOTHING;
            }
        }

        encounterWorld = world;
        Rectangle encounterRoom = new Rectangle(20, 30, 60, 10, encounterWorld);
        for (int i = 21; i < 60; i++) {
            for (int j = 11; j < 30; j++) {
                world[i][j] = Tileset.FLOOR;
                int randomNum = random.nextInt(1000);
                if (randomNum < 300) {
                    world[i][j] = Tileset.WALL;
                }
            }
        }
        for (int i = 19; i < 62; i++) {
            world[i][9] = Tileset.WALL;
        }
        for (int i = 19; i < 62; i++) {
            world[i][31] = Tileset.WALL;
        }
        for (int i = 10; i < 31; i++) {
            world[19][i] = Tileset.WALL;
        }
        for (int i = 10; i < 31; i++) {
            world[61][i] = Tileset.WALL;
        }

        fourscore = generatehidden(4);
        sixscore = generatehidden(6);
        eightscore = generatehidden(8);
        death = generatedeath();

        playerx = random.nextInt(41) + 20;
        playery = random.nextInt(21) + 10;
        while (encounterWorld[playerx][playery].equals(Tileset.WALL)
                || (encounterWorld[playerx + 1][playery].equals(Tileset.WALL)
                && encounterWorld[playerx - 1][playery].equals(Tileset.WALL)
                && encounterWorld[playerx][playery + 1].equals(Tileset.WALL)
                && encounterWorld[playerx][playery - 1].equals(Tileset.WALL))
                || encounterWorld[playerx][playery].equals(Tileset.UNKNOWN)) {
            playerx = random.nextInt(41) + 20;
            playery = random.nextInt(21) + 10;
        }

        world[playerx][playery] = Tileset.FLOWER;


        /* int minenum = 0;
        ArrayList<Integer> xiaodu = new ArrayList<>();
        xiaodu.add(4);
        xiaodu.add(6);
        xiaodu.add(8);
         while (minenum < 4) {
            int x = rrr.nextInt(41) + 20;
            int y = rrr.nextInt(21) + 10;
            if (!encounterWorld[x][y].equals(Tileset.WALL)) {
                encounterWorld[x][y] = Tileset.MOUNTAIN;
                fourscore = new Mine(x, y, xiaodu.get(minenum));
                minenum += 1;
            }
        } */
    }

    private Mine generatehidden(int score) {
        int x = rrr.nextInt(41) + 20;
        int y = rrr.nextInt(21) + 10;
        if (encounterWorld[x][y].equals(Tileset.WALL)) {
            generatehidden(score);
        } else {
            encounterWorld[x][y] = Tileset.UNKNOWN;
        }
        return new Mine(x, y, score);
    }

    private Mine generatedeath() {
        int x = rrr.nextInt(41) + 20;
        int y = rrr.nextInt(21) + 10;
        if (encounterWorld[x][y].equals(Tileset.WALL)) {
            generatedeath();
        } else {
            encounterWorld[x][y] = Tileset.UNKNOWN;
        }
        return new Mine(x, y, 0);
    }

    private TETile[][] getEncounterWorld() {
        return encounterWorld;
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

    public Mine getFourscore() {
        return fourscore;
    }

    public Mine getSixscore() {
        return sixscore;
    }

    public Mine getEightscore() {
        return eightscore;
    }

    public Mine getDeath() {
        return death;
    }

    public int getMapwidth() {
        return mapwidth;
    }

    public int getMapheight() {
        return mapheight;
    }

    public TETile[][] getworld() {
        return encounterWorld;
    }

}
