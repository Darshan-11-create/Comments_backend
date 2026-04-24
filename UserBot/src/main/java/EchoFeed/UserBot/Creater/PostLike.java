package EchoFeed.UserBot.Creater;

import jakarta.persistence.*;
@Entity
public class PostLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="user_id")
    private Creater user;
    @ManyToOne
    @JoinColumn(name="bot_id")
     private BotUser botUser;
    @ManyToOne
    @JoinColumn(name="post_id")
    private Post post;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Creater getUser() {
        return user;
    }

    public void setUser(Creater user) {
        this.user = user;
    }

    public Post getPost() {
        return post;
    }

    public BotUser getBot() {
        return botUser;
    }

    public void setBot(BotUser botUser) {
        this.botUser = botUser;
    }

    public void setPost(Post post) {
        this.post = post;
    }
}
