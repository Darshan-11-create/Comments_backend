package EchoFeed.UserBot.Service;
import EchoFeed.UserBot.Creater.BotUser;
import EchoFeed.UserBot.Creater.Comment;
import EchoFeed.UserBot.Creater.Creater;
import EchoFeed.UserBot.Creater.Post;
import EchoFeed.UserBot.Repositorys.CommentRepository;
import EchoFeed.UserBot.Repositorys.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.time.Duration;
import java.util.List;
import java.util.Set;

@EnableScheduling
@Service
public class CheckerService {
 @Autowired
    private StringRedisTemplate redisTemplate;
 @Autowired
   private CommentRepository commentRepository;
 @Autowired
  private PostRepository postRepository;
  public ResponseEntity<?> checkBotReplies(Long postId,Comment comment,Creater  creater,BotUser botUser){
      String key="postId:"+postId+":Bot_count";

      ResponseEntity<?> response=checkDepth(comment);
      if(response.getStatusCode().value()!=200){
          return response;
      }

      if(comment.getParent_comment()!=null) {
          Comment comment1 = commentRepository.findById(comment.getParent_comment().getId()).orElseThrow(() -> new RuntimeException("Comment not found"));

          Creater ParentCreater=null;
          BotUser ParentBotUser =null;
          if(comment1.getAuthor()==null)
              ParentBotUser=comment1.getBotUser();
          else
              ParentCreater=comment1.getAuthor();
          if ((ParentCreater!=null && creater!=null && ParentCreater.getId().equals(creater.getId())) || (ParentBotUser !=null && botUser !=null && ParentBotUser.getId().equals( botUser.getId()))) {

              return ResponseEntity.status(429).body("You cannot reply to your own comment");
          }

          if(ParentBotUser !=null && creater!=null)
              response=coolDown(creater.getId(), ParentBotUser.getId());
          else if(ParentCreater!=null && botUser !=null)
              response=coolDown(ParentCreater.getId(), botUser.getId());
          if (response!=null && response.getStatusCode().value() != 200) {

              return response;
          }

      }
      Long count=redisTemplate.opsForValue().increment(key,1);
      if(count!=null && count>100){
          redisTemplate.opsForValue().decrement(key,1);
          return ResponseEntity.status(429).body("Too many replies");
      }
      Post post=postRepository.findById(postId).orElseThrow(()->new RuntimeException("Post not found"));
      if(post.getAuthor()!=null && botUser!=null && post.getAuthor().getId()!=null && botUser.getId()!=null) {
          System.out.println("Checking for notifications");
          CheckNotification(post.getAuthor().getId(),botUser.getId());
      }
      return ResponseEntity.status(200).body("Reply added");
  }




  //----------------------------------------------------------------

    public ResponseEntity<?> checkDepth(Comment comment){
      if(comment.getParent_comment()==null)
           return ResponseEntity.ok("comment added");
      Comment parent=commentRepository.findById(comment.getParent_comment().getId()).orElseThrow(()->new RuntimeException("Paarent not found"));
      int depth=0;
      depth= parent.getDepth_level();
      System.out.println("I am from check depth"+depth);
      if(depth+1>20){
          return ResponseEntity.status(429).body("Comment depth limit exceeded");
      }
      return ResponseEntity.ok("Comment added");
  }


  //--------------------------------------------------------------------

    public ResponseEntity<?> coolDown(Long authorId,Long botId){
      String key="cooldown:  "+Math.min(authorId,botId)+":"+Math.max(authorId,botId);
      if(redisTemplate.hasKey(key)){
          return ResponseEntity.status(429).body("Too many interactions between bot and user");
      }

      System.out.println("Updating cooldown    "+key);
      redisTemplate.opsForValue().set(key,"1",Duration.ofMinutes(10));

      return ResponseEntity.status(200).body("Interaction allowed");
  }


  //------------------------------------------------------------------

    public ResponseEntity<?> CheckNotification(Long userId,Long botId){
      String key1="user:"+userId+":pending_notifs";
      String coolDownKey="userId:"+userId+":cooldown";
      if(redisTemplate.hasKey(coolDownKey)){
          String push="Bot:"+botId+":replied to your post";
          redisTemplate.opsForList().rightPush(key1,push);
      }
      else{
          System.out.println("Sending notification to user:  "+userId);
          redisTemplate.opsForValue().set(coolDownKey,"Sending notification to user",Duration.ofMinutes(15));
      }
      return ResponseEntity.ok("Notification processed");
    }


    //------------------------------------------------------------------

    @Scheduled(fixedRate = 5*60*1000)
    public void sendPendingNotifications(){
        Set<String>pendingNotifications=redisTemplate.keys("user:*:pending_notifs");
        if(pendingNotifications==null || pendingNotifications.isEmpty()){
            return;
        }
        for(String key:pendingNotifications){
            List<String>messages=redisTemplate.opsForList().range(key,0,-1);
            if(messages==null || messages.isEmpty())
                continue;
            int count=messages.size();
            String userId=key.split(":")[1];
            String firstBot=messages.get(0).split(":")[1];
            System.out.print("Summarizing Push Notification:  "+"Bot "+firstBot);
            if(count-1>0){
                System.out.print(" and "+(count-1)+" others interacted with your post");
            }
            System.out.println();
            redisTemplate.delete(key);
        }

    }
}
