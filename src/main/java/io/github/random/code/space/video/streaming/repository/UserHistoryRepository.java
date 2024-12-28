package io.github.random.code.space.video.streaming.repository;

import io.github.random.code.space.video.streaming.model.User;
import io.github.random.code.space.video.streaming.model.UserHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface UserHistoryRepository extends JpaRepository<UserHistory, String> {
    List<UserHistory> findAllByUsername(String username);
}