package io.github.random.code.space.video.streaming.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.LongAdder;


@RestController
public class VideoController {

    @Value("${video.storage.location}")
    private Path videoStorageLocation;


    @GetMapping("/video/{filename}")
    public ResponseEntity<StreamingResponseBody> streamVideo(@PathVariable String filename, @RequestHeader(value = "Range", required = false) String range) throws IOException {

        try {
            Resource videoFile = new UrlResource(videoStorageLocation.resolve(filename).toUri());
            long fileSize = videoFile.contentLength();

            LongAdder startAdder = new LongAdder();
            startAdder.add(0);
            long end = fileSize - 1;

            if (range != null) {
                String[] ranges = range.substring("bytes=".length()).split("-");
                startAdder.reset();
                startAdder.add(Long.parseLong(ranges[0]));
                if (ranges.length > 1) {
                    end = Long.parseLong(ranges[1]);
                }
            }

            long start = startAdder.longValue();
            long bytesToRead = end - start + 1;

            StreamingResponseBody responseBody = outputStream -> {
                try (InputStream inputStream = videoFile.getInputStream()) {
                    inputStream.skip(start);
                    byte[] buffer = new byte[8192];
                    long bytesRead = 0;
                    while (bytesRead < bytesToRead) {
                        int read = inputStream.read(buffer, 0, (int) Math.min(bytesToRead - bytesRead, buffer.length));
                        if (read == -1) break;
                        outputStream.write(buffer, 0, read);
                        bytesRead += read;
                    }
                }
            };

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.valueOf("video/mp4"));
            headers.setContentLength(bytesToRead);
            headers.set("Accept-Ranges", "bytes"); // Crucial: This tells the client that range requests are supported

            if (range != null) {
                headers.set("Content-Range", "bytes " + start + "-" + end + "/" + fileSize);
                return new ResponseEntity<>(responseBody, headers, HttpStatus.PARTIAL_CONTENT);
            } else {
                return new ResponseEntity<>(responseBody, headers, HttpStatus.OK);
            }
        } catch (MalformedURLException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/videos")
    public ResponseEntity<List<Map<String, String>>> listVideos() throws IOException {
        List<Map<String, String>> videoList = new ArrayList<>();

        Files.list(videoStorageLocation).filter(Files::isRegularFile).forEach(videoPath -> {
            String filename = videoPath.getFileName().toString();
            Map<String, String> videoInfo = new HashMap<>();
            videoInfo.put("filename", filename);
            videoList.add(videoInfo);
        });

        return ResponseEntity.ok(videoList);
    }
}