package com.neusfear.visualizations;

import java.awt.*;
import java.awt.image.BufferedImage;

import static com.neusfear.utils.VisualizationQuadrant.*;

public class NoiseViewer3D extends NoiseViewer {

    BufferedImage tlImage;
    BufferedImage trImage;
    BufferedImage blImage;
    BufferedImage brImage;

    final int width;
    final int height;
    final int depth;

    public NoiseViewer3D(int width, int height, int depth,
                         NoiseVisualizer3D tlVisualizer,
                         NoiseVisualizer3D trVisualizer,
                         NoiseVisualizer3D blVisualizer,
                         NoiseVisualizer3D brVisualizer) {

        this.width = width;
        this.height = height;
        this.depth = depth;

        // Start animation thread
        startAnimation(1000, 50, tlVisualizer, trVisualizer, blVisualizer, brVisualizer);
    }

    private void startAnimation(int iterations, int frameDelayMillis, NoiseVisualizer3D tlVisualizer, NoiseVisualizer3D trVisualizer, NoiseVisualizer3D blVisualizer, NoiseVisualizer3D brVisualizer) {
        new Thread(() -> {
            for (int i = 0; i < iterations; i++) {
                addDrawTimesByQuadrant(TOP_LEFT, tlVisualizer.populateNoiseValues(i));
                addDrawTimesByQuadrant(TOP_RIGHT, trVisualizer.populateNoiseValues(i));
                addDrawTimesByQuadrant(BOTTOM_LEFT, blVisualizer.populateNoiseValues(i));
                addDrawTimesByQuadrant(BOTTOM_RIGHT, brVisualizer.populateNoiseValues(i));
                try {
                    for (int d = 0; d < depth; d++) {
                        tlImage = tlVisualizer.getImage(d);
                        trImage = trVisualizer.getImage(d);
                        blImage = blVisualizer.getImage(d);
                        brImage = brVisualizer.getImage(d);
                        repaint();
                        Thread.sleep(frameDelayMillis);
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int quadWidth = getWidth() / 2;
        int quadHeight = getHeight() / 2;
        g.drawImage(tlImage, 0, 0, quadWidth, quadHeight, null);
        g.drawImage(trImage, quadWidth, 0, quadWidth, quadHeight, null);
        g.drawImage(blImage, 0, quadHeight, quadWidth, quadHeight, null);
        g.drawImage(brImage, quadWidth, quadHeight, quadWidth, quadHeight, null);
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 16));
        float tlDt = getAverageDrawTime(TOP_LEFT);
        float trDt = getAverageDrawTime(TOP_RIGHT);
        float blDt = getAverageDrawTime(BOTTOM_LEFT);
        float brDt = getAverageDrawTime(BOTTOM_RIGHT);
        g.drawString((!drawTimes.get(TOP_LEFT).isEmpty() ? drawTimes.get(TOP_LEFT).getLast() : "-") + " ms " + tlDt + " ms avg", 10, 20);
        g.drawString((!drawTimes.get(TOP_RIGHT).isEmpty() ? drawTimes.get(TOP_RIGHT).getLast() : "-") + " ms " + trDt + " ms avg", (width / 2) + 10, 20);
        g.drawString((!drawTimes.get(BOTTOM_LEFT).isEmpty() ? drawTimes.get(BOTTOM_LEFT).getLast() : "-") + " ms " + blDt + " ms avg", 10, (width / 2));
        g.drawString((!drawTimes.get(BOTTOM_RIGHT).isEmpty() ? drawTimes.get(BOTTOM_RIGHT).getLast() : "-") + " ms " + brDt + " ms avg", (width / 2) + 10, (width / 2));
    }
}