package ru.koshkin.TrajectoryVisual.core;

import ru.koshkin.TrajectoryVisual.Drawing.Drawable;

public class CoordsCalculator implements Runnable {

    private final long TIME_DIFF = 10; // in millis
    private final double g = 9.81;

    private final double H0;
    private final double V0;
    private final double a0;

    private double a;
    private double x;
    private double y;
    private double V;
    private long t;

    private Drawable drawable;

    public CoordsCalculator(double h0, double v0, double a0, Drawable drawable) {
        H0 = h0;
        V0 = v0;
        this.a0 = a0;
        this.drawable = drawable;
        a = a0 * Math.PI / 180;
        x = 0;
        y = H0;
        V = V0;
        t = 0;
    }

    public double maxHeight() {
        double tRise = V0 * Math.sin(a) / g;
        double hRise = V0 * Math.sin(a) * tRise - g * Math.pow(tRise, 2) / 2;
        return H0 + hRise;
    }

    public double maxLength() {
        double maxHeight = maxHeight();
        double tRise = V0 * Math.sin(a) / g;
        double tFall = Math.sqrt(2 * maxHeight / g);
        return V0 * Math.cos(a) * (tRise + tFall);
    }

    public void run() {
        final double tDiff = Double.valueOf(TIME_DIFF) / Double.valueOf(1000);
        double VVert = V * Math.sin(a);
        double VHor = V * Math.cos(a);
        while (y > 0) {
            double dY = VVert * tDiff - g * Math.pow(tDiff, 2) / 2;
            y += dY;
            VVert -= g * tDiff;
            x += VHor * tDiff;
            try {
                Thread.sleep(TIME_DIFF);
                t += TIME_DIFF;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            V = Math.sqrt(Math.pow(VVert, 2) + Math.pow(VHor, 2));
            drawable.getCurrentSituation(x, y);
        }
    }
}
