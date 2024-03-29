package server.teammatching.entity;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MemberTest {

    @Test
    @DisplayName("이메일이 null이면 수정되지 않는다.")
    void notUpdateEmailByNull() {
        assertEmailUpdate("abc@gmail.com", null, "abc@gmail.com");
    }

    @Test
    @DisplayName("이메일이 빈 문자열이면 수정되지 않는다.")
    void notUpdateEmailByEmptyString() {
        assertEmailUpdate("abd@gmail.com", "", "abd@gmail.com");
    }

    @Test
    @DisplayName("이메일이 null이나 빈 문자열이 아니면 수정된다.")
    void updateEmail() {
        assertEmailUpdate("abd@gmail.com", "efg@naver.com", "efg@naver.com");
    }

    @Test
    @DisplayName("닉네임이 null이면 수정되지 않는다.")
    void notUpdateNickNameByNull() {
        assertNickNameUpdate("hyun", null, "hyun");
    }

    @Test
    @DisplayName("닉네임이 빈 문자열이면 수정되지 않는다.")
    void notUpdateNickNameByEmptyString() {
        assertNickNameUpdate("do", "", "do");
    }

    @Test
    @DisplayName("닉네임이 null이거나 빈 문자열이 아니면 수정된다.")
    void updateNickName() {
        assertNickNameUpdate("lee", "LEE", "LEE");
    }

    @Test
    @DisplayName("대학교가 null이면 수정되지 않는다.")
    void notUpdateUniversityByNull() {
        assertNickNameUpdate("홍익대학교", null, "홍익대학교");
    }

    @Test
    @DisplayName("대학교가 빈 문자열이면 수정되지 않는다.")
    void notUpdateUniversityByEmptyString() {
        assertUniversityUpdate("홍익대학교", "", "홍익대학교");
    }

    @Test
    @DisplayName("대학교가 null이거나 빈 문자열이 아니면 수정된다.")
    void updateUniversity() {
        assertNickNameUpdate("홍익대학교", "서울대학교", "서울대학교");
    }

    @Test
    void 알람_추가() {
        // given
        final Member leader = Member.builder()
                .id(1L)
                .build();

        final Post team = Post.createTeam("title", "content", 1, leader);

        // when
        Alarm.createAlarm(leader, team);

        // then
        assertThat(leader.getAlarms()).hasSize(1);
    }

    private void assertEmailUpdate(final String email, final String emailToUpdate, final String expected) {
        // given
        final Member member = Member.builder()
                .password("1234")
                .email(email)
                .build();

        // when
        member.updateEmail(emailToUpdate);

        // then
        assertThat(member.getEmail()).isEqualTo(expected);
    }

    private void assertNickNameUpdate(final String nickName, final String nickNameToUpdate, final String expected) {
        // given
        final Member member = Member.builder()
                .password("1234")
                .nickName(nickName)
                .build();
        // when
        member.updateNickName(nickNameToUpdate);

        // then
        assertThat(member.getNickName()).isEqualTo(expected);
    }

    private void assertUniversityUpdate(final String university, final String universityToUpdate,
                                        final String expected) {
        // given
        final Member member = Member.builder()
                .password("1234")
                .university(university)
                .build();

        // when
        member.updateUniversity(universityToUpdate);

        // then
        assertThat(member.getUniversity()).isEqualTo(expected);
    }
}
