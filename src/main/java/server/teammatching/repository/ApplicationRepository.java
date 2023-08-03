package server.teammatching.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import server.teammatching.entity.Application;
import server.teammatching.entity.Member;
import server.teammatching.entity.Post;

import java.util.List;
import java.util.Optional;

public interface ApplicationRepository extends JpaRepository<Application, Long> {

    List<Application> findByAppliedMember(Member appliedMember);

    List<Application> findByPost(Post post);

    Optional<Application> findByAppliedMemberAndPost(Member appliedMember, Post post);
}
