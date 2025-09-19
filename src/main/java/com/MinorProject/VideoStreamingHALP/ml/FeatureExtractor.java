package com.MinorProject.VideoStreamingHALP.ml;

import com.MinorProject.VideoStreamingHALP.model.Video;

import java.time.Duration;
import java.time.Instant;

public class FeatureExtractor {
    // x = [log1p(views), log1p(recentViews), log1p(cacheHits), log1p(ageSecs), log1p(sizeBytes)]
    public static double[] features(Video v, long recentViews, long cacheHits) {
        long ageSecs = 0L;
        try {
            // if Video has no timestamp property, derive age as 0 (or add upload time later)
            ageSecs = 0L;
        } catch (Exception ignored) {}
        long views = 0L; // keep 0 if not tracked yet
        long size = 0L;  // will be filled by CacheEntry.sizeBytes
        return new double[] {
                Math.log1p(views),
                Math.log1p(recentViews),
                Math.log1p(cacheHits),
                Math.log1p(ageSecs),
                Math.log1p(size)
        };
    }
}
