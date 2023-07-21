package server.teammatching.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import server.teammatching.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Member save(Member member);
}
