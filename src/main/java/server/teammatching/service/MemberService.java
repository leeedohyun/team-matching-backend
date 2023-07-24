package server.teammatching.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.teammatching.dto.request.MemberRequestDto;
import server.teammatching.dto.request.MemberUpdateRequestDto;
import server.teammatching.dto.response.MemberResponseDto;
import server.teammatching.dto.response.MemberUpdateResponseDto;
import server.teammatching.entity.Member;
import server.teammatching.repository.MemberRepository;

import java.util.List;

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

    public MemberResponseDto findOne(Long memberId) {
        Member findMember = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 회원입니다."));

        return MemberResponseDto.from(findMember, "조회 성공");
    }

    public List<Member> findAll() {
        return memberRepository.findAll();
    }

    public MemberUpdateResponseDto update(Long memberId, MemberUpdateRequestDto updateRequest) {
        Member findMember = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 회원입니다."));

        if (updateRequest.getUpdatedNickName() != null) {
            findMember.updateNickName(updateRequest.getUpdatedNickName());
        }
        if (updateRequest.getUpdatedEmail() != null) {
            findMember.updateEmail(updateRequest.getUpdatedEmail());
        }
        if (updateRequest.getUpdatedUniversity() != null) {
            findMember.updateUniversity(updateRequest.getUpdatedUniversity());
        }

        memberRepository.save(findMember);
        return MemberUpdateResponseDto.from(findMember, "업데이트 성공");
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
