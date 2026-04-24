package EchoFeed.UserBot.Repositorys;

import EchoFeed.UserBot.Creater.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostLikeRepository extends JpaRepository<PostLike,Long> {
    Optional<PostLike>findByPostIdAndUserId(Long postId,Long userId);
}
