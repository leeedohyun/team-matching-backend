package server.teammatching.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import server.teammatching.dto.response.ResumeResponseDto;
import server.teammatching.entity.Application;
import server.teammatching.exception.ApplicationNotFoundException;
import server.teammatching.repository.ApplicationRepository;

@Service
@RequiredArgsConstructor
public class ResumeService {

    private final ApplicationRepository applicationRepository;

    public ResumeResponseDto findResume(Long applicationId) {
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ApplicationNotFoundException("지원이 존재하지 않습니다."));

        return ResumeResponseDto.from(application);
    }
}
