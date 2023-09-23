package server.teammatching.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Getter
@Table(name = "likes")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Like {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "likes_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private LikeStatus likeStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member likedMember;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @Builder
    public Like(LikeStatus likeStatus, Member likedMember, Post post) {
        this.likeStatus = likeStatus;
        this.post = post;
        setLikedMember(likedMember);
    }

    public static Like getnerateLike(Member likedMember, Post post) {
        return Like.builder()
                .post(post)
                .likedMember(likedMember)
                .likeStatus(LikeStatus.LIKE)
                .build();
    }

    public void setLikedMember(Member likedMember) {
        this.likedMember = likedMember;
        likedMember.getLikes().add(this);
    }
}
