package com.neusfear.visualizations;

import com.neusfear.noise.OpenSimplex2S;
import com.neusfear.utils.VisualizationQuadrant;

import java.awt.image.BufferedImage;

public class Basic3DNoiseVisualizer extends NoiseVisualizer3D {

    float scale = 50f;
    double[][][] densities = new double[width][height][depth];

    public Basic3DNoiseVisualizer(VisualizationQuadrant quadrant, int width, int height, int depth, int visType) {
        super(quadrant, width, height, depth, visType);
    }

    @Override
    public float populateNoiseValues(int iteration) {

        long startTime = System.currentTimeMillis();

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                for (int z = 0; z < depth; z++) {
                    densities[x][y][z] = (OpenSimplex2S.noise3_ImproveXY(iteration, (x + xOffset) / scale, (y + yOffset) / scale, z / scale) / 2) + 0.5f;
                }
            }
        }

        return System.currentTimeMillis() - startTime;
    }

    @Override
    public BufferedImage getImage(int depth) {

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        for (int x = 0; x < height; x++) {
            for (int y = 0; y < width; y++) {
                switch (visType) {
                    case 0 -> {
                        int v = (int) (255 * densities[x][y][depth]);
                        int rgb = (v << 16) | (v << 8) | v;
                        image.setRGB(x, y, rgb);
                    }
                }
            }
        }

        return image;
    }
}
