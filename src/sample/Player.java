package sample;

import java.util.Random;

public class Player {
    private Random rand = new Random();
    private int[][] home = new int[4][4];
    private Piece[] chessPos = new Piece[16];
//
//    void print() {
//        for (Piece i : chessPos) {
//            System.out.print(i.getName() + " " + i.getPos() + " ");
//        }
//        System.out.println();
//    }

    void initialize() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                chessPos[i * 4 + j] = new Piece(Constants.COLOR.get(i), j, 0, 56);
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
        // however for ease of debugging, the chance of hitting value 5 and 6 is multiplied by 3
        for (int i = 0; i < dice.length; i++) {
            dice[i] = rand.nextInt(9) + 1;
            if (dice[i] > 7) {
                dice[i] = 6;
            } else if (dice[i] > 5) {
                dice[i] = 5;
            }
        }
        return dice;
    }

    private boolean possibleMove(Piece piece, int dices, int t) {
        int j = piece.getDistanceFromHome();
        char temp = piece.getColor();
        for (Piece i : chessPos) {
            if ((t < i.getPos() && t + dices > i.getPos() - (temp == i.getColor() ? 1 : 0)) || dices > j)
                return false;
        }
        return true;
    }

    Piece getPiece(char color, int id) {
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
    public int[] returnPossibleMove(Piece piece) {
        int[] temp = Controller.getCur();
        // if there is die value remaining
        if (temp[0] != -1 || temp[1] != -1) {
            int t = piece.getPos(), k = temp[0] + temp[1];
            int[] possible = new int[]{-1, -1, -1};
            if (piece.getPos() == 0 && possibleMove(piece, 1, t)) {
                possible[2] = (temp[0] == 6) ? Constants.SPAWN_POS[Constants.COLOR.indexOf(piece.getColor())] : -1;
                return possible;
            }
            if (possibleMove(piece, temp[1], t)) {
                if (temp[1] != -1) {
                    possible[0] = (t + temp[1] > 56 ? -56 + (t + temp[1]) : t + temp[1]);
                }
                if (possibleMove(piece, temp[0], t)) {
                    if (temp[0] != -1) {
                        possible[1] = (t + temp[0] > 56 ? -56 + (t + temp[0]) : t + temp[0]);
                        if (temp[1] != -1) {
                            possible[2] = possibleMove(piece, temp[0] + temp[1], t) ? (t + k > 56 ? -56 + (t + k) : t + k) : -1;
                        }
                    }
                }
            }
            return possible;
        }
        return null;
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

    Object[] canBeKicked(int pos) {
        char tempColor = (char) -1;
        int tempId = -1;
        boolean canBeKicked = false;
        for (int k = 0; k < 16; k++) {
            if (pos == chessPos[k].getPos()) {
                chessPos[k].setPos(0);
                tempColor = chessPos[k].getColor();
                tempId = chessPos[k].getId();
                canBeKicked = true;
                break;
            }
        }
        if (canBeKicked) {
            int i = Constants.COLOR.indexOf(tempColor);
            // return
            return new Object[]{60 + 130 * (tempId % 2) + 450 * (i % 2), 50 + 120 * (tempId / 2) + 450 * (i / 2), tempColor, tempId};
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
