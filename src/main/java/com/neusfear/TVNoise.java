package com.neusfear;

import com.neusfear.visualizations.NoiseViewer;
import com.neusfear.visualizations.VoronoiBiomeVisualizer;

import javax.swing.*;

import static com.neusfear.utils.VisualizationQuadrant.*;

public class TVNoise {

    public static void main(String[] args) {

        int width = 1024;
        int height = 1024;

        JFrame frame = new JFrame("Noise Viewer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(width, height);

        frame.add(new NoiseViewer(width, height,
                new VoronoiBiomeVisualizer(TOP_LEFT, width / 2, height / 2, 0),
                new VoronoiBiomeVisualizer(TOP_RIGHT, width / 2, height / 2, 2),
                new VoronoiBiomeVisualizer(BOTTOM_LEFT, width / 2, height / 2, 3),
                new VoronoiBiomeVisualizer(BOTTOM_RIGHT, width / 2, height / 2, 3)
        ));

        frame.setVisible(true);
    }

}
