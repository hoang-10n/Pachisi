package sample;

import javafx.scene.image.ImageView;

import java.util.Random;

public class Player {
    private Random rand = new Random();
    private int[][] home = new int[4][4];
    private static Piece[] chessPos = new Piece[16];
//
//    void print() {
//        for (Piece i : chessPos) {
//            System.out.print(i.getName() + " " + i.getPos() + " ");
//        }
//        System.out.println();
//    }

    void initialize(ImageView[] pieces) {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                for (ImageView piece : pieces) {
                    if (piece.getId().charAt(0) == Constants.COLOR.get(i)) {
                        if (piece.getId().charAt(1) - '0' == j) {
                            chessPos[i * 4 + j] = new Piece(piece, Constants.COLOR.get(i), j, 0, 56);
                            break;
                        }
                    }
                }
            }
        }
        for (int i = 0; i < 4; i++) {
            home[i] = new int[]{0, 0, 0, 0};
        }
    }

    /**
     * roll 2 dice with random value 1-6
     *
     * @return an array of 2 int, element[0] is bigger or equal to element[1]
     */
    int[] rollDice() {
        int[] dice = new int[2];
        // normal operation would return random number from 1-6
        //TODO change this back to normal when done debugging
//        for (int i = 0; i < dice.length; i++) {
//            dice[i] = rand.nextInt(6) + 1;
//        }
        // however for ease of debugging, the chance of hitting value 6 is multiplied
        for (int i = 0; i < dice.length; i++) {
            dice[i] = rand.nextInt(9) + 1;
            if (dice[i] > 6) {
                dice[i] = 6;
            }
        }
        return dice;
    }

    /**
     * check if a player of a certain color has any possible move
     *
     * @param color color of player to check
     * @return true if possible move exists. False if not
     */
    static boolean thereIsPossibleMove(char color) {
        for (Piece p : chessPos) {
            if (p.getColor() == color) {
                int[] temp = returnPossibleMove(p);
                for (int i : temp) {
                    if (i != -1) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private static boolean possibleMove(Piece piece, int die) {
        if (die == -1) return false;
        int j = piece.getDistanceFromHome();
        char temp = piece.getColor();
        for (Piece i : chessPos) {
            // do not let horses of the same team kick each other!
            if (piece.getPos() + die == i.getPos() && piece.getPos() != 0 && piece.getColor() == i.getColor()) {
                return false;
            }
            if ((piece.getPos() < i.getPos() && piece.getPos() + die > i.getPos() - (temp == i.getColor() ? 1 : 0)) || die > j)
                return false;
        }
        return true;
    }

    static Piece getPiece(char color, int id) {
        for (Piece i : chessPos) {
            if (i.getColor() == color && i.getId() == id) return i;
        }
        return null;
    }

    /**
     * return a null if no move is remaining, return -1 if move slot if not possible, otherwise return possible move offset from current position
     *
     * @param piece piece that is querying its possible moves
     * @return possible moves for the querying piece
     */
    public static int[] returnPossibleMove(Piece piece) {
        int[] temp = Controller.getCur();
        // if there is die value remaining
        if (temp[0] != -1 || temp[1] != -1) {
            int t = piece.getPos(), k = (temp[0] == -1 ? 0 : temp[0]) + (temp[1] == -1 ? 0 : temp[1]);
            int[] possible = new int[]{-1, -1, -1};
            if (piece.getPos() == 0) {
                possible[0] = (Controller.leaveNestPossible()) ? Constants.SPAWN_POS[Constants.COLOR.indexOf(piece.getColor())] : -1;
                if (possible[0] != -1) {
                    t = piece.getStartingPoint();
                    for (Piece p : chessPos) {
                        if (p.getPos() == t) {
                            return possible;
                        }
                    }
                    if (possibleMove(new Piece(piece.getColor(), t), temp[1]))
                        possible[1] = (t + temp[1] > 56 ? -56 + (t + temp[1]) : t + temp[1]);
                }
                return possible;
            }
            if (possibleMove(piece, temp[1])) {
                possible[1] = (t + temp[1] > 56 ? -56 + (t + temp[1]) : t + temp[1]);
            }
            if (possibleMove(piece, temp[0])) {
                possible[0] = (t + temp[0] > 56 ? -56 + (t + temp[0]) : t + temp[0]);
                if (possibleMove(piece, temp[1]))
                    possible[2] = possibleMove(piece, k) ? (t + k > 56 ? -56 + (t + k) : t + k) : -1;
            }
            if (temp[0] == temp[1]) {
                possible[1] = -1;
            }
            return possible;
        }
        return new int[]{-1, -1, -1};
    }
//
//    void print(int c) {
//        for (int i : home[c]) System.out.print(i + " ");
//    }

    boolean checkHome(int dice, int c, int k) {
        for (int i = 0; i < 4; i++) {
            if (i != k && home[c][i] <= dice && home[c][i] != 0) return false;
        }
        return true;
    }

    void updateHome(int dice, char color, int id) {
        int c = Constants.COLOR.indexOf(color);
        home[c][id] = dice;
    }

    Piece canBeKicked(int pos) {
        char tempColor = (char) -1;
        int tempId = -1;
        boolean canBeKicked = false;
        for (Piece chessPo : chessPos) {
            if (pos == chessPo.getPos()) {
                chessPo.setPos(0);
                tempColor = chessPo.getColor();
                tempId = chessPo.getId();
                canBeKicked = true;
                break;
            }
        }
        if (canBeKicked) {
            int i = Constants.COLOR.indexOf(tempColor);
            // return
            return getPiece(tempColor, tempId);
        }
        return null;
    }

//    class Machine {
//        void machineDecision(int turn) {
//            for (int i = turn * 4; i < turn * 4 +4; i++) {
//
//            }
//        }
//    }
}
