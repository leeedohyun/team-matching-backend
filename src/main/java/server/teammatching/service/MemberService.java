package server.teammatching.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import server.teammatching.dto.request.MemberRequestDto;
import server.teammatching.dto.request.MemberUpdateRequestDto;
import server.teammatching.dto.response.MemberResponseDto;
import server.teammatching.dto.response.MemberUpdateResponseDto;
import server.teammatching.entity.Member;
import server.teammatching.exception.DuplicateResourceException;
import server.teammatching.exception.MemberNotFoundException;
import server.teammatching.repository.MemberRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public MemberResponseDto join(final MemberRequestDto request) {
        final String encryptedPassword = passwordEncoder.encode(request.getPassword());
        final Member createdMember = Member.builder()
                .university(request.getUniversity())
                .nickName(request.getNickName())
                .email(request.getEmail())
                .loginId(request.getLoginId())
                .password(encryptedPassword)
                .build();
        validateDuplicateLoginId(createdMember);
        validateDuplicateNickName(createdMember);
        final Member savedMember = memberRepository.save(createdMember);

        return MemberResponseDto.from(savedMember);
    }

    @Transactional(readOnly = true)
    public MemberResponseDto findOne(final String memberId) {
        final Member findMember = memberRepository.findByLoginId(memberId)
                .orElseThrow(MemberNotFoundException::new);

        return MemberResponseDto.from(findMember);
    }

    public MemberUpdateResponseDto update(final String memberId, final MemberUpdateRequestDto updateRequest) {
        final Member findMember = memberRepository.findByLoginId(memberId)
                .orElseThrow(MemberNotFoundException::new);

        findMember.updateUniversity(updateRequest.getUpdatedUniversity());
        findMember.updateEmail(updateRequest.getUpdatedEmail());
        findMember.updateNickName(updateRequest.getUpdatedNickName());

        memberRepository.save(findMember);
        return MemberUpdateResponseDto.from(findMember, "업데이트 성공");
    }

    public void delete(final String memberId) {
        final Member findMember = memberRepository.findByLoginId(memberId)
                .orElseThrow(MemberNotFoundException::new);
        memberRepository.delete(findMember);
    }

    private void validateDuplicateLoginId(Member member) {
        if (memberRepository.existsByLoginId(member.getLoginId())) {
            throw new DuplicateResourceException("id", member.getLoginId());
        }
    }

    private void validateDuplicateNickName(Member member) {
        if (memberRepository.existsByNickName(member.getNickName())) {
            throw new DuplicateResourceException("Nickname", member.getNickName());
        }
    }
}
