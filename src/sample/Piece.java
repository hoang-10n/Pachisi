package sample;

public class Piece {
    private int[] spawnPos = new int[] {1, 43, 15, 29};

    private String name;
    private int pos;
    private int distanceFromHome;

    Piece(String name, int pos, int distanceFromHome) {
        this.name = name;
        this.pos = pos;
        this.distanceFromHome = distanceFromHome;
    }

    public boolean isHome() {
        return distanceFromHome == 0;
    }

    public void setDistanceFromHome(int pos, int c) {
        distanceFromHome = (pos >= spawnPos[c] || pos == 0) ? (55 + spawnPos[c] - pos) : (spawnPos[c] - pos - 1);
    }

    public int getDistanceFromHome() {
        return distanceFromHome;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public int getPos() {
        return pos;
    }

    public String getName() {
        return name;
    }
}
