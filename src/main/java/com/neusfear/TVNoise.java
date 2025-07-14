package com.neusfear;

import com.neusfear.visualizations.Basic2DNoiseVisualizer;
import com.neusfear.visualizations.NoiseViewer2D;
import com.neusfear.visualizations.BilinearInterpolationVisualizer;

import javax.swing.*;

import static com.neusfear.utils.VisualizationQuadrant.*;

public class TVNoise {

    public static void main(String[] args) {

        int width = 1024;
        int height = 1024;

        JFrame frame = new JFrame("Noise Viewer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(width, height);

        frame.add(new NoiseViewer2D(width, height,
                new Basic2DNoiseVisualizer(TOP_LEFT, width / 2, height / 2, 0),
                new Basic2DNoiseVisualizer(TOP_RIGHT, width / 2, height / 2, 0),
                new BilinearInterpolationVisualizer(BOTTOM_LEFT, width / 2, height / 2, 0),
                new BilinearInterpolationVisualizer(BOTTOM_RIGHT, width / 2, height / 2, 0)
        ));

        frame.setVisible(true);
    }

}
