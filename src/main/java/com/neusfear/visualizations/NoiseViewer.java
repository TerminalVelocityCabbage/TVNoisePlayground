package com.neusfear.visualizations;

import com.neusfear.utils.VisualizationQuadrant;

import javax.swing.*;
import java.util.List;
import java.util.Map;

public abstract class NoiseViewer extends JPanel {

    Map<VisualizationQuadrant, List<Float>> drawTimes;

    protected float getAverageDrawTime(VisualizationQuadrant quadrant) {

        if (!drawTimes.containsKey(quadrant)) return 0f;

        List<Float> quadrantTimes = drawTimes.get(quadrant);
        if (quadrantTimes.size() < 2) return 0f;
        float times = 0f;
        for (int i = 0; i < quadrantTimes.size(); i++) {
            times += quadrantTimes.get(i);
        }
        return times / quadrantTimes.size();
    }

    protected void addDrawTimesByQuadrant(VisualizationQuadrant quadrant, float drawTime) {
        drawTimes.get(quadrant).add(drawTime);
    }

}
