package com.portfolio.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.portfolio.app.model.Comment;
import com.portfolio.app.model.Post;
import com.portfolio.app.model.User;
import com.portfolio.app.repository.CommentRepository;
import com.portfolio.app.repository.PostRepository;
import com.portfolio.app.repository.UserRepository;

@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PasswordEncoder encoder;

    // service things down below

    public User getUser(Long userId){
        User user = userRepository.findById(userId).orElseThrow(()-> new RuntimeException("user not found by given ID: " + userId));
        return user;
    }


    public User createUser(User user){
        if (user == null){
            throw new RuntimeException("user object is null!");
        }
        user.setPassword(encoder.encode(user.getPassword()));
        return userRepository.save(user);
    }


    public User createPostForUser(Long userId, Post post){
        User user = userRepository.findById(userId).orElseThrow(()-> new RuntimeException("user not found by given ID: " + userId));

        if (post == null){
            throw new RuntimeException("post object is null!");
        }

        user.addPost(post);
        userRepository.save(user);
        return user;
    }


    public User createCommentForPost(Long userId, Long postId, Comment comment){
        User user = userRepository.findById(userId).orElseThrow(()-> new RuntimeException("user not found by given ID: " + userId));
        Post post = postRepository.findById(postId).orElseThrow(()-> new RuntimeException("post not found by given ID: " + postId));

        if (comment == null){
            throw new RuntimeException("comment object is null!");
        }

        post.addComment(comment, userId);
        userRepository.save(user);
        return post.getUser();
    }


    public User addRepliedComments(Long userId, Long commentId, Comment replyComment) {
        // Validate the user
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found by given ID: " + userId));
    
        // Validate the parent comment
        Comment parentComment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found by given ID: " + commentId));

        if (user == null){
            throw new RuntimeException("given user id not exists!!");
        }
    
        // Set the user and parent comment for the reply
        replyComment.setCommentingUserId(userId);
        replyComment.setPost(parentComment.getPost());
        replyComment.setParentComment(parentComment);
        
        // Save the reply comment to the database
        commentRepository.save(replyComment);
    
        // Add the reply to the parent's list of replies
        parentComment.getReplies().add(replyComment);
    
        // Save the parent comment to update its list of replies
        commentRepository.save(parentComment);
    
        // Return the updated list of replies
        return parentComment.getPost().getUser();
    }


    public String deleteComment(Long userId, Long commentId){
        // Validate the user
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found by given ID: " + userId));
    
        // Validate the comment
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found by given ID: " + commentId));

        if (comment.getCommentingUserId() == user.getId()){
            commentRepository.deleteById(commentId);
            return "comment deleted successfully. Deleted comment ID: " + commentId;
        }else {
            return "comment is not belong to this given user ID: " + userId;
        }
    }


    public String deletePostOfUser(Long userId, Long postId){
        // Validate the user
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found by given ID: " + userId));
    
        // Validate the post
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("post not found by given ID: " + postId));

        if (post.getUser().getId() == user.getId()){
            user.removePost(post);
            userRepository.save(user);
            return "post deleted successfully! deleted post ID: " + postId;
        }else {
            return "post is not belong to this given user ID: " + userId;
        }
    
    }
    

    public void deleteUser(Long id){
        userRepository.deleteById(id);
    }

}
