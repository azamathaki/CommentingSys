package com.portfolio.app.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.portfolio.app.model.Comment;
import com.portfolio.app.model.Post;
import com.portfolio.app.model.User;
import com.portfolio.app.service.UserService;

@RestController
@RequestMapping("/api")
public class UserController {
    
    @Autowired
    private UserService userService;


    // getting an existing user
    @GetMapping("/users/{id}")
    public User gettingUser(@PathVariable("id") Long id){
        return userService.getUser(id);
    }

    // creating new user
    @PostMapping("/users")
    public User creatingUser(@RequestBody User user){
        return userService.createUser(user);
    }

    // creating new post for an user
    @PostMapping("/users/{id}/posts")
    public User creatingPost(@PathVariable("id") Long id, @RequestBody Post post){
        return userService.createPostForUser(id, post);
    }


    // posting new comment on a post
    @PostMapping("/users/{commentingUserId}/posts/{postId}/comments")
    public User creatingComment(@PathVariable("commentingUserId") Long commentingUserId,
                                @PathVariable("postId") Long postId,
                                @RequestBody Comment comment)
    {
        return userService.createCommentForPost(commentingUserId, postId, comment); 
    }


    // posting new reply comment to an existing comment
    @PostMapping("/users/{userId}/comments/{commentId}")
    public User addingReplaiedComments(@PathVariable("userId") Long userId,
                                                @PathVariable("commentId") Long commentId,
                                                @RequestBody Comment replyComment)
    {
        return userService.addRepliedComments(userId, commentId, replyComment);
    }


    // deleting user's comment by comment id
    @DeleteMapping("/users/{userId}/comments/{commentId}")
    public String deletingComment(@PathVariable("userId") Long userId, @PathVariable("commentId") Long commentId){
        return userService.deleteComment(userId, commentId);
    }


    // deleting user's post by postid
    @DeleteMapping("/users/{userId}/posts/{postId}")
    public String deletingPost(@PathVariable("userId") Long userId, @PathVariable("postId") Long postId){
        return userService.deletePostOfUser(userId, postId);
    }

    // this deletion will erase this user and all details posts and comments
    @DeleteMapping("/users/{id}")
    public String deletingUser(@PathVariable("id") Long id){
        userService.deleteUser(id);
        return "deleted user ID: " + id;
    }

}
