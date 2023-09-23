package server.teammatching.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import server.teammatching.dto.request.MemberRequestDto;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(nullable = false)
    private String loginId;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String nickName;

    private String image;

    @Column(nullable = false)
    private String university;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "leader", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Post> postList = new ArrayList<>();

    @OneToMany(mappedBy = "likedMember", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Like> likes = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Alarm> alarms = new ArrayList<>();

    @Builder
    public Member(String loginId, String password, String email, String nickName, String university, Role role) {
        this.loginId = loginId;
        this.password = password;
        this.email = email;
        this.nickName = nickName;
        this.university = university;
        this.role = role;
    }

    public static Member createMember(MemberRequestDto request, PasswordEncoder passwordEncoder) {
        return Member.builder()
                .loginId(request.getLoginId())
                .password(passwordEncoder.encode(request.getPassword()))
                .nickName(request.getNickName())
                .university(request.getUniversity())
                .email(request.getEmail())
                .role(Role.USER)
                .build();
    }

    public void updateEmail(String email) {
        this.email = email;
    }

    public void updateNickName(String nickName) {
        this.nickName = nickName;
    }

    public void updateUniversity(String university) {
        this.university = university;
    }
}
