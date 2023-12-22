package server.teammatching.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import server.teammatching.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findById(final Long memberId);

    Optional<Member> findByLoginId(final String loginId);

    boolean existsByLoginId(final String LoginId);

    boolean existsByNickName(final String nickName);
}
