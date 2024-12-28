package io.github.random.code.space.video.streaming.Service;

import io.github.random.code.space.video.streaming.model.UserHistory;
import io.github.random.code.space.video.streaming.repository.UserHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class HistoryService {

    @Autowired
    private UserHistoryRepository userHistoryRepository;

    public void saveHistory(String username, String videoName) {
        userHistoryRepository.save(UserHistory.builder().username(username).videoName(videoName).build());
    }

    public List<UserHistory> getHistory(String username) {
        List<UserHistory> history = new ArrayList<>();
        userHistoryRepository.findAllByUsername(username).forEach(userHistory -> {
            if (history.stream().noneMatch(historyItem -> historyItem.getVideoName().equals(userHistory.getVideoName()))) {
                history.add(userHistory);
            }
        });
        return history;
    }
}
