package EchoFeed.UserBot.Creater;

import jakarta.persistence.*;

import java.time.LocalDateTime;
@Entity
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne
    @JoinColumn(name = "author_id")
    private Creater author;
    @ManyToOne
    @JoinColumn(name="bot_id")
    private BotUser botUser;
    private String content;
    private LocalDateTime created_at;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Creater getAuthor() {
        return author;
    }

    public void setAuthor(Creater author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public BotUser getBotUser() {
        return botUser;
    }

    public void setBotUser(BotUser botUser) {
        this.botUser = botUser;
    }

    public LocalDateTime getCreated_at() {
        return created_at;
    }
    @PrePersist
   public void created(){
        this.created_at=LocalDateTime.now();
   }
}
