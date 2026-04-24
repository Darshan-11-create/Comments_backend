package EchoFeed.UserBot.Repositorys;

import EchoFeed.UserBot.Creater.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post,Long> {

}
