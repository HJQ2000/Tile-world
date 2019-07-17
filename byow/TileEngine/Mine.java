package byow.TileEngine;

public class Mine {
    private int x;
    private int y;
    private int score;
    public Mine(int x, int y, int score) {
        this.x = x;
        this.y = y;
        this.score = score;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getScore() {
        return score;
    }

}
