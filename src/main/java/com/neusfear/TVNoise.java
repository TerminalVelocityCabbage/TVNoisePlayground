package com.neusfear;

import com.neusfear.visualizations.*;

import javax.swing.*;

import static com.neusfear.utils.VisualizationQuadrant.*;

public class TVNoise {

    public static void main(String[] args) {

        int width = 1024;
        int height = 1024;
        int depth = 384;

        JFrame frame = new JFrame("Noise Viewer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(width, height);

        frame.add(new NoiseViewer2D(width, height,
                new VoronoiRiverVisualizer(TOP_LEFT, width / 2, height / 2, 0),
                new VoronoiRiverVisualizer(TOP_RIGHT, width / 2, height / 2, 0),
                new VoronoiRiverVisualizer(BOTTOM_LEFT, width / 2, height / 2, 0),
                new VoronoiRiverVisualizer(BOTTOM_RIGHT, width / 2, height / 2, 0)
        ));
//        frame.add(new NoiseViewer3D(width, height, depth,
//                new Basic3DNoiseVisualizer(TOP_LEFT, width / 2, height / 2, depth, 0),
//                new Basic3DNoiseVisualizer(TOP_RIGHT, width / 2, height / 2, depth, 0),
//                new TrilinearInterpolationVisualizer(BOTTOM_LEFT, width / 2, height / 2, depth, 0),
//                new TrilinearInterpolationVisualizer(BOTTOM_RIGHT, width / 2, height / 2, depth, 0)
//        ));

        frame.setVisible(true);
    }

}
