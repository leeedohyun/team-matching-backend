package server.teammatching.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import server.teammatching.entity.Application;
import server.teammatching.entity.Member;

import java.util.List;

public interface ApplicationRepository extends JpaRepository<Application, Long> {

    List<Application> findByAppliedMember(Member appliedMember);
}
