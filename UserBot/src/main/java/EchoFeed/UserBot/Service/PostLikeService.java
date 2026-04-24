package EchoFeed.UserBot.Service;

import EchoFeed.UserBot.Creater.BotUser;
import EchoFeed.UserBot.Creater.Creater;
import EchoFeed.UserBot.Creater.Post;
import EchoFeed.UserBot.Creater.PostLike;
import EchoFeed.UserBot.Repositorys.BotRepository;
import EchoFeed.UserBot.Repositorys.CreaterRepository;
import EchoFeed.UserBot.Repositorys.PostLikeRepository;
import EchoFeed.UserBot.Repositorys.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PostLikeService {
  @Autowired
    private PostLikeRepository postLikeRepository;
  @Autowired
   private PostRepository postRepository;
  @Autowired
   private CreaterRepository createrRepository;
  @Autowired
   private BotRepository botRepository;
  @Autowired
   private ViralityService viralityService;
  public String addLike(Long postId,Long userId){
      Post post=postRepository.findById(postId).orElseThrow(()->new RuntimeException("Post not found"));
      Creater creater;
      BotUser botUser;
      Optional<PostLike>Liking=postLikeRepository.findByPostIdAndUserId(postId,userId);
      if(Liking.isPresent()){
          postLikeRepository.delete(Liking.get());
          return "UnLiked";
      }
      String res;
      PostLike postLike=new PostLike();
      if(createrRepository.existsById(userId)){
          creater=createrRepository.findById(userId).orElseThrow(()->new RuntimeException("user not found"));
          postLike.setUser(creater);
          res="HumanLike";
      }
      else{
          res="BotLike";
          botUser =botRepository.findById(userId).orElseThrow(()->new RuntimeException("bot not found"));
          postLike.setBot(botUser);
      }
      viralityService.updateVirality(postId,res);

      postLike.setPost(post);
      postLikeRepository.save(postLike);
      return "Liked";
  }
}
