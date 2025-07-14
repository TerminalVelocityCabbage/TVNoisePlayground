package com.neusfear.visualizations;

import com.neusfear.utils.VisualizationQuadrant;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.neusfear.utils.VisualizationQuadrant.*;

public class NoiseViewer2D extends JPanel {

    BufferedImage tlImage;
    BufferedImage trImage;
    BufferedImage blImage;
    BufferedImage brImage;

    final int width;
    final int height;

    Map<VisualizationQuadrant, List<Float>> drawTimes;

    public NoiseViewer2D(int width, int height,
                         NoiseVisualizer tlVisualizer,
                         NoiseVisualizer trVisualizer,
                         NoiseVisualizer blVisualizer,
                         NoiseVisualizer brVisualizer) {

        this.width = width;
        this.height = height;

        this.drawTimes = new HashMap<>();
        this.drawTimes.put(TOP_LEFT, new ArrayList<>());
        this.drawTimes.put(TOP_RIGHT, new ArrayList<>());
        this.drawTimes.put(BOTTOM_LEFT, new ArrayList<>());
        this.drawTimes.put(BOTTOM_RIGHT, new ArrayList<>());

        // Start animation thread
        startAnimation(1000, tlVisualizer, trVisualizer, blVisualizer, brVisualizer);
    }

    private float getAverageDrawTime(VisualizationQuadrant quadrant) {

        if (!drawTimes.containsKey(quadrant)) return 0f;

        List<Float> quadrantTimes = drawTimes.get(quadrant);
        if (quadrantTimes.size() < 2) return 0f;
        float times = 0f;
        for (int i = 0; i < quadrantTimes.size(); i++) {
            times += quadrantTimes.get(i);
        }
        return times / quadrantTimes.size();
    }

    private void addDrawTimesByQuadrant(VisualizationQuadrant quadrant, float drawTime) {
        drawTimes.get(quadrant).add(drawTime);
    }

    private void startAnimation(int iterations, NoiseVisualizer tlVisualizer, NoiseVisualizer trVisualizer, NoiseVisualizer blVisualizer, NoiseVisualizer brVisualizer) {
        new Thread(() -> {
            for (int i = 0; i < iterations; i++) {
                addDrawTimesByQuadrant(TOP_LEFT, tlVisualizer.populateNoiseValues(i));
                addDrawTimesByQuadrant(TOP_RIGHT, trVisualizer.populateNoiseValues(i));
                addDrawTimesByQuadrant(BOTTOM_LEFT, blVisualizer.populateNoiseValues(i));
                addDrawTimesByQuadrant(BOTTOM_RIGHT, brVisualizer.populateNoiseValues(i));
                tlImage = tlVisualizer.getImage();
                trImage = trVisualizer.getImage();
                blImage = blVisualizer.getImage();
                brImage = brVisualizer.getImage();
                repaint();
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