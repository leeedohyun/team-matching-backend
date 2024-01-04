package server.teammatching.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import server.teammatching.entity.Member;
import server.teammatching.repository.ApplicationRepository;
import server.teammatching.repository.MemberRepository;
import server.teammatching.repository.PostRepository;
import server.teammatching.repository.RecruitmentRepository;

@ExtendWith(MockitoExtension.class)
public class PostServiceTest {

    @Mock
    protected ApplicationRepository applicationRepository;

    @Mock
    protected MemberRepository memberRepository;

    @Mock
    protected PostRepository postRepository;

    @Mock
    protected RecruitmentRepository recruitmentRepository;

    protected Member leader;

    @BeforeEach
    void setUp() {
        leader = Member.builder()
                .id(1L)
                .nickName("닉네임")
                .loginId("id")
                .build();
    }
}
