package server.teammatching.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import server.teammatching.entity.Post;

public interface PostRepository extends JpaRepository<Post, Long> {
}
