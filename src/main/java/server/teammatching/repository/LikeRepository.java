package server.teammatching.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import server.teammatching.entity.Like;
import server.teammatching.entity.Member;
import server.teammatching.entity.Post;

public interface LikeRepository extends JpaRepository<Like, Long> {

    Optional<Like> findByLikedMemberAndPost(final Member likedMember, final Post post);

    List<Like> findByLikedMember(final Member likedMember);
}
