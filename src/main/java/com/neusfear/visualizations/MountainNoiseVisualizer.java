package com.neusfear.visualizations;

import com.neusfear.noise.OpenSimplex2S;
import com.neusfear.utils.VisualizationQuadrant;

import java.awt.image.BufferedImage;

public class MountainNoiseVisualizer extends NoiseVisualizer2D {

    double[][] heights = new double[width][height];

    public MountainNoiseVisualizer(VisualizationQuadrant quadrant, int width, int height, int visType) {
        super(quadrant, width, height, visType);
    }

    @Override
    public float populateNoiseValues(int iteration) {

        long startTime = System.currentTimeMillis();

        float octave1Scale = 160f;
        float octave1Influence = 100;
        float octave2Scale = 80f;
        float octave2Influence = 50;
        float octave3Scale = 40f;
        float octave3Influence = 12;

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {

                float mountainNoise = (OpenSimplex2S.noise2(0, (x + xOffset) / octave1Scale, (y + yOffset) / octave1Scale) * 0.5f) + 0.5f;
                float shapeNoise = ((OpenSimplex2S.noise2(1, (x + xOffset) / octave2Scale, (y + yOffset) / octave2Scale) * 0.5f) + 0.5f) * mountainNoise;
                float detailNoise = (OpenSimplex2S.noise2(1, (x + xOffset) / octave3Scale, (y + yOffset) / octave3Scale) * 0.5f) + 0.5f;

                heights[x][y] =
                        (
                                Math.pow(mountainNoise, 5) * octave1Influence +
                                Math.pow(shapeNoise, 3) * octave2Influence +
                                detailNoise * octave3Influence
                        );

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
                    case 0 -> { //Mountains
                        int v = (int) heights[x][y];
                        int rgb = (v << 16) | (v << 8) | v;
                        image.setRGB(x, y, rgb);
                    }
                }
            }
        }

        return image;
    }
}
