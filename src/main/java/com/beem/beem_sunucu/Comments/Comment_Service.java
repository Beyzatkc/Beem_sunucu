package com.beem.beem_sunucu.Comments;

import com.beem.beem_sunucu.Posts.*;
import com.beem.beem_sunucu.Users.CustomExceptions;
import com.beem.beem_sunucu.Users.User;
import com.beem.beem_sunucu.Users.User_Repo;
import com.beem.beem_sunucu.Users.User_Response_DTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    private final Comment_Like_Repo commentLikeRepo;

    public Comment_Service(Comment_Repo commentRepo, User_Repo userRepo, Post_Repo postRepo, Comment_Like_Repo commentLikeRepo) {
        this.commentRepo = commentRepo;
        this.userRepo = userRepo;
        this.postRepo = postRepo;
        this.commentLikeRepo = commentLikeRepo;
    }
    @Transactional
    public Comment_DTO_Response commentCreate(Comment_DTO_Request commentDtoRequest){
        Optional<User> user = userRepo.findById(commentDtoRequest.getUserId());
        Optional<Post>post=postRepo.findById(commentDtoRequest.getPostId());
        if (user.isEmpty()) {
            throw new CustomExceptions.AuthenticationException("Kullanıcı bulunamadı.");
        }
        if(post.isEmpty()){
            throw new CustomExceptions.AuthenticationException("Post bulunamadı.");
        }
        Comment comment=new Comment();
        comment.setUser(user.get());
        comment.setContents(commentDtoRequest.getContents());
        comment.setCommentDate(LocalDateTime.now());
        comment.setNumberofLikes(0);
        comment.setParentYorum(null);
        comment.setPost(post.get());
        comment.setSubComments(new ArrayList<>());
        commentRepo.save(comment);
        return new Comment_DTO_Response(comment);
    }

    @Transactional
    public List<Comment_DTO_Response>commentsGet(Long postId,Long currentUserId, int page, int size){
        Page<Comment> comments = commentRepo.findByPost_PostIdAndParentCommentIsNullOrderByCommentDateDesc(postId, PageRequest.of(page, size)
        );
        return comments.stream()
                .map(comment -> {
                    Comment_DTO_Response dto = new Comment_DTO_Response(comment);
                    boolean isLiked = commentLikeRepo.existsByComment_CommentIdAndUser_Id(comment.getCommentId(), currentUserId);
                    dto.setLiked(isLiked);
                    return dto;
                })
                .toList();
    }

    @Transactional
    public Comment_DTO_Response createSubComment(Comment_DTO_Request dto){
        User user = userRepo.findById(dto.getUserId())
                .orElseThrow(() -> new CustomExceptions.AuthenticationException("Kullanıcı bulunamadı."));

        Post post = postRepo.findById(dto.getPostId())
                .orElseThrow(() -> new CustomExceptions.NotFoundException("Post bulunamadı."));

        Comment parentComment = commentRepo.findById(dto.getParentCommentId())
                .orElseThrow(() -> new CustomExceptions.NotFoundException("Yorum bulunamadı."));

        Comment reply=new Comment();
        reply.setUser(user);
        reply.setPost(post);
        reply.setContents(dto.getContents());
        reply.setCommentDate(LocalDateTime.now());
        reply.setNumberofLikes(0);
        reply.setParentYorum(parentComment);
        reply.setSubComments(new ArrayList<>());
        commentRepo.save(reply);
        Comment_DTO_Response cdto=new Comment_DTO_Response(reply);
        cdto.setParentCommentUsername(parentComment.getUser().getUsername());
        return cdto;
    }

    @Transactional
    public List<Comment_DTO_Response>subCommentsGet(Long parentCommentId,Long currentUserId, int page, int size){
        Page<Comment>comments=commentRepo.findByParentComment_CommentIdOrderByCommentDateDesc(parentCommentId, PageRequest.of(page, size));
        return comments.stream()
                .map(comment -> {
                    Comment_DTO_Response dto = new Comment_DTO_Response(comment);
                    dto.setParentCommentUsername(comment.getUser().getUsername());
                    boolean isLiked = commentLikeRepo.existsByComment_CommentIdAndUser_Id(comment.getCommentId(), currentUserId);
                    dto.setLiked(isLiked);
                    return dto;
                })
                .toList();
    }

    @Transactional
    public String toggleLike(Long commentId,Long userId){
       Comment comment=commentRepo.findById(commentId)
               .orElseThrow(() -> new CustomExceptions.AuthenticationException("Yorum bulunamadı"));
       User user=userRepo.findById(userId)
               .orElseThrow(() -> new CustomExceptions.AuthenticationException("Kullanıcı bulunamadı"));
       Optional<Comment_Like>existingLike=commentLikeRepo.findByComment_CommentIdAndUser_Id(commentId,userId);
       if(existingLike.isPresent()){
           commentLikeRepo.delete(existingLike.get());

           int newCount = Math.max(0, comment.getNumberofLikes() - 1);
           commentRepo.updateLikeCount(commentId, newCount);

           return "Beğeni kaldırıldı";
       }else{
           Comment_Like commentLike=new Comment_Like();
           commentLike.setComment(comment);
           commentLike.setUser(user);
           commentLikeRepo.saveAndFlush(commentLike);

           int newCount = comment.getNumberofLikes() + 1;
           commentRepo.updateLikeCount(commentId, newCount);

           return "Yorum beğenildi";
       }

    }
    @Transactional
    public List<User_Response_DTO> users_who_like(Long commentId, Long currentUserId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Comment_Like> commentas_like_page = commentLikeRepo.findCommentLikesWithFollowOrder(commentId, currentUserId, pageable);
        List<Comment_Like> comments_like = commentas_like_page.getContent();
        return comments_like.stream().map(commentLike -> new User_Response_DTO(commentLike.getUser())).toList();
    }

    @Transactional
    public void removeComment(Long commentId,Long userId){
        Comment comment=commentRepo.findById(commentId)
                .orElseThrow(() -> new CustomExceptions.NotFoundException("Yorum bulunamadı."));
        if(userId==null){
            throw new CustomExceptions.AuthorizationException("Kullanıcı Bulunamadı");
        }
        if(!comment.getUser().getId().equals(userId)){
            throw new CustomExceptions.NotFoundException("Bu yorumu silme yetkiniz yok.");
        }
       commentRepo.delete(comment);
    }

    @Transactional
    public void updateComment(Long commentId,Long userId,Comment_DTO_Update commentDtoUpdate){
        Comment comment=commentRepo.findById(commentId)
                .orElseThrow(() -> new CustomExceptions.NotFoundException("Yorum bulunamadı."));
        if(userId==null){
            throw new CustomExceptions.AuthorizationException("Kullanıcı Bulunamadı");
        }
        if(!comment.getUser().getId().equals(userId)){
            throw new CustomExceptions.NotFoundException("Bu yorumu güncelleme yetkiniz yok.");
        }
        comment.setContents(commentDtoUpdate.getContents());
        comment.setCommentDate(LocalDateTime.now());
        commentRepo.save(comment);
    }
}







