package com.neusfear.noise;

import java.util.HashMap;
import java.util.Map;

public class Voronoi2D {

    static int cx, cy, nx, ny, id;
    static double[] offset;
    static double fx, fy, d;

    private static final Map<String, Integer> neighborCache = new HashMap<>();

    private static String generateMapIdFromCoordinates(int x, int y) {
        return "x" + x + "y" + y;
    }

    private static int hash2D(int x, int y) {
        final int PRIME1 = 73856093;
        final int PRIME2 = 19349663;
        return (x * PRIME1 ^ y * PRIME2) & 0xFFFFFFFF;
    }

    private static double[] randomOffset(int x, int y) {
        int seed = hash2D(x, y);

        return new double[]{
                rng(seed),
                rng(seed ^ 0x68bc21d4),
        };
    }

    // Fast random number based on integer input with some moving of bits around
    private static double rng(int s) {
        return (s * 0x6AC690C5 & 0x0FFFFFFF) / (double) 0x0FFFFFFF;
    }

    private static double distance(double x1, double y1, double x2, double y2) {
        double dx = x1 - x2, dy = y1 - y2;
        return Math.sqrt(dx * dx + dy * dy);
    }

    public static int getId(double x, double y) {
        cx = (int)Math.floor(x);
        cy = (int)Math.floor(y);

        double minDist = Double.MAX_VALUE;
        int nearestId = 0;

        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                nx = cx + dx;
                ny = cy + dy;

                id = hash2D(nx, ny);
                offset = randomOffset(nx, ny);

                fx = nx + offset[0];
                fy = ny + offset[1];

                d = distance(x, y, fx, fy);
                if (d < minDist) {
                    minDist = d;
                    nearestId = id;
                }
            }
        }

        return nearestId;
    }

    public static int getNearestNeighborID(double x, double y) {
        cx = (int)Math.floor(x);
        cy = (int)Math.floor(y);

        String idStr = generateMapIdFromCoordinates(cx, cy);
        if (neighborCache.containsKey(idStr)) {
            return neighborCache.get(idStr);
        }

        double minDist = Double.MAX_VALUE;
        int nearestId = -1;

        // First, get the current Voronoi cell's feature ID
        int currentId = getId(x, y);

        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                nx = cx + dx;
                ny = cy + dy;

                int candidateId = hash2D(nx, ny);
                if (candidateId == currentId) continue; // Exclude self

                offset = randomOffset(nx, ny);
                fx = nx + offset[0];
                fy = ny + offset[1];

                d = distance(x, y, fx, fy);
                if (d < minDist) {
                    minDist = d;
                    nearestId = candidateId;
                }
            }
        }

        neighborCache.put(idStr, nearestId);

        return nearestId;
    }

    public static double distanceToNearestNeighbor(double x, double y) {
        cx = (int)Math.floor(x);
        cy = (int)Math.floor(y);

        double nearest = Double.MAX_VALUE;
        double secondNearest = Double.MAX_VALUE;

        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                nx = cx + dx;
                ny = cy + dy;

                offset = randomOffset(nx, ny);
                fx = nx + offset[0];
                fy = ny + offset[1];

                d = distance(x, y, fx, fy);

                if (d < nearest) {
                    secondNearest = nearest;
                    nearest = d;
                } else if (d < secondNearest) {
                    secondNearest = d;
                }
            }
        }

        // Distance to edge is half the difference between the two closest site distances
        return 0.5 * (secondNearest - nearest);
    }

}
