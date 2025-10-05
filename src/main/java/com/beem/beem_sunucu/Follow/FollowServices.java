package com.beem.beem_sunucu.Follow;

import com.beem.beem_sunucu.User.User;
import com.beem.beem_sunucu.User.UserDTO;
import com.beem.beem_sunucu.User.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
public class FollowServices {

    private final FollowRepository followRepository;
    private final UserRepository userRepository;

    public FollowServices(FollowRepository followRepository, UserRepository userRepository){
        this.followRepository = followRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public FollowDTO createFollow(FollowDTO followDTO){
        if(!userRepository.existsById(followDTO.getFollowingId())){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Following User Not Found");
        }
        if(!userRepository.existsById(followDTO.getFollowedId())){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Followed User Not Found");
        }
        if(followRepository.existsByFollowedIdAndFollowingId(followDTO.getFollowedId(), followDTO.getFollowingId())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Following this user");
        }
        if(followDTO.getFollowingId().equals(followDTO.getFollowedId())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You cannot follow yourself");
        }

        followDTO.setFollowed(true);
        Follow follow = new Follow();
        follow.setFollowedId(followDTO.getFollowedId());
        follow.setFollowingId(followDTO.getFollowingId());
        followRepository.save(follow);
        return followDTO;
    }

    @Transactional
    public FollowDTO unFollow(FollowDTO followDTO){

        if(!userRepository.existsById(followDTO.getFollowingId())){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Following User Not Found");
        }
        if(!userRepository.existsById(followDTO.getFollowedId())){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Followed User Not Found");
        }
        if(followDTO.getFollowingId().equals(followDTO.getFollowedId())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You cannot unfollow yourself");
        }
        if(!followRepository.existsByFollowedIdAndFollowingId(followDTO.getFollowedId(), followDTO.getFollowingId())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"You are not following this user");
        }

        followDTO.setFollowed(false);
        followRepository.deleteByFollowedIdAndFollowingId(followDTO.getFollowedId(), followDTO.getFollowingId());

        return followDTO;
    }

    @Transactional
    public List<UserDTO> userFollowing(Long id){

        if(!userRepository.existsById(id)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"User Not Found");
        }
        List<Follow> followingList = followRepository.findByFollowingId(id);
        if(followingList==null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Following users null");
        }

        List<UserDTO> list = new ArrayList<>();
        if(followingList.isEmpty()){
            return list;
        }

        for(Follow follow: followingList){
            list.add(
                    new UserDTO(
                            userRepository.findById(follow.getFollowedId()).orElse(new User())
                    )
            );
        }

        return list;
    }


}
