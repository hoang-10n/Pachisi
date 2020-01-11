package sample;

import java.util.Random;

public class Player {
    private Random rand =  new Random();
    private int[] spawnPos = new int[] {1, 43, 15, 29};
    private int[][] home = new int[4][4];

    private Piece[] chessPos = new Piece[16];
    private int[] dice = new int[] {6, 5};
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
                chessPos[i*4 + j] = new Piece(Character.toString('a'+ i) + j, 0, 56);
            }
        }
        for (int i = 0; i < 4; i++) {
            home[i] = new int[]{0,0,0,0};
        }
    }

    int[] roleDice() {
        dice[0] = rand.nextInt(6) + 1;
        dice[1] = rand.nextInt(6) + 1;
        int swap;
        if (dice[1] > dice[0]) {
            swap = dice[1];
            dice[1] = dice[0];
            dice[0] = swap;
        }
        return dice;
    }

    private boolean possibleMove(Piece piece, int dices, int t) {
        int j = piece.getDistanceFromHome();
        String temp = piece.getName();
        for (Piece i : chessPos) {
            if ((t < i.getPos() && t + dices > i.getPos() - (temp.charAt(0) == i.getName().charAt(0) ? 1 : 0)) || dices > j) return false;
        }
        return true;
    }

    Piece getPiece(String name) {
        for (Piece i : chessPos) {
            if (i.getName().equals(name)) return i;
        }
        return null;
    }

    public int[] returnPossibleMove(Piece piece) {
        int t = piece.getPos(), k = dice[0] + dice[1];
        int[] possible = new int[] {-1,-1,-1};
        if (piece.getPos() == 0 && possibleMove(piece, 1, t)) {
            possible[2] = (dice[0] == 6) ? spawnPos[piece.getName().charAt(0) - 'a'] : -1;
            return possible;
        }
        if (possibleMove(piece, dice[1], t)) {
            possible[0] = (t + dice[1] > 56 ? -56+ (t + dice[1]) : t + dice[1]);
            if (possibleMove(piece, dice[0], t)) {
                possible[1] = (t + dice[0] > 56 ? -56+ (t + dice[0]) : t + dice[0]);
                possible[2] = possibleMove(piece, dice[0] + dice[1], t) ? (t + k > 56 ? -56+ (t + k) : t + k) : -1;
            }
        }
        return possible;
    }
//
//    void print(int c) {
//        for (int i : home[c]) System.out.print(i + " ");
//    }

    boolean checkHome(int dice, int c, int k) {
        if (dice > home[c][k]) {
            for (int i = 0; i < 4; i++) {
                if (i != k && home[c][i] <= dice && home[c][i] != 0 && home[c][i] > home[c][k]) return false;
            }
        }
        return true;
    }

    void updateHome(int dice, String name) {
        int c = name.charAt(0) - 'a';
        int t = name.charAt(1) - '0';
        home[c][t] = dice;
    }

    Object[] kickable (int pos) {
        String temp = "";
        boolean kickable = false;
        for (int k = 0; k < 16; k++) {
            if (pos == chessPos[k].getPos()) {
                chessPos[k].setPos(0);
                temp = chessPos[k].getName();
                kickable = true;
                break;
            }
        }
        if (kickable) {
            int i = temp.charAt(0) - 'a';
            int j = temp.charAt(1) - '0';
            return new Object[]{60 + 130 * (j % 2) + 450 * (i % 2), 50 + 120 * (j / 2) + 450 * (i / 2),temp};
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
