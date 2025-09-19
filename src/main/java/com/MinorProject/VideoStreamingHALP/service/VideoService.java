package com.MinorProject.VideoStreamingHALP.service;

import com.MinorProject.VideoStreamingHALP.model.Video;
import com.MinorProject.VideoStreamingHALP.repository.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
public class VideoService {

    @Autowired
    private VideoRepository videoRepository;

    @Value("${video.upload.dir}")
    private String uploadDir;  // path from application.properties

    // Upload new video
    public Video uploadVideo(MultipartFile file, String title, String description) throws IOException {
        File directory = new File(uploadDir);
        if (!directory.exists()) directory.mkdirs();

        String filePath = uploadDir + "/" + file.getOriginalFilename();
        file.transferTo(new File(filePath));

        Video video = new Video();
        video.setTitle(title);
        video.setDescription(description);
        video.setFilePath("/videos/" + file.getOriginalFilename());
        video.setSizeBytes(file.getSize());  // NEW: save file size
        return videoRepository.save(video);
    }

    // Returns all videos
    public List<Video> getAllVideos() {
        return videoRepository.findAll();
    }

    // Returns single video by ID
    public Video getVideo(Long id) {
        return videoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Video not found"));
    }
}
