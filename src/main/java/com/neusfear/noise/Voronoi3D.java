package com.neusfear.noise;

import java.util.HashMap;
import java.util.Map;

public class Voronoi3D {

    static int cx, cy, cz, nx, ny, nz, id;
    static double[] offset;
    static double fx, fy, fz, d;

    private static final Map<String, Integer> idCache = new HashMap<>();
    private static final Map<String, Integer> neighborCache = new HashMap<>();

    private static String generateMapIdFromCoordinates(int x, int y, int z) {
        return "x" + x + "y" + y + "z" + z;
    }

    private static int hash3D(int x, int y, int z) {
        final int PRIME1 = 73856093;
        final int PRIME2 = 19349663;
        final int PRIME3 = 83492791;
        return (x * PRIME1 ^ y * PRIME2 ^ z * PRIME3) & 0xFFFFFFFF;
    }

    private static double[] randomOffset(int x, int y, int z) {
        int seed = hash3D(x, y, z);

        return new double[]{
                rng(seed),
                rng(seed ^ 0x68bc21d4),
                rng(seed ^ 0xd4e12c77),
        };
    }

    //Fast random number based on integer input with some moving of bits around
    private static double rng(int s) {
        return (s * 0x6AC690C5 & 0x0FFFFFFF) / (double) 0x0FFFFFFF;
    }

    private static double distance(double x1, double y1, double z1, double x2, double y2, double z2) {
        double dx = x1 - x2, dy = y1 - y2, dz = z1 - z2;
        return Math.sqrt(dx * dx + dy * dy + dz * dz);
    }

    public static int getId(double x, double y, double z) {
        cx = (int)Math.floor(x);
        cy = (int)Math.floor(y);
        cz = (int)Math.floor(z);

        double minDist = Double.MAX_VALUE;
        int nearestId = 0;

        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                for (int dz = -1; dz <= 1; dz++) {
                    nx = cx + dx;
                    ny = cy + dy;
                    nz = cz + dz;

                    id = hash3D(nx, ny, nz);
                    offset = randomOffset(nx, ny, nz);

                    fx = nx + offset[0];
                    fy = ny + offset[1];
                    fz = nz + offset[2];

                    d = distance(x, y, z, fx, fy, fz);
                    if (d < minDist) {
                        minDist = d;
                        nearestId = id;
                    }
                }
            }
        }

        return nearestId;
    }

    public static int getNearestNeighborID(double x, double y, double z) {
        cx = (int)Math.floor(x);
        cy = (int)Math.floor(y);
        cz = (int)Math.floor(z);

        String idStr = generateMapIdFromCoordinates(cx, cy, cz);
        if (neighborCache.containsKey(idStr)) {
            return neighborCache.get(idStr);
        }

        double minDist = Double.MAX_VALUE;
        int nearestId = -1;

        // First, get the current Voronoi cell's feature ID
        int currentId = getId(x, y, z);

        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                for (int dz = -1; dz <= 1; dz++) {
                    nx = cx + dx;
                    ny = cy + dy;
                    nz = cz + dz;

                    int candidateId = hash3D(nx, ny, nz);
                    if (candidateId == currentId) continue; // Exclude self

                    offset = randomOffset(nx, ny, nz);
                    fx = nx + offset[0];
                    fy = ny + offset[1];
                    fz = nz + offset[2];

                    d = distance(x, y, z, fx, fy, fz);
                    if (d < minDist) {
                        minDist = d;
                        nearestId = candidateId;
                    }
                }
            }
        }

        neighborCache.put(idStr, nearestId);

        return nearestId;
    }

    public static double distanceToNearestNeighbor(double x, double y, double z) {
        cx = (int)Math.floor(x);
        cy = (int)Math.floor(y);
        cz = (int)Math.floor(z);

        double nearest = Double.MAX_VALUE;
        double secondNearest = Double.MAX_VALUE;

        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                for (int dz = -1; dz <= 1; dz++) {
                    nx = cx + dx;
                    ny = cy + dy;
                    nz = cz + dz;

                    offset = randomOffset(nx, ny, nz);
                    fx = nx + offset[0];
                    fy = ny + offset[1];
                    fz = nz + offset[2];

                    d = distance(x, y, z, fx, fy, fz);

                    if (d < nearest) {
                        secondNearest = nearest;
                        nearest = d;
                    } else if (d < secondNearest) {
                        secondNearest = d;
                    }
                }
            }
        }

        // Distance to edge is half the difference between the two closest site distances
        return 0.5 * (secondNearest - nearest);
    }

}
