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
import java.util.*;
import java.util.stream.Collectors;

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
        User user = userRepo.findById(commentDtoRequest.getUserId())
                .orElseThrow(() -> new CustomExceptions.AuthenticationException("Kullanıcı bulunamadı."));

        Post post = postRepo.findById(commentDtoRequest.getPostId())
                .orElseThrow(() -> new CustomExceptions.AuthenticationException("Post bulunamadı."));

        Comment comment = new Comment();
        comment.setUser(user);
        comment.setContents(commentDtoRequest.getContents());
        comment.setCommentDate(LocalDateTime.now());
        comment.setNumberofLikes(0);
        comment.setParentYorum(null);
        comment.setPost(post);
        comment.setSubComments(new ArrayList<>());

        commentRepo.save(comment);

        return new Comment_DTO_Response(
                comment.getCommentId(),
                comment.getWhosReply(),
                post.getPostId(),
                post.getUser().getId(),
                user.getId(),
                user.getUsername(),
                comment.getContents(),
                comment.getNumberofLikes(),
                comment.getCommentDate(),
                comment.getUpdateDate(),
                null,
                false,
                0L
        );
    }


    @Transactional(readOnly = true)
    public List<Comment_DTO_Response> commentsGet(
            Long postId,
            Long currentUserId,
            int page,
            int size
    ) {

        Page<Comment_DTO_Response> comments =
                commentRepo.findMainComments(
                        postId,
                        PageRequest.of(page, size)
                );

        List<Comment_DTO_Response> content = comments.getContent();

        List<Long> commentIds = content.stream()
                .map(Comment_DTO_Response::getComment_id)
                .toList();

        Set<Long> likedCommentIds;

        if (currentUserId != null && !commentIds.isEmpty()) {
            likedCommentIds = commentLikeRepo
                    .findByComment_CommentIdInAndUser_Id(commentIds, currentUserId)
                    .stream()
                    .map(cl -> cl.getComment().getCommentId())
                    .collect(Collectors.toSet());
        } else {
            likedCommentIds = new HashSet<>();
        }

        content.forEach(dto ->
                dto.setLiked(likedCommentIds.contains(dto.getComment_id()))
        );

        return content;
    }

    public Long CountPinned(Long postId){
        return commentRepo.countPinnedComments(postId);
    }


    @Transactional
    public Comment_DTO_Response createSubComment(Comment_DTO_Request dto) {
        User user = userRepo.findById(dto.getUserId())
                .orElseThrow(() -> new CustomExceptions.AuthenticationException("Kullanıcı bulunamadı."));

        Post post = postRepo.findById(dto.getPostId())
                .orElseThrow(() -> new CustomExceptions.NotFoundException("Post bulunamadı."));

        Comment parentComment = commentRepo.findById(dto.getParentCommentId())
                .orElseThrow(() -> new CustomExceptions.NotFoundException("Yorum bulunamadı."));

        Comment reply = new Comment();
        reply.setUser(user);
        reply.setPost(post);
        reply.setContents(dto.getContents().trim());
        reply.setCommentDate(LocalDateTime.now());
        reply.setNumberofLikes(0);
        reply.setWhosReply(dto.getParentUsername());
        parentComment.setSubCommentsCount(parentComment.getSubCommentsCount()+1);
        reply.setParentYorum(parentComment);
        reply.setSubComments(new ArrayList<>());

        commentRepo.save(reply);
        commentRepo.save(parentComment);

        Comment_DTO_Response cdto = new Comment_DTO_Response(
                reply.getCommentId(),
                reply.getWhosReply(),
                post.getPostId(),
                post.getUser().getId(),
                user.getId(),
                user.getUsername(),
                reply.getContents(),
                reply.getNumberofLikes(),
                reply.getCommentDate(),
                reply.getUpdateDate(),
                parentComment.getCommentId(),
                reply.getPinned(),
                0L
        );

        cdto.setParentCommentUsername(dto.getParentUsername());

        return cdto;
    }



    @Transactional(readOnly = true)
    public List<Comment_DTO_Response> subCommentsGet(
            Long parentCommentId,
            Long currentUserId,
            int page,
            int size
    ) {

        Page<Comment_DTO_Response> comments =
                commentRepo.findSubCommentsByParentId(
                        parentCommentId,
                        PageRequest.of(page, size)
                );

        List<Comment_DTO_Response> content = comments.getContent();

        List<Long> commentIds = content.stream()
                .map(Comment_DTO_Response::getComment_id)
                .toList();
        Set<Long> likedCommentIds =
                commentLikeRepo.findByComment_CommentIdInAndUser_Id(commentIds, currentUserId)
                        .stream()
                        .map(cl -> cl.getComment().getCommentId())
                        .collect(Collectors.toSet());

        content.forEach(dto ->
                dto.setLiked(likedCommentIds.contains(dto.getComment_id()))
        );

        return content;
    }

    @Transactional
    public Comment_DTO_Response toggleLike(Long commentId, Long userId) {
        Comment comment = commentRepo.findById(commentId)
                .orElseThrow(() -> new CustomExceptions.AuthenticationException("Yorum bulunamadı"));
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new CustomExceptions.AuthenticationException("Kullanıcı bulunamadı"));

        Optional<Comment_Like> existingLike = commentLikeRepo.findByComment_CommentIdAndUser_Id(commentId, userId);

        if (existingLike.isPresent()) {
            commentLikeRepo.delete(existingLike.get());
            comment.setNumberofLikes(Math.max(0, comment.getNumberofLikes() - 1));
            commentRepo.saveAndFlush(comment);
            Comment_DTO_Response dto= convertToDto(comment);
            dto.setLiked(false);
            return dto;
        } else {
            Comment_Like commentLike = new Comment_Like();
            commentLike.setComment(comment);
            commentLike.setUser(user);
            commentLikeRepo.save(commentLike);

            comment.setNumberofLikes(comment.getNumberofLikes() + 1);
            commentRepo.saveAndFlush(comment);
            Comment_DTO_Response dto= convertToDto(comment);
            dto.setLiked(true);
            return dto;
        }
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
        if(comment.getParentYorum()!=null){
            Comment parentcomment=commentRepo.findById(comment.getParentYorum().getCommentId())
                    .orElseThrow(() -> new CustomExceptions.NotFoundException("Üst Yorum bulunamadı."));

            parentcomment.setSubCommentsCount(comment.getSubCommentsCount()-1);
            commentRepo.save(parentcomment);
        }

       commentRepo.delete(comment);
    }

    @Transactional
    public Comment_DTO_Response updateComment(
            Long commentId,
            Long userId,
            Comment_DTO_Update commentDtoUpdate
    ) {
        Comment comment = commentRepo.findById(commentId)
                .orElseThrow(() -> new CustomExceptions.NotFoundException("Yorum bulunamadı."));

        boolean isliked=commentLikeRepo.existsByComment_CommentIdAndUser_Id(commentId,userId);

        if (userId == null) {
            throw new CustomExceptions.NotFoundException("Kullanıcı bulunamadı.");
        }

        if (!comment.getUser().getId().equals(userId)) {
            throw new CustomExceptions.AuthorizationException("Bu yorumu güncelleme yetkiniz yok.");
        }

        comment.setContents(commentDtoUpdate.getContents().trim());
        comment.setUpdateDate(LocalDateTime.now());
        commentRepo.save(comment);
        Comment_DTO_Response dto= convertToDto(comment);

        dto.setEdited(true);
        dto.setLiked(isliked);

        return dto;
    }


    @Transactional
    public Comment_DTO_Response pinComment(Long commentId,Long currentUserId){
        Comment comment=commentRepo.findById(commentId)
                .orElseThrow(()-> new CustomExceptions.NotFoundException("Yorum bulunamadı."));

        Long postId = comment.getPost().getPostId();

        if (Boolean.TRUE.equals(comment.getPinned())) {
            throw new CustomExceptions.AlreadyExistsException("Yorum zaten sabitlenmiş.");
        }

        Long pinnedCount = commentRepo.countPinnedComments(postId);

        if (pinnedCount >= 5) {
            throw new CustomExceptions.BusinessException(
                    "En fazla 5 yorum sabitlenebilir."
            );
        }
         Long postOwnerId=comment.getPost().getUser().getId();
         //if(!postOwnerId.equals(currentUserId)){
          //   throw new CustomExceptions.AuthorizationException("Bu yorumu sabitleme yetkiniz yok.");
         //}

        boolean isliked=commentLikeRepo.existsByComment_CommentIdAndUser_Id(commentId,currentUserId);

        comment.setPinned(true);
        commentRepo.save(comment);

        Comment_DTO_Response dto= convertToDto(comment);
        dto.setLiked(isliked);
        return dto;
    }

    @Transactional
    public Comment_DTO_Response removePin(Long commentId,Long currentUserId){
        Comment comment=commentRepo.findById(commentId)
                .orElseThrow(()-> new CustomExceptions.NotFoundException("Yorum bulunamadı."));
        Long postOwnerId=comment.getPost().getUser().getId();
        //if(!postOwnerId.equals(currentUserId)){
        //   throw new CustomExceptions.AuthorizationException("Bu yorumu sabitleme yetkiniz yok.");
        //}
        boolean isliked=commentLikeRepo.existsByComment_CommentIdAndUser_Id(commentId,currentUserId);

        comment.setPinned(false);
        commentRepo.save(comment);

        Comment_DTO_Response dto= convertToDto(comment);
        dto.setLiked(isliked);
        return dto;
    }


    private Comment_DTO_Response convertToDto(Comment comment) {
        return new Comment_DTO_Response(
                comment.getCommentId(),
                comment.getWhosReply(),
                comment.getPost().getPostId(),
                comment.getPost().getUser().getId(),
                comment.getUser().getId(),
                comment.getUser().getUsername(),
                comment.getContents(),
                comment.getNumberofLikes(),
                comment.getCommentDate(),
                comment.getUpdateDate(),
                comment.getParentYorum() != null ? comment.getParentYorum().getCommentId() : null,
                comment.getPinned(),
                comment.getSubCommentsCount()
        );
    }
}







