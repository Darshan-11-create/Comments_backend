package EchoFeed.UserBot.Controller;

import EchoFeed.UserBot.Creater.CreaterBotDto;
import EchoFeed.UserBot.Service.CreaterBotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class CreaterBotController {
  @Autowired
    private CreaterBotService createrBotService;
  @PostMapping("/addUser")
    public String AddCreater(@RequestBody CreaterBotDto createrBotDto){
      return createrBotService.AddCreaterBot(createrBotDto);
  }
  @DeleteMapping("/user/{id}")
  public String DeleteCreater(@PathVariable Long id){
      return createrBotService.DeleteCreaterBot(id);
  }
}
