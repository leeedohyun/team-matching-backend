package server.teammatching.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private int recruitNumber;

    @Column(nullable = false)
    private String field;

    @ColumnDefault("0")
    private int views;

    @Enumerated(EnumType.STRING)
    private PostStatus status;

    @ColumnDefault("0")
    private int likeNumber;

    private int designerNumber;

    private int frontendNumber;

    private int backendNumber;

    @Enumerated(EnumType.STRING)
    private PostType type;

    @Lob
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member leader;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "post", cascade = CascadeType.ALL)
    private Recruitment recruitment;
}
