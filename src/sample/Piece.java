package sample;

import java.awt.*;

public class Piece {
    private char color;
    private int id;
    private int pos;
    private int distanceFromHome;
    private Image image;

    Piece(char color, int id, int pos, int distanceFromHome) {
        this.color = color;
        this.id = id;
        this.pos = pos;
        this.distanceFromHome = distanceFromHome;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public boolean isHome() {
        return distanceFromHome == 0;
    }

    /**
     * @param pos
     * @param color
     */
    public void setDistanceFromHome(int pos, char color) {
        int temp = Constants.COLOR.indexOf(color);
        distanceFromHome = (pos >= Constants.SPAWN_POS[temp] || pos == 0) ? (55 + Constants.SPAWN_POS[temp] - pos) : (Constants.SPAWN_POS[temp] - pos - 1);
    }

    public int getDistanceFromHome() {
        return distanceFromHome;
    }

    public char getColor() {
        return color;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public int getId() {
        return id;
    }
}
