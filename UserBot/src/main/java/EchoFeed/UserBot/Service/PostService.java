package EchoFeed.UserBot.Service;

import EchoFeed.UserBot.Creater.Post;
import EchoFeed.UserBot.Repositorys.BotRepository;
import EchoFeed.UserBot.Repositorys.CreaterRepository;
import EchoFeed.UserBot.Repositorys.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PostService {
   @Autowired
     private PostRepository postRepository;
   @Autowired
     private CreaterRepository createrRepository;
   @Autowired
     private BotRepository botRepository;
   public void createPost(Post post){
       if(post.getAuthor()==null)
           return;
       else
           post.setAuthor(createrRepository.findById(post.getAuthor().getId()).orElseThrow(()->new RuntimeException("User not found")));
       postRepository.save(post);
   }
   public Post getPostById(Long id){
       return postRepository.findById(id).orElseThrow(()->new RuntimeException("Post not found"));
   }
   public List<Post> getAll(){
       return postRepository.findAll();
   }

   public void deletePost(Long id){
       postRepository.deleteById(id);
   }
}
