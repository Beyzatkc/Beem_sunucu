package com.beem.beem_sunucu.Posts;

import com.beem.beem_sunucu.Follow.FollowRepository;
import com.beem.beem_sunucu.Users.CustomExceptions;
import com.beem.beem_sunucu.Users.User;
import com.beem.beem_sunucu.Users.User_Repo;
import com.beem.beem_sunucu.Users.User_Response_DTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class Post_Service {
    private final FollowRepository followRepository;
    private final User_Repo userRepo;
    private final Post_Repo postRepo;
    private final Postof_Like_Repo postofLikeRepo;


    public Post_Service(FollowRepository followRepository, User_Repo userRepo, Post_Repo postRepo, Postof_Like_Repo postofLikeRepo) {
        this.followRepository = followRepository;
        this.userRepo = userRepo;
        this.postRepo = postRepo;
        this.postofLikeRepo = postofLikeRepo;
    }
    public Post_DTO_Response postCreate(Post_DTO_Request postDto){
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
        return new Post_DTO_Response(post);
    }
    public List<Post_DTO_Response> fetch_users_posts(Long userId,int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("postDate").descending());
        List<Post>posts=postRepo.findByUser_Id(userId,pageable).getContent();
        return posts.stream().map(post -> new Post_DTO_Response(post)).toList();
    }

    @Transactional
    public String toggleLike(Long postId, Long userId) {
        Post post = postRepo.findById(postId)
                .orElseThrow(() -> new CustomExceptions.AuthenticationException("Gönderi bulunamadı"));
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new CustomExceptions.AuthenticationException("Kullanıcı bulunamadı"));
        Optional<Post_Like> existingLike = postofLikeRepo.findByPost_PostIdAndUser_Id(postId, userId);
        if (existingLike.isPresent()) {
            postofLikeRepo.delete(existingLike.get());

            int newCount = Math.max(0, post.getNumberofLikes() - 1);
            postRepo.updateLikeCount(postId, newCount);

            return "Beğeni kaldırıldı";
        } else {
            Post_Like like = new Post_Like();
            like.setPost(post);
            like.setUser(user);
            postofLikeRepo.saveAndFlush(like);

            int newCount = post.getNumberofLikes() + 1;
            postRepo.updateLikeCount(postId, newCount);

            return "Gönderi beğenildi";
        }

    }
    public List<User_Response_DTO> users_who_like(Long postId, Long currentUserId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Post_Like> posts_like_page = postofLikeRepo.findPostLikesWithFollowOrder(postId, currentUserId, pageable);
        List<Post_Like> posts_like = posts_like_page.getContent();
        return posts_like.stream().map(postLike -> new User_Response_DTO(postLike.getUser())).toList();
    }


    public List<Post_DTO_Response> homePagePosts(Long currentUserId, int page, int size) {
        //takip edilen kullanicialri getirid
        List<Long> followIds = followRepository.findFollowedIds(currentUserId);

        //takip eidlen kullanıcıların begendigi gonderiler
        List<Long> followLikes = postofLikeRepo.findPostIdsByUsers(followIds);

        Pageable pageable = PageRequest.of(page, size);

        //dbden sırali skeilde cektik
        Page<Post> postPage = postRepo.findHomePagePostsNative( currentUserId, followIds, followLikes, pageable);

        List<Post> posts = postPage.getContent();
        return posts.stream().map(post -> new Post_DTO_Response(post)).toList();
    }

}
