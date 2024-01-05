package server.teammatching.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import server.teammatching.entity.Application;
import server.teammatching.entity.Member;
import server.teammatching.entity.Post;

public interface ApplicationRepository extends JpaRepository<Application, Long> {

    List<Application> findByAppliedMember(Member appliedMember);

    List<Application> findByPost(Post post);

    Optional<Application> findByAppliedMemberAndPost(Member appliedMember, Post post);

    void deleteByIdAndAppliedMember_LoginId(Long applicationId, String loginId);
}
