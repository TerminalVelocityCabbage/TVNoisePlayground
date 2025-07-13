package com.neusfear.utils;

public enum VisualizationQuadrant {
    TOP_LEFT, TOP_RIGHT, BOTTOM_LEFT, BOTTOM_RIGHT;

    public boolean isTop() {
        return this == TOP_LEFT || this == TOP_RIGHT;
    }

    public boolean isBottom() {
        return !isTop();
    }

    public boolean isLeft() {
        return this == TOP_LEFT || this == BOTTOM_LEFT;
    }

    public boolean isRight() {
        return !isLeft();
    }
}
