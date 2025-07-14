package com.neusfear.visualizations;

import com.neusfear.noise.OpenSimplex2S;
import com.neusfear.utils.VisualizationQuadrant;

import java.awt.image.BufferedImage;

public class Basic2DNoiseVisualizer extends NoiseVisualizer {

    float scale = 50f;
    double[][] densities = new double[width][height];

    public Basic2DNoiseVisualizer(VisualizationQuadrant quadrant, int width, int height, int visType) {
        super(quadrant, width, height, visType);
    }

    @Override
    public float populateNoiseValues(int z1) {

        long startTime = System.currentTimeMillis();

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                densities[x][y] = (OpenSimplex2S.noise2(0, (x + xOffset) / scale, (y + yOffset) / scale) / 2) + 0.5f;
            }
        }

        return System.currentTimeMillis() - startTime;
    }

    @Override
    public BufferedImage getImage() {

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        for (int x = 0; x < height; x++) {
            for (int y = 0; y < width; y++) {
                switch (visType) {
                    case 0 -> {
                        int v = (int) (255 * densities[x][y]);
                        int rgb = (v << 16) | (v << 8) | v;
                        image.setRGB(x, y, rgb);
                    }
                }
            }
        }

        return image;
    }
}
