package sample;

import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;

public class Controller {
    private Image[] dice = new Image[6];
    private boolean isHome;
    private ImageView old = null;
    private ImageView[] piece;
    private int[] cur = new int[]{6,5}, possibleMove = new int[3], home = new int[]{-36,705,-1,670};
    private Player player = new Player();
    private Piece temp;

    private int[][] pos = new int[][]{{287, 287, 287, 287, 287, 287, 287, 239, 191, 143, 95, 47, -1, -1, -1, 47, 95, 143, 191, 239, 287, 287, 287, 287, 287, 287, 287, 352, 417, 417, 417, 417, 417, 417, 417, 465, 513, 561, 609, 657, 705, 705, 705, 657, 609, 561, 513, 465, 417, 417, 417, 417, 417, 417, 417, 352},
                                        {-36, 12, 60, 108, 156, 204, 252, 252, 252, 252, 252, 252, 252, 317, 382, 382, 382, 382, 382, 382, 382, 430, 478, 526, 574, 622, 670, 670, 670, 622, 574, 526, 478, 430, 382, 382, 382, 382, 382, 382, 382, 317, 252, 252, 252, 252, 252, 252, 252, 204, 156, 108, 60, 12, -36, -36}};
    @FXML
    ImageView dice1, dice2, a0, a1, a2, a3, b0, b1, b2, b3, c0, c1, c2, c3, d0, d1, d2, d3, move1, move2, move3;

    @FXML
    void initialize() {
        for (int i = 0; i < 6; i++) {
            dice[i] = new Image("dice//dice" + (i + 1) + ".jpg");
        }
        piece = new ImageView[] {a0, a1, a2, a3, b0, b1, b2, b3, c0, c1, c2, c3, d0, d1, d2, d3};
        player.initialize();
    }

    @FXML
    void role() {
        cur = player.roleDice();
        dice1.setImage(dice[cur[0] - 1]);
//        dice1.setLayoutX(r.nextInt(151) + 300);
//        dice1.setLayoutY(r.nextInt(151) + 300);
//        dice1.setRotate(r.nextInt(361) - 180);
        dice2.setImage(dice[cur[1] - 1]);
//        dice2.setLayoutX(r.nextInt(151) + 300);
//        dice2.setLayoutY(r.nextInt(151) + 300);
//        dice2.setRotate(r.nextInt(361) - 180);
        if (old != null) old.setEffect(new Glow(0));
        old = null;
    }

    ImageView getPiece(String name) {
        for (ImageView i : piece) {
            if (i.getId().equals(name)) return i;
        }
        return null;
    }


    void transition(ImageView image, double x, double y) {
        TranslateTransition transition = new TranslateTransition(Duration.seconds(1), image); //@@
        transition.setToX(x - image.getX());
        transition.setToY(y - image.getY());
        transition.play();
    }

    void kickChess(Object[] kickedPiece) {
        Piece chess = player.getPiece((String) kickedPiece[2]);
        ImageView chessImage = getPiece((String) kickedPiece[2]);
        chess.setPos(0);
        chess.setDistanceFromHome(0, chess.getName().charAt(0) - 'a');
        double x = (int) kickedPiece[0], y = (int) kickedPiece[1];
        transition(chessImage, x, y);
    }

    private void animate () {
        old.toFront();
        move1.setVisible(false);
        move2.setVisible(false);
        move3.setVisible(false);
        move1.toFront();
        move2.toFront();
        move3.toFront();
        if (old != null) {
            old.setEffect(new Glow(0));
        }
        old = null;
    }

    @FXML
    void moveChess(MouseEvent event) {
        ImageView tmp = (ImageView) event.getSource();
        double x = tmp.getLayoutX();
        double y = tmp.getLayoutY();
        if (old != null) {
            transition(old, x, y);
            int c = tmp.getId().charAt(4) - '1';
            if (!isHome) {
                Object[] kick = player.kickable(possibleMove[c]); //@@
                if (kick != null) kickChess(kick);
                temp.setPos(possibleMove[c]);
                temp.setDistanceFromHome(possibleMove[c], old.getId().charAt(0) - 'a');
            } else {
                temp.setPos(-1);
                player.updateHome(cur[c], temp.getName());
            }
            animate();
        }
    }

    @FXML
    void choose(MouseEvent event) {
//        move1.setVisible(false);
//        move2.setVisible(false);
//        move3.setVisible(false);
        ImageView temp = (ImageView) event.getSource();
        temp.setEffect(new Glow(0.5));
        showPossibleMove(temp);
        if (old != null && old != temp) {
            old.setEffect(new Glow(0));
        }
        old = temp;
    }

    private void show(ImageView i, int p) {
         if (p == -1) { //@@
//             i.setVisible(false);
         } else {
             i.setVisible(true);
             i.setLayoutX(pos[0][p - 1]);
             i.setLayoutY(pos[1][p - 1]);
         }
    }

    private void show(ImageView i, double x, double y) {
        i.setVisible(true);
        i.setLayoutX(x);
        i.setLayoutY(y);
    }

    private void showHomeMove(ImageView temp) {
        int t = temp.getId().charAt(0) -'a';
        int k = temp.getId().charAt(1) -'0';
        double x = temp.getTranslateX() + temp.getX();
        double y = temp.getTranslateY() + temp.getY();
        int current;
        if (player.checkHome(cur[1], t, k)) {
            if (t == 0 || t == 3) {
                current = (int) (Math.abs(y - home[t]) / 48);
                if (cur[1] > current) show(move2, 352, y - 48 * (cur[1] - current) * (t == 3 ? 1 : -1));
                if (cur[0] > current && player.checkHome(cur[0], t, k))
                    show(move1, 352, y - 48 * (cur[0] - current) * (t == 3 ? 1 : -1));
            } else {
                current = (int) (Math.abs(x - home[t]) / 48);
                if (cur[1] > current) show(move2, x - 48 * (cur[1] - current) * (t == 1 ? 1 : -1), 317);
                if (cur[0] > current && player.checkHome(cur[0], t, k))
                    show(move1, x - 48 * (cur[0] - current) * (t == 1 ? 1 : -1), 317);
            }
        }
    }

    private void showPossibleMove(ImageView i) {
        temp = player.getPiece(i.getId());
        isHome = temp.isHome();
        if (temp == null) System.out.println("Cannot find piece.");
        else {
            if (!temp.isHome()) {
                possibleMove = player.returnPossibleMove(temp);
                show(move1, possibleMove[0]);
                show(move2, possibleMove[1]);
                show(move3, possibleMove[2]);
            } else {
                possibleMove = new int[]{cur[0], cur[1], -1};
                showHomeMove(i);
            }
        }
    }
}
