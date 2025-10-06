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

        if(followingList.isEmpty()){
            return new ArrayList<>();
        }

        List<Long> IdList = followingList.stream().map(Follow::getFollowedId).toList();

        List<UserDTO> DTOList = userRepository.findAllById(IdList).stream().map(UserDTO::new).peek(userDTO -> {
            userDTO.setFollow(true);
        }).toList();

        return DTOList;
    }

    @Transactional
    public List<UserDTO> otherUserFollowing(Long myid, Long targetid){
        if(!userRepository.existsById(targetid)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"User Not Found");
        }

        List<Long> targetUserfollowingList = followRepository.findByFollowingId(targetid).stream().map(Follow::getFollowedId).toList();
        if(targetUserfollowingList.isEmpty()){
            return new ArrayList<>();
        }

        List<Long> myFollowingList = followRepository.findByFollowingId(myid).stream().map(Follow::getFollowedId).toList();

        List<UserDTO> DTOList = userRepository.findAllById(targetUserfollowingList).stream().map(UserDTO::new).peek(userDTO ->{
            userDTO.setFollow(
                    myFollowingList.contains(userDTO.getId())
            );
        }).toList();

        return DTOList;

    }


}
