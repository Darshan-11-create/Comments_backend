package EchoFeed.UserBot.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class ViralityService {
    @Autowired
    private StringRedisTemplate redisTemplate;
    public void updateVirality(Long postId,String type){
        int value=0;
        switch(type){
            case "BotReply":value=1;
               break;
            case "HumanLike":value=20;
                 break;
            case "HumanComment":value=50;
             break;
            }
        String key="postId:"+postId+":virality";
        redisTemplate.opsForValue().increment(key,value);
    }
}
