package sample;

import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;

public class Controller {
    private static int[] cur = new int[2];
    char turnColor;
    @FXML
    ImageView dice1, dice2, b0, b1, b2, b3, r0, r1, r2, r3, y0, y1, y2, y3, g0, g1, g2, g3, turnIndicator, move1, move2, move3;
    private Image[] dice = new Image[6];
    private boolean isHome;
    private ImageView old = null;
    private ImageView[] piece;
    private int[] possibleMove = new int[3];
    private Player player = new Player();
    private Piece temp;
    private boolean turnOver = false;
    private boolean firstTurn = true;

    public static int[] getCur() {
        return cur;
    }

    @FXML
    void initialize() {
        for (int i = 0; i < 6; i++) {
            dice[i] = new Image("dice//dice" + (i + 1) + ".png");
        }
        piece = new ImageView[]{b0, b1, b2, b3, r0, r1, r2, r3, y0, y1, y2, y3, g0, g1, g2, g3};
        player.initialize();
    }

    @FXML
    void roll() {
        cur = player.rollDice();
        dice1.setImage(dice[cur[0] - 1]);
        //TODO what is this?
//        dice1.setLayoutX(r.nextInt(151) + 300);
//        dice1.setLayoutY(r.nextInt(151) + 300);
//        dice1.setRotate(r.nextInt(361) - 180);
        dice2.setImage(dice[cur[1] - 1]);
//        dice2.setLayoutX(r.nextInt(151) + 300);
//        dice2.setLayoutY(r.nextInt(151) + 300);
//        dice2.setRotate(r.nextInt(361) - 180);
        if (old != null) old.setEffect(new Glow(0));
        old = null;
        // swapping here so that the dice are not unnaturally sorted on the board
        int swap;
        if (cur[1] > cur[0]) {
            swap = cur[1];
            cur[1] = cur[0];
            cur[0] = swap;
        }
    }

    ImageView getPiece(String name) {
        for (ImageView i : piece) {
            if (i.getId().equals(name)) return i;
        }
        return null;
    }


    void transition(ImageView image, double x, double y) {
        //I'm changing this to 0.3 second because it was slow af
        TranslateTransition transition = new TranslateTransition(Duration.seconds(0.3), image);
        transition.setToX(x - image.getX());
        transition.setToY(y - image.getY());
        transition.play();
    }

    void kickChess(Object[] kickedPiece) {
        Piece chess = player.getPiece((char) kickedPiece[2], (int) kickedPiece[3]);
        ImageView chessImage = getPiece(kickedPiece[2] + "" + kickedPiece[3]);
        chess.setPos(0);
        chess.setDistanceFromHome(0, chess.getColor());
        double x = (int) kickedPiece[0], y = (int) kickedPiece[1];
        transition(chessImage, x, y);
    }

    private void animate() {
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
        if (Controller.cur[0] != Controller.cur[1]) {
            turnOver = !turnOver;
        }
        if (old != null) {
            transition(old, x, y);
            if (firstTurn) {
                firstTurn = false;
                turnColor = old.getId().charAt(0);
            } else {
                // change the color of the horse in turn when turn is over
                if (turnOver) {
                    turnColor = Constants.COLOR.get(Constants.COLOR.indexOf(turnColor) == 3 ? 0 : Constants.COLOR.indexOf(turnColor) + 1);
                }
            }
            setTurnIndicator(turnColor);
            int c = tmp.getId().charAt(4) - '1';
            if (!isHome) {
                Object[] kick = player.canBeKicked(possibleMove[c]); //TODO
                if (kick != null) kickChess(kick);
                temp.setPos(possibleMove[c]);
                temp.setDistanceFromHome(possibleMove[c], old.getId().charAt(0));
            } else {
                temp.setPos(-1);
                player.updateHome(cur[c], temp.getColor(), temp.getId());
                cur[c] = -1;
            }
            animate();
        }
    }

    @FXML
    void setTurnIndicator(char turnColor) {
        turnIndicator.setImage(new Image("dice//" + turnColor + ".png"));
    }

    @FXML
    void choose(MouseEvent event) {
//        move1.setVisible(false);
//        move2.setVisible(false);
//        move3.setVisible(false);
        ImageView temp = (ImageView) event.getSource();
        // if it's not first turn, don't let the player choose the illegal piece with wrong color
        if (!firstTurn) {
            if (temp.getId().charAt(0) != turnColor) {
                return;
            }
        }
        temp.setEffect(new Glow(0.5));
        showPossibleMove(temp);
        if (old != null && old != temp) {
            old.setEffect(new Glow(0));
        }
        old = temp;
    }

    private void show(ImageView i, int p) {
        if (p == -1) { //TODO what is this?
//             i.setVisible(false);
        } else {
            i.setVisible(true);
            i.setLayoutX(Constants.POS[0][p - 1]);
            i.setLayoutY(Constants.POS[1][p - 1]);
        }
    }

    private void show(ImageView i, double x, double y) {
        i.setVisible(true);
        i.setLayoutX(x);
        i.setLayoutY(y);
    }

    private void showHomeMove(ImageView temp) {
        String tempId = temp.getId();
        int t = Constants.COLOR.indexOf(tempId.charAt(0));
        int k = temp.getId().charAt(1) - '0';
        double x = temp.getTranslateX() + temp.getX();
        double y = temp.getTranslateY() + temp.getY();
        int current;
        if (player.checkHome(cur[1], t, k)) {
            if (t == 0 || t == 3) {
                current = (int) (Math.abs(y - Constants.HOME[t]) / 48);
                if (cur[1] > current) show(move2, 352, y - 48 * (cur[1] - current) * (t == 3 ? 1 : -1));
                if (cur[0] > current && player.checkHome(cur[0], t, k))
                    show(move1, 352, y - 48 * (cur[0] - current) * (t == 3 ? 1 : -1));
            } else {
                current = (int) (Math.abs(x - Constants.HOME[t]) / 48);
                if (cur[1] > current) show(move2, x - 48 * (cur[1] - current) * (t == 1 ? 1 : -1), 317);
                if (cur[0] > current && player.checkHome(cur[0], t, k))
                    show(move1, x - 48 * (cur[0] - current) * (t == 1 ? 1 : -1), 317);
            }
        }
    }

    private void showPossibleMove(ImageView i) {
        String tempId = i.getId();
        temp = player.getPiece(tempId.charAt(0), tempId.charAt(1) - '0');
        isHome = temp.isHome();
        if (temp == null) System.out.println("Cannot find piece");
        else {
            if (!isHome) {
                possibleMove = player.returnPossibleMove(temp);
                if (possibleMove != null) {
                    if (possibleMove[0] != -1)
                        show(move1, possibleMove[0]);
                    if (possibleMove[1] != -1)
                        show(move2, possibleMove[1]);
                    if (possibleMove[2] != -1)
                        show(move3, possibleMove[2]);
                }
            } else {
                possibleMove = new int[]{cur[0], cur[1], -1};
                showHomeMove(i);
            }
        }
    }
}