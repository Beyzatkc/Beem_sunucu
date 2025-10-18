package com.beem.beem_sunucu.Search;

import com.beem.beem_sunucu.Posts.Post_DTO_Response;
import com.beem.beem_sunucu.Posts.Post_Repo;
import com.beem.beem_sunucu.Users.User_Repo;
import com.beem.beem_sunucu.Users.User_Response_DTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class SearchService {

    private final User_Repo userRepo;
    private final Post_Repo postRepo;

    @Autowired
    public SearchService(User_Repo userRepo, Post_Repo postRepo){
        this.userRepo = userRepo;
        this.postRepo = postRepo;
    }

    @Transactional(readOnly = true)
    public Page<User_Response_DTO> searchUsers(String searchValue,int page, int size){
        return userRepo
                .findByUsernameContainingIgnoreCase(searchValue, PageRequest.of(page,size))
                .map(User_Response_DTO::new);
    }

    @Transactional(readOnly = true)
    public Page<Post_DTO_Response> searchPosts(String searchValue,int page, int size){
        return postRepo
                .findByPostNameContainingIgnoreCase(searchValue, PageRequest.of(page,size))
                .map(Post_DTO_Response::new);
    }
}
