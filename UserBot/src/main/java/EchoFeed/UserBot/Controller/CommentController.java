package EchoFeed.UserBot.Controller;

import EchoFeed.UserBot.Creater.BotUser;
import EchoFeed.UserBot.Creater.Comment;
import EchoFeed.UserBot.Creater.Creater;
import EchoFeed.UserBot.Creater.Post;
import EchoFeed.UserBot.Repositorys.BotRepository;
import EchoFeed.UserBot.Repositorys.CommentRepository;
import EchoFeed.UserBot.Repositorys.CreaterRepository;
import EchoFeed.UserBot.Repositorys.PostRepository;
import EchoFeed.UserBot.Service.CheckerService;
import EchoFeed.UserBot.Service.CommentService;
import EchoFeed.UserBot.Service.ViralityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class CommentController {
  @Autowired
    private CommentService commentService;
  @Autowired
   private PostRepository postRepository;
  @Autowired
   private ViralityService viralityService;
  @Autowired
    private CreaterRepository createrRepository;
  @Autowired
    private CommentRepository commentRepository;
  @Autowired
    private BotRepository botRepository;
  @Autowired
   private CheckerService checkerService;
  @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<?> addComment(@PathVariable Long postId, @RequestBody Comment comment){
    Creater creater=null;
    BotUser botUser =null;
    Post post=postRepository.findById(postId).orElseThrow(()->new RuntimeException("post not found"));
    if(comment.getAuthor()==null && comment.getBotUser()!=null && comment.getBotUser().getId()!=null){
        botUser =botRepository.findById(comment.getBotUser().getId()).orElseThrow(()->new RuntimeException("bot not found"));
        comment.setBotUser(botUser);
    }
    else if(comment.getAuthor()!=null && comment.getAuthor().getId()!=null){
        creater = createrRepository.findById(comment.getAuthor().getId()).orElseThrow(()->new RuntimeException("user not found"));
      comment.setAuthor(creater);
    }
    else{
        return ResponseEntity.status(400).body("Invalid comment either of the bot and user must not be null");
    }
    String res;
    if(creater==null){
          res = "BotReply";
//          comment.setBot(botUser);
      }
      else{
          res="HumanComment";
      }

      ResponseEntity<?>response=null;
      response=checkerService.checkBotReplies(postId,comment,creater,botUser);
        if(response.getStatusCode().value()!=200){
            return response;
        }
        viralityService.updateVirality(postId,res);
      commentService.addComment(postId,comment);
      return ResponseEntity.ok("Comment Added");
  }

  @DeleteMapping("/comments/{id}")
    public void deleteComment(@PathVariable  Long id){
      commentService.deleteComment(id);
  }
}
