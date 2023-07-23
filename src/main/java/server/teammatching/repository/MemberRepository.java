package server.teammatching.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import server.teammatching.entity.Member;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Member save(Member member);
    List<Member> findAll();
    boolean existsByLoginId(String LoginId);
    boolean existsByNickName(String nickName);
}
