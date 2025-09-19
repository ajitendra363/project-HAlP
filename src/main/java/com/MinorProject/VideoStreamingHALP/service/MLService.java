package com.MinorProject.VideoStreamingHALP.service;

import com.MinorProject.VideoStreamingHALP.ml.FeatureExtractor;
import com.MinorProject.VideoStreamingHALP.ml.HalpAlgorithm;
import com.MinorProject.VideoStreamingHALP.ml.ModelLoader;
import com.MinorProject.VideoStreamingHALP.model.Video;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class MLService {
    private final ModelLoader loader;

    public MLService(@Value("${halp.model.path:models/halp-model.bin}") String modelPath) {
        this.loader = new ModelLoader(modelPath);
        this.loader.load();
    }

    public double score(Video v, long recentViews, long cacheHits, long sizeBytes) {
        double[] x = FeatureExtractor.features(v, recentViews, cacheHits);
        // override size feature with actual size
        if (x.length >= 5) x[26] = Math.log1p(sizeBytes);
        return HalpAlgorithm.score(x, loader.isLoaded());
    }
}
