package server.teammatching.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import server.teammatching.dto.request.TeamAndStudyCreateRequestDto;
import server.teammatching.dto.response.TeamAndStudyCreateResponseDto;
import server.teammatching.entity.Member;
import server.teammatching.entity.PostType;
import server.teammatching.repository.MemberRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
class StudyServiceTest {

    @Autowired private StudyService studyService;
    @Autowired private MemberRepository memberRepository;

    @Test
    @DisplayName(value = "스터디 생성 테스트")
    public void 스터디_생성_테스트() throws Exception {
        //given
        Member member = Member.builder()
                .loginId("login")
                .email("dkfdk@gmaiol.com")
                .nickName("member")
                .university("hongik")
                .password("1234")
                .build();

        memberRepository.save(member);

        TeamAndStudyCreateRequestDto teamAndStudyCreateRequestDto = TeamAndStudyCreateRequestDto.builder()
                .title("OO 스터디 모집합니다.")
                .recruitNumber(4)
                .content("OO 스터디 모집합니다. 열심히 참여하실 분들 구합니다.")
                .build();

        //when
        TeamAndStudyCreateResponseDto responseDto = studyService.create(teamAndStudyCreateRequestDto, member.getLoginId());

        //then
        assertThat(responseDto.getTitle()).isEqualTo(teamAndStudyCreateRequestDto.getTitle());
        assertThat(responseDto.getNickName()).isEqualTo("member");
        assertThat(responseDto.getPostId()).isEqualTo(1L);
        assertThat(responseDto.getType()).isEqualTo(PostType.STUDY);
        assertThat(responseDto.getContent()).isEqualTo(teamAndStudyCreateRequestDto.getContent());
    }
}