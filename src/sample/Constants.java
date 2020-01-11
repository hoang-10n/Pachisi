package sample;

import java.util.ArrayList;
import java.util.Arrays;

public class Constants {
    /**
     * spawn position corresponding to blue, yellow, green, red
     */
    public static final int[] SPAWN_POS = new int[]{1, 15, 29, 43};
    /**
     * blue, red, yellow, green. Made ArrayList for ease of use COLOR.indexOf()
     */
    public static final ArrayList<Character> COLOR = new ArrayList<>(
            Arrays.asList('b', 'y', 'g', 'r'));
    public static final int[] HOME = new int[]{-36, 705, -1, 670};
    public static final int[] START = new int[]{6};
    public static final int[][] POS = new int[][]{{287, 287, 287, 287, 287, 287, 287, 239, 191, 143, 95, 47, -1, -1, -1, 47, 95, 143, 191, 239, 287, 287, 287, 287, 287, 287, 287, 352, 417, 417, 417, 417, 417, 417, 417, 465, 513, 561, 609, 657, 705, 705, 705, 657, 609, 561, 513, 465, 417, 417, 417, 417, 417, 417, 417, 352},
            {-36, 12, 60, 108, 156, 204, 252, 252, 252, 252, 252, 252, 252, 317, 382, 382, 382, 382, 382, 382, 382, 430, 478, 526, 574, 622, 670, 670, 670, 622, 574, 526, 478, 430, 382, 382, 382, 382, 382, 382, 382, 317, 252, 252, 252, 252, 252, 252, 252, 204, 156, 108, 60, 12, -36, -36}};

}
