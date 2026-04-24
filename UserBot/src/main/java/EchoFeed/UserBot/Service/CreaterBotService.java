package EchoFeed.UserBot.Service;

import EchoFeed.UserBot.Creater.Creater;
import EchoFeed.UserBot.Creater.CreaterBotDto;
import EchoFeed.UserBot.Repositorys.BotRepository;
import EchoFeed.UserBot.Repositorys.CreaterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreaterBotService {
  @Autowired
    private CreaterRepository createrRepository;
  @Autowired
   private BotRepository botRepository;
  public String AddCreaterBot(CreaterBotDto createrBotDto){
      try {
          if(createrBotDto.getBotUser()==null)
              createrRepository.save(createrBotDto.getCreater());
          else
              botRepository.save(createrBotDto.getBotUser());
      } catch (Exception e) {
          System.out.println(e.fillInStackTrace()+"   "+createrBotDto);
          return "Error Please Try Again";
      }
      return "Added Successfully";
  }
  public String DeleteCreaterBot(Long id){
      try{
          if(createrRepository.existsById(id)){
              createrRepository.deleteById(id);
          }
          else{
              botRepository.deleteById(id);
          }
      } catch (Exception e) {
          return "not found";
      }
      return "Deleted Successfully";
  }
}
