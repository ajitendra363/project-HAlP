package com.MinorProject.VideoStreamingHALP.repository;

import com.MinorProject.VideoStreamingHALP.model.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VideoRepository extends JpaRepository<Video, Long> {
    // No need to add anything here, JpaRepository provides:
    // save(), findAll(), findById(), deleteById(), etc.
}
