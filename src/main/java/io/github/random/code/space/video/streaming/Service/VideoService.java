package io.github.random.code.space.video.streaming.Service;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Service
public class VideoService {

    @Value("${video.storage.location}")
    private Path videoStorageLocation;

    public List<String> listAllVideos() throws IOException {
        List<String> videoList = new ArrayList<>();
        Files.list(videoStorageLocation).filter(Files::isRegularFile).forEach(videoPath -> videoList.add(videoPath.getFileName().toString()));
        return videoList;
    }
}
