package com.neusfear.visualizations;

import com.neusfear.noise.OpenSimplex2S;
import com.neusfear.noise.Voronoi3D;
import com.neusfear.utils.ColorUtils;
import com.neusfear.utils.VisualizationQuadrant;

import java.awt.image.BufferedImage;

public class VoronoiBiomeVisualizer extends NoiseVisualizer{

    private static final boolean SWIRL = true;
    private static final double SCALE = 0.005;
    private static final float SWIRL_AMOUNT_1 = 45f;
    private static final float NOISE_SCALE_1 = .02f;
    private static final float SWIRL_AMOUNT_2 = 20f;
    private static final float NOISE_SCALE_2 = .07f;

    double[][] distances = new double[width][height];
    int[][] ids = new int[width][height];
    int[][] neighbours = new int[width][height];

    public VoronoiBiomeVisualizer(VisualizationQuadrant quadrant, int width, int height, int visType) {
        super(quadrant, width, height, visType);
    }

    @Override
    public float populateNoiseValues(int iteration) {
        float noiseOctave1, noiseOctave2;
        long startTime = System.currentTimeMillis();

        // Compute distance to nearest neighbor per pixel
        double xd, yd, zd;
        for (int x1 = 0; x1 < height; x1++) {
            for (int y1 = 0; y1 < width; y1++) {
                if (SWIRL) {
                    noiseOctave1 = SWIRL_AMOUNT_1 * OpenSimplex2S.noise3_ImproveXY(0, (x1 + xOffset) * NOISE_SCALE_1, (y1 + yOffset) * NOISE_SCALE_1, iteration);
                    noiseOctave2 = SWIRL_AMOUNT_2 * OpenSimplex2S.noise3_ImproveXY(0, (x1 + xOffset) * NOISE_SCALE_2, (y1 + yOffset) * NOISE_SCALE_2, iteration);
                    xd = (x1 + xOffset + noiseOctave1 + noiseOctave2) * SCALE;
                    yd = (y1 + yOffset + noiseOctave1 + noiseOctave2) * SCALE;
                    zd = (iteration + noiseOctave1 + noiseOctave2) * SCALE;
                } else {
                    xd = (x1 + xOffset) * SCALE;
                    yd = (y1 + yOffset) * SCALE;
                    zd = iteration * SCALE;
                }

                if (visType != 0 && visType != 1) {
                    distances[x1][y1] = Voronoi3D.distanceToNearestNeighbor(xd, yd, zd);
                }
                if (visType != 1) {
                    ids[x1][y1] = Voronoi3D.getId(xd, yd, zd);
                }
                neighbours[x1][y1] = Voronoi3D.getNearestNeighborID(xd, yd, zd);
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
                    case 0 -> { //Show cell ID
                        image.setRGB(x, y, ColorUtils.colorFromIdInt(ids[x][y]));
                    }
                    case 1 -> { //Show Neighbor cell ID
                        image.setRGB(x, y, ColorUtils.colorFromIdInt(neighbours[x][y]));
                    }
                    case 2 -> { //Show Distance to neighbor:
                        int v = (int)(255 * distances[x][y]);
                        int rgb = (v << 16) | (v << 8) | v;
                        image.setRGB(x, y, rgb);
                    }
                    case 3 -> { //show colors with distance indicated
                        int color = ColorUtils.colorFromIdInt(ids[x][y]);
                        int red = (color >> 16) & 0xFF;
                        int green = (color >> 8) & 0xFF;
                        int blue = color & 0xFF;
                        red *= distances[x][y] * 2;
                        blue *= distances[x][y] * 2;
                        green *= distances[x][y] * 2;
                        int rgb = (red << 16) | (green << 8) | blue;
                        image.setRGB(x, y, rgb);
                    }
                    case 4 -> { //edge biomes
                        int color = ColorUtils.colorFromIdInt(ids[x][y]);
                        int red = (color >> 16) & 0xFF;
                        int green = (color >> 8) & 0xFF;
                        int blue = color & 0xFF;
                        red = (int) Math.max(0, 1 - distances[x][y]);
                        blue = (int) Math.max(0, 1 - distances[x][y]);
                        green = (int) Math.max(0, 1 - distances[x][y]);
                        int rgb = (red << 16) | (green << 8) | blue;
                        image.setRGB(x, y, rgb);
                    }
                    case 5 -> { //Show Distance to neighbor with border threshold:
                        int v = (int)(255 * distances[x][y]);
                        int rgb = (v << 16) | (v << 8) | v;
                        image.setRGB(x, y, distances[x][y] > 0.01 ? rgb : 0xffffff);
                    }
                    case 6 -> { //Different id at borders
                        if (distances[x][y] < 0.5) {
                            image.setRGB(x, y, ColorUtils.colorFromIdInt(ids[x][y]));
                        } else {
                            image.setRGB(x, y, ColorUtils.colorFromIdInt(ids[x][y] + neighbours[x][y]));
                        }
                    }
                    case 7 -> { //id color only at borders
                        if (distances[x][y] < .03) {
                            image.setRGB(x, y, ColorUtils.colorFromIdInt(ids[x][y]));
                        } else {
                            image.setRGB(x, y, 0);
                        }
                    }
                }
            }
        }

        return image;
    }
}
