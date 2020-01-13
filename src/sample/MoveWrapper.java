package sample;

import javafx.scene.image.ImageView;

/**
 * this wrapper class contains the image view of a move and also its location
 */
public class MoveWrapper {
    private ImageView move;
    private int location;

    MoveWrapper(ImageView move) {
        this.move = move;
    }

    public void setLocation(int location) {
        this.location = location;
    }

    public double getFitHeight() {
        return move.getFitHeight();
    }

    public double getFitWidth() {
        return move.getFitWidth();
    }

    void setVisible(boolean boo) {
        move.setVisible(boo);
    }

    void setLayoutX(double x) {
        move.setLayoutX(x);
    }

    void setLayoutY(double y) {
        move.setLayoutY(y);
    }

    void toFront() {
        move.toFront();
    }

    public ImageView getMove() {
        return move;
    }

    public int getLocation() {
        return location;
    }
}
