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

import static org.assertj.core.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
class TeamServiceTest {

    @Autowired private TeamService teamService;
    @Autowired private MemberRepository memberRepository;

    @Test
    @DisplayName(value = "팀 생성 게시글 테스트")
    public void 팀_생성_게시글_테스트() throws Exception {
        //given
        TeamAndStudyCreateRequestDto teamAndStudyCreateRequestDto = TeamAndStudyCreateRequestDto.builder()
                .title("OO팀 모집합니다.")
                .field("공모전")
                .recruitNumber(4)
                .content("OO팀 모집합니다. 열심히 참여하실 분들 구합니다.")
                .build();

        Member member = Member.builder()
                .loginId("login")
                .email("dkfdk@gmaiol.com")
                .nickName("member")
                .university("hongik")
                .password("1234")
                .build();

        memberRepository.save(member);

        //when
        TeamAndStudyCreateResponseDto teamAndStudyCreateResponseDto = teamService.create(teamAndStudyCreateRequestDto, member.getId());

        //then
        assertThat(teamAndStudyCreateResponseDto.getTitle()).isEqualTo(teamAndStudyCreateRequestDto.getTitle());
        assertThat(teamAndStudyCreateResponseDto.getMemberId()).isEqualTo(1L);
        assertThat(teamAndStudyCreateResponseDto.getPostId()).isEqualTo(1L);
        assertThat(teamAndStudyCreateResponseDto.getType()).isEqualTo(PostType.TEAM);
        assertThat(teamAndStudyCreateResponseDto.getContent()).isEqualTo(teamAndStudyCreateRequestDto.getContent());
    }
}