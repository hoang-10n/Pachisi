package sample;


import javafx.scene.image.ImageView;

public class Piece {
    private ImageView imageView;
    private char color;
    private int id;
    private int pos;
    private int distanceFromHome;
    private final int spawnX;
    private final int spawnY;

    Piece(ImageView imageView, char color, int id, int pos, int distanceFromHome) {
        this.color = color;
        this.id = id;
        this.pos = pos;
        this.distanceFromHome = distanceFromHome;
        this.imageView = imageView;
        spawnX = (int) imageView.getX();
        spawnY = (int) imageView.getY();
    }

    Piece(char color, int pos) {
        this.color = color;
        distanceFromHome = 56;
        spawnX = 0;
        spawnY = 0;
        this.pos = pos;
    }

    int getStartingPoint() {
        return Constants.SPAWN_POS[Constants.COLOR.indexOf(color)];
    }

    public ImageView getImageView() {
        return imageView;
    }

        public int getSpawnX() {
        return spawnX;
    }

    public int getSpawnY() {
        return spawnY;
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
