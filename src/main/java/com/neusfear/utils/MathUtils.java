package com.neusfear.utils;

public class MathUtils {

    /**
     *
     *             P010           P110
     *            +--------------+
     *           /|             /|
     *          / |            / |
     *     P011+--------------+P111
     *        |  |           |  | 
     *        |  P000--------|--P100
     *        | /            | /
     *        |/             |/
     *     P001+-------------+P101        
     *
     * | Corner | (x,y,z) |
     * | ------ | ------- |
     * | P000   | (0,0,0) |
     * | P100   | (1,0,0) |
     * | P010   | (0,1,0) |
     * | P110   | (1,1,0) |
     * | P001   | (0,0,1) |
     * | P101   | (1,0,1) |
     * | P011   | (0,1,1) |
     * | P111   | (1,1,1) |
     *
     * x0 is left x1 is right
     * y0 is bottom y1 is top
     * z0 is back z1 is front
     *
     * @param x a value from 0 to 1 between x0 and x1 planes
     * @param y a value from 0 to 1 between y0 and y1 planes
     * @param z a value from 0 to 1 between z0 and z1 planes
     * @return the resulting value from the interpolation
     */
    public static float trilinearInterpolate(
            float P000, float P100, float P010, float P110,
            float P001, float P101, float P011, float P111,
            float x, float y, float z
    ) {
        // Interpolate along X for each Y/Z layer
        float P00 = P000 * (1 - x) + P100 * x;
        float P10 = P010 * (1 - x) + P110 * x;
        float P01 = P001 * (1 - x) + P101 * x;
        float P11 = P011 * (1 - x) + P111 * x;

        // Interpolate along Y for each Z layer
        float P0 = P00 * (1 - y) + P10 * y;
        float P1 = P01 * (1 - y) + P11 * y;

        // Interpolate along Z
        return P0 * (1 - z) + P1 * z;
    }


    /**
     *
     *       P01 +-------+ P11
     *           |       |
     *           |       |
     *       P0  +-------+ P10
     *
     * | Corner | (x,y) |
     * | ------ | ----- |
     * | P00    | (0,0) |
     * | P10    | (1,0) |
     * | P01    | (0,1) |
     * | P11    | (1,1) |
     *
     * x0 is left x1 is right
     * y0 is bottom y1 is top
     *
     * @param x a value from 0 to 1 between x0 and x1 planes
     * @param y a value from 0 to 1 between y0 and y1 planes
     * @return the resulting value from the interpolation
     */
    public static float bilinearInterpolate(
            float P00, float P10, float P01, float P11,
            float x, float y
    ) {
        float P0 = P00 * (1 - x) + P10 * x;
        float P1 = P01 * (1 - x) + P11 * x;
        return P0 * (1 - y) + P1 * y;
    }


}
