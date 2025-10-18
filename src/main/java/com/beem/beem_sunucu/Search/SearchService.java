package com.beem.beem_sunucu.Search;

import com.beem.beem_sunucu.Users.User;
import com.beem.beem_sunucu.Users.User_Repo;
import com.beem.beem_sunucu.Users.User_Response_DTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class SearchService {

    private final User_Repo userRepo;

    public SearchService(User_Repo userRepo){
        this.userRepo = userRepo;
    }

    public Page<User_Response_DTO> searchUser(String searchValue,int page, int size){
        return userRepo
                .findByUsernameContainingIgnoreCase(searchValue, PageRequest.of(page,size))
                .map(User_Response_DTO::new);
    }
}
