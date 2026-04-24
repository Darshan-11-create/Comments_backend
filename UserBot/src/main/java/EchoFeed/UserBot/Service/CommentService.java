package EchoFeed.UserBot.Service;

import EchoFeed.UserBot.Creater.Comment;
import EchoFeed.UserBot.Creater.Post;
import EchoFeed.UserBot.Repositorys.CommentRepository;
import EchoFeed.UserBot.Repositorys.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentService {
  @Autowired
    private CommentRepository commentRepository;
  @Autowired
   private PostRepository postRepository;
  public void addComment(Long postId,Comment comment){
     Post post=postRepository.findById(postId).orElseThrow(()->new RuntimeException("Post not found"));
     if(comment.getParent_comment()!=null && comment.getParent_comment().getId()!=null){
         Comment parent=commentRepository.findById(comment.getParent_comment().getId()).orElseThrow(()->new RuntimeException("parent not found"));
         comment.setParent_comment(parent);
     }
     comment.setPost(post);
      commentRepository.save(comment);
  }
  public void deleteComment(Long id){
      commentRepository.deleteById(id);
  }

}
