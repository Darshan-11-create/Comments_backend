package EchoFeed.UserBot.Controller;

import EchoFeed.UserBot.Creater.Creater;
import EchoFeed.UserBot.Creater.CreaterBotDto;
import EchoFeed.UserBot.Repositorys.PostLikeRepository;
import EchoFeed.UserBot.Service.PostLikeService;
import EchoFeed.UserBot.Service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class PostLikeController {
    @Autowired
    private PostLikeService postLikeService;
    @PostMapping("/posts/{postId}/like")
    public String likePost(@PathVariable Long postId, @RequestBody CreaterBotDto createrBotDto){
        return postLikeService.addLike(postId,createrBotDto);
    }
}
