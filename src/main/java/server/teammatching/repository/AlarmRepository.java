package server.teammatching.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import server.teammatching.entity.Alarm;

public interface AlarmRepository extends JpaRepository<Alarm, Long> {
}
