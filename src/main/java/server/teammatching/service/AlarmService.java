package server.teammatching.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.teammatching.dto.response.AlarmResponse;
import server.teammatching.entity.Alarm;
import server.teammatching.entity.Application;
import server.teammatching.entity.Member;
import server.teammatching.repository.AlarmRepository;
import server.teammatching.repository.ApplicationRepository;
import server.teammatching.repository.MemberRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AlarmService {
    
    private final AlarmRepository alarmRepository;
    private final ApplicationRepository applicationRepository;
    private final MemberRepository memberRepository;

    public List<AlarmResponse> checkAlarms(Long memberId) {
        Member findMember = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("유효하지 않은 id 입니다."));
        List<Alarm> alarmList = alarmRepository.findByMember(findMember);

        List<AlarmResponse> responses = new ArrayList<>();

        for (Alarm alarm : alarmList) {
            Application application = applicationRepository.findByAppliedMemberAndPost(findMember, alarm.getPost())
                    .orElseThrow(() -> new RuntimeException("유효하지 않은 id 입니다."));
            AlarmResponse alarmResponse = AlarmResponse.builder()
                    .alarmId(alarm.getId())
                    .applicationStatus(application.getStatus())
                    .build();
            responses.add(alarmResponse);
        }
        return responses;
    }
}
