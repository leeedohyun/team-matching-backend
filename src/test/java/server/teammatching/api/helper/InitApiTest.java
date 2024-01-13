package server.teammatching.api.helper;

import static server.teammatching.api.steps.AuthSteps.기본_로그인;
import static server.teammatching.api.steps.ProjectSteps.기본_프로젝트;

import org.junit.jupiter.api.BeforeEach;

import server.teammatching.api.steps.MemberSteps;

public class InitApiTest extends ApiTest {

    protected static final String 빈_세션 = "";

    protected static String 기본_회원_아이디;
    protected static String 기본_세션;
    protected static Long 프로젝트_아이디;

    @BeforeEach
    void init() {
        기본_회원_아이디 = MemberSteps.기본_회원_가입();
        기본_세션 = 기본_로그인();
        프로젝트_아이디 = 기본_프로젝트();
    }
}
