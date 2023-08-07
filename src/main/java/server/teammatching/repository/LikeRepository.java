package server.teammatching.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import server.teammatching.entity.Like;
import server.teammatching.entity.Member;
import server.teammatching.entity.Post;

import java.util.List;
import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {

    Optional<Like> findByLikedMemberAndPost(Member likedMember, Post post);

    List<Like> findByLikedMember(Member likedMember);
}
