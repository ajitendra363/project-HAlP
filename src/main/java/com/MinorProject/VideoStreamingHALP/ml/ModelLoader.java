package com.MinorProject.VideoStreamingHALP.ml;

import java.nio.file.*;

public class ModelLoader {
    private final String path;
    private boolean loaded;

    public ModelLoader(String path) { this.path = path; }

    public void load() {
        try { loaded = Files.exists(Path.of(path)); }
        catch (Exception e) { loaded = false; }
    }

    public boolean isLoaded() { return loaded; }
}
