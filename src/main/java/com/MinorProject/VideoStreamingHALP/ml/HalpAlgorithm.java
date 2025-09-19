package com.MinorProject.VideoStreamingHALP.ml;

// Two-stage HALP reusable scorer: deterministic heuristic + small learnable boost (stub)
public class HalpAlgorithm {
    public static double score(double[] x, boolean modelLoaded) {
        if (x == null || x.length < 27) return 0.0;

        // Example: simple sum of first 5 elements as base
        double base = 0.0;
        for (int i = 0; i < 5; i++) {
            base += x[i];
        }

        double heuristic = 0.5 * base
                + 0.3 * x[23]
                + 0.2 * x[24]
                - 0.1 * x[25]
                - 0.05 * x[26];

        double boost = modelLoaded ? 0.2 * (base + x[23]) : 0.0;

        return heuristic + boost;
    }
}
