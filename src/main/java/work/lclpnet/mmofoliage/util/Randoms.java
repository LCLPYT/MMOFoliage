package work.lclpnet.mmofoliage.util;

import java.util.Random;

public class Randoms {

    public static int getRandomInt(int i, int j, Random random) {
        final int max = Math.max(i, j), min = Math.min(i, j);
        return min + random.nextInt(max - min + 1);
    }
}
