package sample;

import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;

public class Controller {
    private static int[] cur = new int[]{-1, -1};
    char turnColor;
    @FXML
    ImageView dice1, dice2, b0, b1, b2, b3, r0, r1, r2, r3, y0, y1, y2, y3, g0, g1, g2, g3, turnIndicator, move1, move2, move3;
    MoveWrapper[] moveWrap = new MoveWrapper[3];
    private Image[] dice = new Image[6];
    @FXML
    Button roll;
    private boolean isHome;
    private ImageView old = null;
    private ImageView[] piece;
    private int[] possibleMove = new int[3];
    private Player player = new Player();
    private Piece temp;
    private boolean firstTurn = true;
    /**
     * how many dice throw a player has left. This turns into 2 when a player gets the same value for 2 dice
     */
    private int turnRemaining = 1;

    public static int[] getCur() {
        return cur;
    }

    @FXML
    void initialize() {
        for (int i = 0; i < 6; i++) {
            dice[i] = new Image("dice//dice" + (i + 1) + ".png");
        }
        moveWrap[0] = new MoveWrapper(move1);
        moveWrap[1] = new MoveWrapper(move2);
        moveWrap[2] = new MoveWrapper(move3);
        piece = new ImageView[]{b0, b1, b2, b3, r0, r1, r2, r3, y0, y1, y2, y3, g0, g1, g2, g3};
        player.initialize();
    }

    @FXML
    void skip() {
        // when skip button is pressed, remove all dice on the table
        removeDieWithNum(-1);
        turnRemaining = 1;
        roll.setDisable(false);
        cur = new int[]{-1, -1};
        if (!firstTurn) {
            turnColor = Constants.COLOR.get(Constants.COLOR.indexOf(turnColor) == 3 ? 0 : Constants.COLOR.indexOf(turnColor) + 1);
            setTurnIndicator(turnColor);
        }
    }

    @FXML
    void roll() {
        if (roll.isDisable() || turnRemaining == 0) {
            return;
        }
        roll.setDisable(true);
        cur = player.rollDice();
        // if game is starting, default first value to 6 so there is no need to wait for this value
        if (firstTurn) {
            cur[0] = 6;
        }
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
        if (cur[0] == cur[1]) {
            turnRemaining = 2;
        }
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
        moveWrap[0].setVisible(false);
        moveWrap[1].setVisible(false);
        moveWrap[2].setVisible(false);
        moveWrap[0].toFront();
        moveWrap[1].toFront();
        moveWrap[2].toFront();
        if (old != null) {
            old.setEffect(new Glow(0));
        }
        old = null;
    }

    /**
     * this method takes in the ImageView of the move, and turn the corresponding die value to -1, also remove the corresponding die from the table
     *
     * @param move the ImageView of the move in progress
     */
    void removeDie(ImageView move) {
        for (MoveWrapper moveWrapper : moveWrap) {
            if (move.equals(moveWrapper.getMove())) {
                int loc = moveWrapper.getLocation();
                if (temp.getPos() == 0) {
                    removeDieWithNum(6);
                    cur[0] = cur[1];
                    cur[1] = -1;
                    return;
                }
                for (int j = 0; j < cur.length; j++) {
                    int dieValue = loc - temp.getPos();
                    if (cur[j] == dieValue) {
                        removeDieWithNum(dieValue);
                        //do not let cur[0] be -1 if cur[1] != -1. Keeps the sorted order
                        if (j == 0) {
                            cur[0] = cur[1];
                            cur[1] = -1;
                        } else
                            cur[j] = -1;
                        return;
                    }
                    // if location the ImageView of the move doesn't match any of the dice's value, the player is
                    // jumping 2 die values at the same time
                    cur[0] = -1;
                    cur[1] = -1;
                    removeDieWithNum(-1);
                }
                return;
            }
        }
    }

    /**
     * this method removes the image of a die with number in parameter from the table. If num is -1, remove all dice
     *
     * @param num num of die to remove
     */
    void removeDieWithNum(int num) {
        if (num == -1) {
            dice1.setImage(null);
            dice2.setImage(null);
            return;
        }
        // get the url of the first die's image
        if (dice1.getImage() != null) {
            String url = dice1.getImage().getUrl();
            // use regex to strip the url, only the number will remain
            url = url.replaceAll(".*(\\d).png$", "$1");
            if (url.charAt(0) - '0' == num) {
                dice1.setImage(null);
                return;
            }
        }
        dice2.setImage(null);
    }

    @FXML
    void moveChess(MouseEvent event) {
        // decrement turnRemaining first. Then check if there is still die value remaining after removing die
        if (turnRemaining > 0) {
            turnRemaining--;
        }
        ImageView tmp = (ImageView) event.getSource();
        removeDie(tmp);
        // if there is die value remaining, increment turnRemaining so the player can still move
        for (int i : cur) {
            if (i != -1) {
                turnRemaining++;
                break;
            }
        }
        double x = tmp.getLayoutX();
        double y = tmp.getLayoutY();
        if (old != null) {
            transition(old, x, y);
            if (firstTurn) {
                firstTurn = false;
                turnColor = old.getId().charAt(0);
            } else {
                // change the color of the horse in turn when turn is over
                if (turnRemaining == 0) {
                    skip();
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
        // if there is still turn after the move even though no dice value is remaining, this is a double move instance
        // let the player roll the die
        if (turnRemaining > 0 && cur[0] == -1 && cur[1] == -1) {
            roll.setDisable(false);
        }
    }

    @FXML
    void setTurnIndicator(char turnColor) {
        turnIndicator.setImage(new Image("dice//" + turnColor + ".png"));
    }

    @FXML
    void choose(MouseEvent event) {
        moveWrap[0].setVisible(false);
        moveWrap[1].setVisible(false);
        moveWrap[2].setVisible(false);
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

    private void show(MoveWrapper i, int p) {
        if (p == -1) { //TODO what is this?
//             i.setVisible(false);
        } else {
            i.setVisible(true);
            i.setLayoutX(Constants.POS[0][p - 1]);
            i.setLayoutY(Constants.POS[1][p - 1]);
        }
    }

    private void show(MoveWrapper i, double x, double y) {
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
                if (cur[1] > current) show(moveWrap[1], 352, y - 48 * (cur[1] - current) * (t == 3 ? 1 : -1));
                if (cur[0] > current && player.checkHome(cur[0], t, k))
                    show(moveWrap[0], 352, y - 48 * (cur[0] - current) * (t == 3 ? 1 : -1));
            } else {
                current = (int) (Math.abs(x - Constants.HOME[t]) / 48);
                if (cur[1] > current) show(moveWrap[1], x - 48 * (cur[1] - current) * (t == 1 ? 1 : -1), 317);
                if (cur[0] > current && player.checkHome(cur[0], t, k))
                    show(moveWrap[0], x - 48 * (cur[0] - current) * (t == 1 ? 1 : -1), 317);
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
                    moveWrap[0].setLocation(possibleMove[0]);
                    moveWrap[1].setLocation(possibleMove[1]);
                    moveWrap[2].setLocation(possibleMove[2]);
                    if (possibleMove[0] != -1)
                        show(moveWrap[0], possibleMove[0]);
                    if (possibleMove[1] != -1)
                        show(moveWrap[1], possibleMove[1]);
                    if (possibleMove[2] != -1)
                        show(moveWrap[2], possibleMove[2]);
                }
            } else {
                possibleMove = new int[]{cur[0], cur[1], -1};
                showHomeMove(i);
            }
        }
    }
}