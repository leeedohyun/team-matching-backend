package server.teammatching.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import server.teammatching.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findById(Long memberId);
    List<Member> findAll();
    Optional<Member> findByLoginId(String loginId);
    boolean existsByLoginId(String LoginId);
    boolean existsByNickName(String nickName);
}
