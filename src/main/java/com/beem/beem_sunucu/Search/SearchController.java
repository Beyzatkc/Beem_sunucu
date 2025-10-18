package com.beem.beem_sunucu.Search;

import com.beem.beem_sunucu.Posts.Post_DTO_Response;
import com.beem.beem_sunucu.Users.User_Response_DTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/search")
public class SearchController {

    private final SearchService service;

    @Autowired
    public SearchController(SearchService service) {
        this.service = service;
    }

    @GetMapping("/users")
    public Page<User_Response_DTO> searchUsers(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return service.searchUsers(keyword, page, size);
    }

    @GetMapping("/posts")
    public Page<Post_DTO_Response> searchPosts(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return service.searchPosts(keyword, page, size);
    }
}
