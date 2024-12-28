package io.github.random.code.space.video.streaming.repository;

import io.github.random.code.space.video.streaming.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
}