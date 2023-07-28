package server.teammatching.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import server.teammatching.entity.Post;
import server.teammatching.entity.PostType;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findByType(PostType postType);
}
