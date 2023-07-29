package server.teammatching.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import server.teammatching.entity.Application;

public interface ApplicationRepository extends JpaRepository<Application, Long> {
}
