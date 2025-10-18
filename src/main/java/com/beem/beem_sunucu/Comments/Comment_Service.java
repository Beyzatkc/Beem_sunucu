package com.beem.beem_sunucu.Comments;

import com.beem.beem_sunucu.Posts.Post;
import com.beem.beem_sunucu.Posts.Post_DTO_Request;
import com.beem.beem_sunucu.Posts.Post_DTO_Response;
import com.beem.beem_sunucu.Posts.Post_Repo;
import com.beem.beem_sunucu.Users.CustomExceptions;
import com.beem.beem_sunucu.Users.User;
import com.beem.beem_sunucu.Users.User_Repo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class Comment_Service {
    private final Comment_Repo commentRepo;
    private final User_Repo userRepo;
    private final Post_Repo postRepo;

    public Comment_Service(Comment_Repo commentRepo, User_Repo userRepo, Post_Repo postRepo) {
        this.commentRepo = commentRepo;
        this.userRepo = userRepo;
        this.postRepo = postRepo;
    }
    @Transactional
    public Comment_DTO_Response commentCreate(Comment_DTO_Request commentDtoRequest){
        Optional<User> user = userRepo.findById(commentDtoRequest.getUserId());
        Optional<Post>post=postRepo.findById(commentDtoRequest.getPostId());
        if (user.isEmpty()) {
            throw new CustomExceptions.AuthenticationException("Kullanıcı bulunamadı.");
        }
        Comment comment=new Comment();
        comment.setUser(user.get());
        comment.setContents(commentDtoRequest.getContents());
        comment.setCommentDate(LocalDateTime.now());
        comment.setNumberofLikes(0);
        comment.setPost(post.get());
        comment.setSubComments(new ArrayList<>());
        commentRepo.save(comment);
        return new Comment_DTO_Response(comment);
    }

    @Transactional
    public List<Comment_DTO_Response>comments(Long postId, int page, int size){
        Page<Comment>comments=commentRepo.findByPost_PostIdOrderByCommentDateDesc(postId, PageRequest.of(page, size));
        return null;
    }
}