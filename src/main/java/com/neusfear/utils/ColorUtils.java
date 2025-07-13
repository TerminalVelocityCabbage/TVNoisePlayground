package com.neusfear.utils;

import java.awt.*;


public class ColorUtils {

    private static final double GOLDEN_RATIO_CONJUGATE = 0.61803398875;

    /**
     * Returns a unique, visually distinct RGB color from an ID as a 0xRRGGBB int.
     */
    public static int colorFromIdInt(int id) {
        double hue = (scramble(id) * GOLDEN_RATIO_CONJUGATE) % 1.0;
        Color color = Color.getHSBColor((float) hue, 0.6f, 0.95f);
        return (color.getRed() << 16) | (color.getGreen() << 8) | color.getBlue();
    }

    // Scrambles similar integers to unrelated ones for better hue spread
    private static int scramble(int x) {
        x ^= (x >>> 17);
        x *= 0xed5ad4bb;
        x ^= (x >>> 11);
        x *= 0xac4c1b51;
        x ^= (x >>> 15);
        x *= 0x31848bab;
        x ^= (x >>> 14);
        return x & 0x7FFFFFFF;
    }

    // Example usage
    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            int rgb = colorFromIdInt(i);
            System.out.printf("ID %d -> 0x%06X%n", i, rgb);
        }
    }
}
