package server.teammatching.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import server.teammatching.entity.Member;
import server.teammatching.entity.Post;
import server.teammatching.entity.PostType;

public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findByType(final PostType postType);

    List<Post> findByLeaderAndType(final Member leader, final PostType postType);

    Optional<Post> findByIdAndType(final Long postId, final PostType postType);

    Optional<Post> findByIdAndLeader_LoginId(final Long postId, final String loginId);

    void deleteById(final Long postId);
}
