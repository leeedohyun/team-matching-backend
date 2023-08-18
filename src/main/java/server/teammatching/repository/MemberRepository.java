package server.teammatching.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import server.teammatching.entity.Member;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Member save(Member member);
    Optional<Member> findById(Long memberId);
    List<Member> findAll();
    Optional<Member> findByLoginId(String loginId);
    boolean existsByLoginId(String LoginId);
    boolean existsByNickName(String nickName);
}
