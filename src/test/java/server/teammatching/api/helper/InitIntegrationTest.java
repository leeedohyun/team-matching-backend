package server.teammatching.api.helper;

import static server.teammatching.api.steps.AuthSteps.기본_로그인;

import org.junit.jupiter.api.BeforeEach;

import server.teammatching.api.steps.MemberSteps;

public class InitIntegrationTest extends IntegrationTest {

    protected static String 기본_회원_아이디;
    protected static String 기본_세션;

    @BeforeEach
    void init() {
        기본_회원_아이디 = MemberSteps.기본_회원_가입();
        기본_세션 = 기본_로그인();
    }
}
