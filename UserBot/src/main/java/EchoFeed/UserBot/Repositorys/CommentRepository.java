package EchoFeed.UserBot.Repositorys;

import EchoFeed.UserBot.Creater.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment,Long> {
}
