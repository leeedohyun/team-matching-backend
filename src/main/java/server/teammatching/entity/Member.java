package server.teammatching.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    public Member(final String loginId, final String password, final String email, final String nickName,
                  final String university) {
        this.loginId = loginId;
        this.password = password;
        this.email = email;
        this.nickName = nickName;
        this.university = university;
        this.role = Role.USER;
    }

    public void updateEmail(final String email) {
        if (email != null && !email.isEmpty()) {
            this.email = email;
        }
    }

    public void updateNickName(final String nickName) {
        if (nickName != null && !nickName.isEmpty()) {
            this.nickName = nickName;
        }
    }

    public void updateUniversity(final String university) {
        if (university != null && !university.isEmpty()) {
            this.university = university;
        }
    }
}
