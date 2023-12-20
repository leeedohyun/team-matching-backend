package server.teammatching.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
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
                .orElseThrow(ApplicationNotFoundException::new);

        return ResumeResponseDto.from(application);
    }
}
