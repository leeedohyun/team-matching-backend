package server.teammatching.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.teammatching.dto.request.MemberRequestDto;
import server.teammatching.dto.response.MemberResponseDto;
import server.teammatching.entity.Member;
import server.teammatching.repository.MemberRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public MemberResponseDto join(MemberRequestDto request) {
        Member createdMember = Member.createMember(request, passwordEncoder);
        validateDuplicateLoginId(createdMember);
        validateDuplicateNickName(createdMember);
        Member savedMember = memberRepository.save(createdMember);
        return MemberResponseDto.from(savedMember, "회원가입이 성공했습니다.");
    }

    private void validateDuplicateLoginId(Member member) {
        if (memberRepository.existsByLoginId(member.getLoginId())) {
            throw new IllegalStateException("다른 회원이 사용 중인 아이디입니다.");
        }
    }

    private void validateDuplicateNickName(Member member) {
        if (memberRepository.existsByNickName(member.getNickName())) {
            throw new IllegalStateException("다른 회원이 사용 중인 닉네임입니다.");
        }
    }
}
