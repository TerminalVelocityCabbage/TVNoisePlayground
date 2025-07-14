package com.neusfear.visualizations;

import com.neusfear.noise.OpenSimplex2S;
import com.neusfear.utils.MathUtils;
import com.neusfear.utils.VisualizationQuadrant;

import java.awt.image.BufferedImage;

public class BilinearInterpolationVisualizer extends NoiseVisualizer2D {

    int spacing = 4; //width and height need to be divisible by this number
    float scale = 50f;
    float[][] densities = new float[width/spacing + 1][height/spacing + 1];
    float[][] interpolatedDensities = new float[width][height];

    public BilinearInterpolationVisualizer(VisualizationQuadrant quadrant, int width, int height, int visType) {
        super(quadrant, width, height, visType);
    }

    @Override
    public float populateNoiseValues(int iteration) {

        long startTime = System.currentTimeMillis();

        for (int x = 0; x < width / spacing + 1; x++) {
            for (int y = 0; y < height / spacing + 1; y++) {
                densities[x][y] = (OpenSimplex2S.noise2(0, (x + (double) xOffset / spacing) / (scale / spacing), (y + (double) yOffset / spacing) / (scale / spacing)) / 2) + 0.5f;
            }
        }

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                interpolatedDensities[x][y] = MathUtils.bilinearInterpolate(
                        densities[(int) Math.floor((double) x / spacing)][(int) Math.floor((double) y / spacing)],
                        densities[(int) Math.ceil((double) x / spacing)][(int) Math.floor((double) y / spacing)],
                        densities[(int) Math.floor((double) x / spacing)][(int) Math.ceil((double) y / spacing)],
                        densities[(int) Math.ceil((double) x / spacing)][(int) Math.ceil((double) y / spacing)],
                        (float) (x % spacing) / spacing, (float) (y % spacing) / spacing
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

                    case 0 -> {
                        int v = (int) (255 * interpolatedDensities[x][y]);
                        int rgb = (v << 16) | (v << 8) | v;
                        image.setRGB(x, y, rgb);
                    }
                }
            }
        }

        return image;
    }
}
