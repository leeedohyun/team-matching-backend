package server.teammatching.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import server.teammatching.entity.Alarm;
import server.teammatching.entity.Member;

public interface AlarmRepository extends JpaRepository<Alarm, Long> {

    List<Alarm> findByMember(final Member member);
}
