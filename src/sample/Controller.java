package sample;

import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

import java.util.Random;

public class Controller {
    private static int[] cur = new int[]{-1, -1};
    char turnColor;
    @FXML
    ImageView die1, die2, b0, b1, b2, b3, r0, r1, r2, r3, y0, y1, y2, y3, g0, g1, g2, g3, turnIndicator, move1, move2, move3;
    @FXML
    Circle c01, c02, c03, c04, c05, c06, c07, c08, c09, c10, c11, c12, c13, c14, c15, c16, c17, c18, c19, c20, c21, c22, c23, c24, c25, c26, c27, c28, c29, c30, c31, c32, c33, c34, c35, c36, c37, c38, c39, c40, c41, c42, c43, c44, c45, c46, c47, c48, c49, c50, c51, c52, c53, c54, c55, c56;
    @FXML
    Button roll, skip;
    Object[] die1WithValue = new Object[]{die1, 1};
    Object[] die2WithValue = new Object[]{die2, 1};
    MoveWrapper[] moveWrap = new MoveWrapper[3];
    private Image[] dice = new Image[6];
    private boolean isHome;
    private Circle[] circles;
    private ImageView old = null;
    private ImageView[] pieces;
    private int[] possibleMove = new int[3];
    private Player player = new Player();
    private Piece temp;
    private boolean firstTurn = true;
    private boolean diceFinished = true;
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
            dice[i] = new Image("sprites//dice" + (i + 1) + ".png");
        }
        circles = new Circle[]{
                c01, c02, c03, c04, c05, c06, c07, c08, c09, c10, c11, c12, c13, c14, c15, c16, c17, c18, c19, c20, c21, c22, c23, c24, c25, c26, c27, c28, c29, c30, c31, c32, c33, c34, c35, c36, c37, c38, c39, c40, c41, c42, c43, c44, c45, c46, c47, c48, c49, c50, c51, c52, c53, c54, c55, c56
        };
        die1.setImage(null);
        die2.setImage(null);
        skip.setDisable(true);
        moveWrap[0] = new MoveWrapper(move1);
        moveWrap[1] = new MoveWrapper(move2);
        moveWrap[2] = new MoveWrapper(move3);
        pieces = new ImageView[]{b0, b1, b2, b3, y0, y1, y2, g0, g1, g2, g3, y3, r0, r1, r2, r3};
        player.initialize(pieces);
    }

    @FXML
    void skip() {
        animate();
        skip.setDisable(true);
        if (!diceFinished) {
            return;
        }
        // when skip button is pressed, remove all dice on the table
        removeDieWithNum(-1);
        roll.setDisable(false);
        cur = new int[]{-1, -1};
        if (!firstTurn) {
            turnColor = Constants.COLOR.get(Constants.COLOR.indexOf(turnColor) == 3 ? 0 : Constants.COLOR.indexOf(turnColor) + 1);
            setTurnIndicator(turnColor);
        }
        turnRemaining = 1;
    }

    @FXML
    void roll() {
        skip.setDisable(true);
        if (!diceFinished) {
            return;
        }
        diceFinished = false;
        if (roll.isDisable() || turnRemaining == 0) {
            return;
        }
        roll.setDisable(true);
        cur = player.rollDice();

        Random r = new Random();
        // return to original location
        die1.setLayoutX(Constants.die1OriginalPos[0]);
        die1.setLayoutY(Constants.die1OriginalPos[1]);
        die2.setLayoutX(Constants.die2OriginalPos[0]);
        die2.setLayoutY(Constants.die2OriginalPos[1]);
        die1.toFront();
        die2.toFront();
//        die1.setRotate(r.nextInt(361) - 180);
//        die2.setLayoutX(r.nextInt(151) + 300);
//        die2.setLayoutY(r.nextInt(151) + 300);
//        die2.setRotate(r.nextInt(361) - 180);
        // Check for collision
//        while (die1.getBoundsInParent().intersects(die2.getBoundsInParent())) {
//            die2.setLayoutX(r.nextInt(151) + 300);
//            die2.setLayoutY(r.nextInt(151) + 300);
//            die2.setRotate(r.nextInt(361) - 180);
//        }
        // this thread animates the dice. Does not allow any other function while the dice are being animated
        new Thread(() -> {
            int sign1 = r.nextBoolean() ? 1 : -1;
            int sign2 = r.nextBoolean() ? 1 : -1;
            // ensure that 2 dice always move in 2 different directions by making the signs of 2 vectors always opposite to each other
            int randomVector1X = (r.nextInt(10) + 10) * sign1;
            int randomVector1Y = (r.nextInt(10) + 10) * sign2;
            int randomVector2X = (r.nextInt(10) + 10) * sign1 * (-1);
            int randomVector2Y = (r.nextInt(10) + 10) * sign2 * (-1);
            int[][] randomVector = {new int[]{randomVector1X, randomVector1Y}, new int[]{randomVector2X, randomVector2Y}}; // 2 random moving vector for the dice

            int friction = 1;// factor to slow down dice
            // die animating
            for (int i = 2; i < 200; i = 10 + i) {
                // these 2 blocks of codes reduce the vectors by factor of friction until the dice are no longer moving
                for (int j = 0; j < randomVector.length; j++) {
                    for (int k = 0; k < randomVector[0].length; k++) {
                        if (randomVector[j][k] != 0) {
                            if (Math.abs(randomVector[j][k]) <= friction) {
                                randomVector[j][k] = 0;
                            } else {
                                randomVector[j][k] -= friction * Integer.signum(randomVector[j][k]);
                            }
//                            if (randomVector[j][k] < 0) {
//                                randomVector[j][k] = 0;
//                            }
                        }
                    }
                }
                //move die 1
                die1.setLayoutX(die1.getLayoutX() + randomVector[0][0]);
                die1.setLayoutY(die1.getLayoutY() + randomVector[0][1]);
                //move die 2
                die2.setLayoutX(die2.getLayoutX() + randomVector[1][0]);
                die2.setLayoutY(die2.getLayoutY() + randomVector[1][1]);
                int rotateRate1 = r.nextInt(5) - 2;
                int rotateRate2 = r.nextInt(5) - 2;
                die1.setRotate(i + rotateRate1);
                die2.setRotate(i + rotateRate2);

                die1.setImage(dice[r.nextInt(6)]);
                die2.setImage(dice[r.nextInt(6)]);
                try {
                    Thread.sleep(i / 2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            diceFinished = true;
            die1.setImage(dice[cur[0] - 1]);
            die2.setImage(dice[cur[1] - 1]);
            die1WithValue[1] = cur[0];
            die2WithValue[1] = cur[1];
//            skip.setDisable(false);
            if (firstTurn) {
                if(!leaveNestPossible()) {
                    roll();
                    return;
                };// if the player cannot leave the nest in first turn, let them skip
            } else
            //check if player has any possible move. If they don't, let them skip
            {
                if (Player.thereIsPossibleMove(turnColor)) {
                    skip.setDisable(true);
                } else {
                    skip.setDisable(false);
                }
            }
        }).start();
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

    /**
     * check if any of the dice have a value that allows player to leave the nest
     *
     * @return true if the dice values allow player can leave nest, false if not
     */
    static boolean leaveNestPossible() {
        for (int i : cur) {
            for (int j : Constants.LEAVE_NEST_DIE) {
                if (i == j) {
                    return true;
                }
            }
        }
        return false;
    }


    void transition(ImageView image, double x, double y) {
        //I'm changing this to 0.3 second because it was slow af
        TranslateTransition transition = new TranslateTransition(Duration.seconds(0.3), image);
        transition.setToX(x - image.getX());
        transition.setToY(y - image.getY());
        transition.play();
    }

    void kickChess(Piece piece) {
        ImageView chessImage = piece.getImageView();
        piece.setPos(0);
        piece.setDistanceFromHome(0, piece.getColor());
        int x = piece.getSpawnX(), y = piece.getSpawnY();
        transition(chessImage, x, y);
    }

    private void animate() {
        moveWrap[0].setVisible(false);
        moveWrap[1].setVisible(false);
        moveWrap[2].setVisible(false);
        moveWrap[0].toFront();
        moveWrap[1].toFront();
        moveWrap[2].toFront();
        if (old != null) {
            old.toFront();
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
                    // if location of the move from nest is not starting point, then player is quick moving
                    if (loc != temp.getStartingPoint()) {
                        removeDieWithNum(-1);
                        cur[0] = -1;
                        cur[1] = -1;
                    }
                    removeDieWithNum(6);//TODO change this when allow dice of value 1 to let player leave nest
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
     * @param num value of die that needs removing
     */
    void removeDieWithNum(int num) {
        if (num == -1) {
            die1.setImage(null);
            die2.setImage(null);
            return;
        }
        if ((int) die1WithValue[1] == num) {
            die1.setImage(null);
            die1WithValue[1] = -1;
            return;
        }
        if ((int) die2WithValue[1] == num) {
            die2.setImage(null);
            die2WithValue[1] = -1;
        }
    }

    @FXML
    void moveChess(MouseEvent event) {
        if (cur[0] == cur[1]) {
            turnRemaining = 2;
        }
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
            }
            setTurnIndicator(turnColor);
            int c = tmp.getId().charAt(4) - '1';
            if (!isHome) {
                Piece pieceToBeKicked = player.canBeKicked(possibleMove[c]); //TODO
                if (pieceToBeKicked != null) kickChess(pieceToBeKicked);
                temp.setPos(possibleMove[c]);
                temp.setDistanceFromHome(possibleMove[c], old.getId().charAt(0));
            } else {
                temp.setPos(-1);
                player.updateHome(cur[c], temp.getColor(), temp.getId());
                cur[c] = -1;
            }
            animate();
        }
        // skip to next turn when turn is over
        if (turnRemaining == 0) {
            skip();
            return;
        }
        // if there is still turn after the move even though no dice value is remaining, this is a double move instance
        // make the player roll the die, don't let them skip
        if (turnRemaining > 0 && cur[0] == -1 && cur[1] == -1) {
            roll.setDisable(false);
            skip.setDisable(true);
        }
        // if there is no possible move after this move, let player skip
        if (!Player.thereIsPossibleMove(turnColor) && !(cur[0] == -1 && cur[1] == -1)) {
            skip.setDisable(false);
        }
    }

    @FXML
    void setTurnIndicator(char turnColor) {
        turnIndicator.setImage(new Image("sprites//" + turnColor + ".png"));
    }

    @FXML
    void choose(MouseEvent event) {
        if (!diceFinished) {
            return;
        }
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

    private void kickedToHome(MoveWrapper i, int p) {
        if (p == -1) {
            i.setVisible(false);
        } else {
            i.setVisible(true);
            Circle temp = null;
            for (Circle circle : circles) {
                if (circle != null && Integer.parseInt(circle.getId()) == p) {
                    temp = circle;
//                    circle.setEffect(new Glow(0.8));
                    break;
                }
            }
            if (temp != null) {
                i.setLayoutX(temp.getLayoutX() - i.getFitWidth() / 2);
                i.setLayoutY(temp.getLayoutY() - i.getFitHeight() / 4 * 3);
            }
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
        temp = Player.getPiece(tempId.charAt(0), tempId.charAt(1) - '0');
        isHome = temp.isHome();
        if (temp == null) System.out.println("Cannot find piece");
        else {
            if (!isHome) {
                possibleMove = Player.returnPossibleMove(temp);
                moveWrap[0].setLocation(possibleMove[0]);
                moveWrap[1].setLocation(possibleMove[1]);
                moveWrap[2].setLocation(possibleMove[2]);
                if (possibleMove[0] != -1)
                    kickedToHome(moveWrap[0], possibleMove[0]);
                if (possibleMove[1] != -1)
                    kickedToHome(moveWrap[1], possibleMove[1]);
                if (possibleMove[2] != -1)
                    kickedToHome(moveWrap[2], possibleMove[2]);
            } else {
                possibleMove = new int[]{cur[0], cur[1], -1};
                showHomeMove(i);
            }
        }
    }
}