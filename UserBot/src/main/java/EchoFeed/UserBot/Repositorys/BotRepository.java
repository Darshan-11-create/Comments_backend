package EchoFeed.UserBot.Repositorys;

import EchoFeed.UserBot.Creater.BotUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BotRepository extends JpaRepository<BotUser,Long> {

}
