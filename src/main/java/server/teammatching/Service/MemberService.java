package server.teammatching.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.teammatching.dto.MemberRequest;
import server.teammatching.entity.Member;
import server.teammatching.repository.MemberRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;

    public Member join(MemberRequest request) {
        return memberRepository.save(request.toEntity());
    }
}
