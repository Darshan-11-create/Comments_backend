package EchoFeed.UserBot.Creater;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name="post_id")
    private Post post;
   @ManyToOne
   @JoinColumn(name="author_id")
    private Creater author;
   @ManyToOne
   @JoinColumn(name="bot_id")
    private BotUser botUser;
    private String content;
    private int depth_level;
    @ManyToOne
    @JoinColumn(name="parent_comment_id")
    @JsonBackReference
    private Comment parent_comment;
    @OneToMany(mappedBy = "parent_comment", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Comment> replies;
    private LocalDateTime created_at;

    public Long getId() {
        return id;
    }


    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
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

    public int getDepth_level() {
        return depth_level;
    }

    public Comment getParent_comment() {
        return parent_comment;
    }

    public void setParent_comment(Comment parent_comment) {
        this.parent_comment = parent_comment;
        if(parent_comment==null){
            this.depth_level=0;
        }
        else
            this.depth_level=parent_comment.getDepth_level()+1;
    }

    public BotUser getBotUser() {
        return botUser;
    }

    public void setBotUser(BotUser botUser) {
        this.botUser = botUser;
    }

    public List<Comment> getReplies() {
        return replies;
    }

    public void setReplies(List<Comment> replies) {
        this.replies = replies;
    }

    public LocalDateTime getCreated_at() {
        return created_at;
    }
    @PrePersist
   public void created(){
        this.created_at=LocalDateTime.now();
   }
}
