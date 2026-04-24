package EchoFeed.UserBot.Repositorys;

import EchoFeed.UserBot.Creater.Creater;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CreaterRepository extends JpaRepository<Creater,Long> {
}
