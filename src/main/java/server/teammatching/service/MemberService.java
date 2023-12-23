package server.teammatching.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import server.teammatching.auth.AuthenticationUtils;
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
    public MemberResponseDto findOne(String memberId, String authenticatedId) {
        AuthenticationUtils.verifyLoggedInUser(memberId, authenticatedId);
        Member findMember = memberRepository.findByLoginId(memberId)
                .orElseThrow(MemberNotFoundException::new);

        return MemberResponseDto.from(findMember);
    }

    @Transactional(readOnly = true)
    public List<MemberResponseDto> findAll() {
        List<Member> findMembers = memberRepository.findAll();

        return findMembers.stream()
                .map(MemberResponseDto::from)
                .collect(Collectors.toList());
    }

    public MemberUpdateResponseDto update(String memberId, MemberUpdateRequestDto updateRequest,
                                          String authenticatedId) {
        AuthenticationUtils.verifyLoggedInUser(memberId, authenticatedId);
        Member findMember = memberRepository.findByLoginId(memberId)
                .orElseThrow(MemberNotFoundException::new);

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
