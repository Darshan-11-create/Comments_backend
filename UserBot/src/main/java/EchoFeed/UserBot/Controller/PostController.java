package EchoFeed.UserBot.Controller;
import EchoFeed.UserBot.Creater.Post;
import EchoFeed.UserBot.Service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class PostController {
   @Autowired
    private PostService postService;
   @PostMapping("/posts")
    public Post createPost(@RequestBody Post post){
       postService.createPost(post);
       if(post.getAuthor()==null)
           return null;
       return post;
   }
   @GetMapping("/posts/{id}")
    public Post getPost(@PathVariable Long id){
       return postService.getPostById(id);
   }
   @GetMapping("/posts/all")
    public List<Post> getAllPosts(){
       return postService.getAll();
   }
   @DeleteMapping("/posts/{id}")
    public void deletePost(@PathVariable Long id){
       postService.deletePost(id);
   }
}
