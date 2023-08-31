package server.teammatching.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.teammatching.dto.response.ApplicantAlarmResponse;
import server.teammatching.dto.response.LeaderAlarmResponse;
import server.teammatching.entity.Alarm;
import server.teammatching.entity.Application;
import server.teammatching.entity.Member;
import server.teammatching.exception.ApplicationNotFoundException;
import server.teammatching.exception.MemberNotFoundException;
import server.teammatching.repository.AlarmRepository;
import server.teammatching.repository.ApplicationRepository;
import server.teammatching.repository.MemberRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AlarmService {
    
    private final AlarmRepository alarmRepository;
    private final ApplicationRepository applicationRepository;
    private final MemberRepository memberRepository;

    public List<ApplicantAlarmResponse> checkApplicantAlarms(String memberId) {
        Member findMember = memberRepository.findByLoginId(memberId)
                .orElseThrow(() -> new MemberNotFoundException("유효하지 않은 id 입니다."));
        List<Alarm> alarmList = alarmRepository.findByMember(findMember);

        List<ApplicantAlarmResponse> responses = new ArrayList<>();

        for (Alarm alarm : alarmList) {
            if (applicationRepository.findByAppliedMemberAndPost(findMember, alarm.getPost()).isPresent()) {
                Application application = applicationRepository.findByAppliedMemberAndPost(findMember, alarm.getPost())
                        .orElseThrow(() -> new ApplicationNotFoundException("유효하지 않은 id 입니다."));
                ApplicantAlarmResponse applicantAlarmResponse = ApplicantAlarmResponse.builder()
                        .alarmId(alarm.getId())
                        .applicationStatus(application.getStatus())
                        .title(alarm.getPost().getTitle())
                        .build();
                responses.add(applicantAlarmResponse);
            }
        }
        return responses;
    }

    public List<LeaderAlarmResponse> checkLeaderAlarms(String memberId) {
        Member findMember = memberRepository.findByLoginId(memberId)
                .orElseThrow(() -> new MemberNotFoundException("유효하지 않은 id 입니다."));
        List<Alarm> alarmList = alarmRepository.findByMember(findMember);

        List<LeaderAlarmResponse> responses = new ArrayList<>();

        for (Alarm alarm : alarmList) {
            if (applicationRepository.findByAppliedMemberAndPost(findMember, alarm.getPost()).isPresent()) {
                LeaderAlarmResponse leaderAlarmResponse = LeaderAlarmResponse.builder()
                        .alarmId(alarm.getId())
                        .title(alarm.getPost().getTitle())
                        .build();
                responses.add(leaderAlarmResponse);
            }
        }
        return responses;
    }
}
