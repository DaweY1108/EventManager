package me.dawey.eventmanager.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Calculation {
    public static int randomNumberBetween(int min, int max) {
        Random r = new Random();
        return r.nextInt((max + 1) - min) + min;
    }

    public static boolean isTruePercent(int percentage) {
        Random rand = new Random();
        return (rand.nextInt(100) < percentage);
    }

    public static List<Integer> getRandomNumbers(int min, int max, int count) {
        List<Integer> numbers = new ArrayList<>();
        for (int i = 0; i < count && i < max; i++) {
            int number = randomNumberBetween(min, max);
            if (!numbers.contains(number)) {
                numbers.add(number);
            } else {
                i--;
            }
        }
        return numbers;
    }
}
