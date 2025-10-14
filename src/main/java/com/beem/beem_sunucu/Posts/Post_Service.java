package com.beem.beem_sunucu.Posts;

import com.beem.beem_sunucu.Users.CustomExceptions;
import com.beem.beem_sunucu.Users.User;
import com.beem.beem_sunucu.Users.User_Repo;
import com.beem.beem_sunucu.Users.User_Response_DTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class Post_Service {
    private final User_Repo userRepo;
    private final Post_Repo postRepo;
    private final Postof_Like_Repo postofLikeRepo;


    public Post_Service(User_Repo userRepo, Post_Repo postRepo, Postof_Like_Repo postofLikeRepo) {
        this.userRepo = userRepo;
        this.postRepo = postRepo;
        this.postofLikeRepo = postofLikeRepo;
    }
    public void postCreate(Post_DTO_Request postDto){
        Optional<User> user = userRepo.findById(postDto.getUser_id());
        if (user.isEmpty()) {
            throw new CustomExceptions.AuthenticationException("Kullanıcı bulunamadı.");
        }
        Post post=new Post();
        post.setUser(user.get());
        post.setPostName(postDto.getPostName());
        post.setContents(postDto.getContents());
        post.setNumberofLikes(0);
        post.setPostDate(LocalDateTime.now());
        postRepo.save(post);
    }
    public List<Post_DTO_Response> fetch_users_posts(Long userId,int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("postDate").descending());
        List<Post>posts=postRepo.findByUser_Id(userId,pageable).getContent();
        return posts.stream().map(post -> new Post_DTO_Response(post)).toList();
    }
    public String Like_the_post(Long postId, Long userId){

        Optional<Post> postdb = postRepo.findById(postId);
        Optional<User> userdb = userRepo.findById(userId);

        if (postdb.isEmpty() || userdb.isEmpty()) {
            throw new CustomExceptions.AuthenticationException("Gönderi veya kullanıcı bulunamadı");
        }
        Post post = postdb.get();
        User user=userdb.get();
        Post_Like like = new Post_Like();
        like.setPost(post);
        like.setUser(user);
        postofLikeRepo.saveAndFlush(like);

        int newCount=post.getNumberofLikes() + 1;
        postRepo.updateLikeCount(postId, newCount);

        return "Gönderi beğenildi";
    }
    public String remove_post_likes(Long postId, Long userId){
        postofLikeRepo.deleteByPost_PostIdAndUser_Id(postId, userId);

        Optional<Post> postdb = postRepo.findById(postId);
        if (postdb.isPresent()) {
            Post post = postdb.get();
            int newCount = Math.max(0, post.getNumberofLikes() - 1);
            postRepo.updateLikeCount(postId, newCount);
        }
        return "Beğeni kaldırıldı";
    }
    public List<User_Response_DTO> users_who_like(Long postId, Long currentUserId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        List<Post_Like> posts_like = postofLikeRepo.findLikesOrderedByFollow(postId, currentUserId, pageable).getContent();

        return posts_like.stream().map(postLike -> new User_Response_DTO(postLike.getUser())).toList();
    }
    //ANASAYFA GONDEİRLERİ GELSİNNNN
}
