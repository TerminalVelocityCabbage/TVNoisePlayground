package com.neusfear.visualizations;

import com.neusfear.noise.OpenSimplex2S;
import com.neusfear.noise.Voronoi2D;
import com.neusfear.noise.Voronoi3D;
import com.neusfear.utils.ColorUtils;
import com.neusfear.utils.VisualizationQuadrant;

import java.awt.image.BufferedImage;

public class VoronoiRiverVisualizer extends NoiseVisualizer2D {

    private static final boolean SWIRL = true;
    private static final double SCALE = 0.003;
    private static final float SWIRL_AMOUNT_1 = 40;
    private static final float NOISE_SCALE_1 = .02f;
    private static final float SWIRL_AMOUNT_2 = 4f;
    private static final float NOISE_SCALE_2 = .07f;
    private static final float WEIGHT_SCALE = 150f;

    double[][] distances = new double[width][height];
    double[][] weights = new double[width][height];

    public VoronoiRiverVisualizer(VisualizationQuadrant quadrant, int width, int height, int visType) {
        super(quadrant, width, height, visType);
    }

    @Override
    public float populateNoiseValues(int iteration) {
        float noiseOctave1, noiseOctave2;
        long startTime = System.currentTimeMillis();

        // Compute distance to nearest neighbor per pixel
        double xd, yd;
        for (int x1 = 0; x1 < height; x1++) {
            for (int y1 = 0; y1 < width; y1++) {
                if (SWIRL) {
                    noiseOctave1 = SWIRL_AMOUNT_1 * OpenSimplex2S.noise2(iteration, (x1 + xOffset) * NOISE_SCALE_1, (y1 + yOffset) * NOISE_SCALE_1);
                    noiseOctave2 = SWIRL_AMOUNT_2 * OpenSimplex2S.noise2(iteration, (x1 + xOffset) * NOISE_SCALE_2, (y1 + yOffset) * NOISE_SCALE_2);
                    xd = (x1 + xOffset + noiseOctave1 + noiseOctave2) * SCALE;
                    yd = (y1 + yOffset + noiseOctave1 + noiseOctave2) * SCALE;
                } else {
                    xd = (x1 + xOffset) * SCALE;
                    yd = (y1 + yOffset) * SCALE;
                }

                distances[x1][y1] = Voronoi2D.distanceToNearestNeighbor(xd, yd);
                weights[x1][y1] = OpenSimplex2S.noise2(iteration, (x1 + xOffset) / WEIGHT_SCALE, (y1 + yOffset) / WEIGHT_SCALE) / 2f + 0.5f;
            }
        }

        return System.currentTimeMillis() - startTime;
    }

    @Override
    public BufferedImage getImage() {

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        // Normalize and write pixels
        for (int x = 0; x < height; x++) {
            for (int y = 0; y < width; y++) {
                switch (visType) {
                    case 0 -> { //Show Distance to neighbor:
                        double river = Math.pow((1 - distances[x][y]), 300);
                        river *= Math.pow(weights[x][y], 3);
                        int v = (int)(255 * river);
                        int rgb = (v << 16) | (v << 8) | v;
                        image.setRGB(x, y, rgb);
                    }
                }
            }
        }

        return image;
    }
}
