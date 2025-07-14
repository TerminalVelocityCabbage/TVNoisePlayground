package com.neusfear.visualizations;

import com.neusfear.utils.VisualizationQuadrant;

import java.awt.image.BufferedImage;

public abstract class NoiseVisualizer2D {

    final int width;
    final int height;

    final int yOffset;
    final int xOffset;

    final int visType;

    public NoiseVisualizer2D(VisualizationQuadrant quadrant, int width, int height, int visType) {
        this.width = width;
        this.height = height;
        this.visType = visType;

        xOffset = quadrant.isRight() ? width : 0;
        yOffset = quadrant.isBottom() ? height : 0;
    }

    public abstract float populateNoiseValues(int iteration);

    public abstract BufferedImage getImage();
}
