package server.teammatching.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import server.teammatching.entity.Member;
import server.teammatching.entity.Post;
import server.teammatching.entity.PostType;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findByType(PostType postType);

    List<Post> findByLeaderAndType(Member leader, PostType postType);

    Optional<Post> findByIdAndType(Long postId, PostType postType);

    Optional<Post> findByIdAndLeader_LoginId(Long postId, String loginId);

    void deleteByIdAndLeader_LoginId(Long postId, String loginId);
}
