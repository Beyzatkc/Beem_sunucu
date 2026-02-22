package com.beem.beem_sunucu.Follow;

import com.beem.beem_sunucu.Follow.FollowRequest.FollowResponseDTO;
import com.beem.beem_sunucu.Follow.FollowRequest.FollowSendRequest;
import com.beem.beem_sunucu.Users.SimpleUserDTO;
import com.beem.beem_sunucu.Users.User;
import org.springframework.stereotype.Component;

@Component
public class FollowMapper {

    public FollowResponseDTO toFollowResponseDTO(
            FollowSendRequest entity, boolean isMyFollower, boolean isFollowingYou
    ) {
        if (entity == null) return null;

        FollowResponseDTO dto = new FollowResponseDTO();
        dto.setId(entity.getId());
        dto.setRequester(toSimpleUserDTO(entity.getRequester()));
        dto.setRequested(toSimpleUserDTO(entity.getRequested()));
        dto.setStatus(entity.getStatus());
        dto.setDate(entity.getDate());
        dto.setMyFollower(isMyFollower);
        dto.setFollowingYou(isFollowingYou);

        return dto;
    }

    public FollowUserResponseDTO toFollowUserResponseDTO(User user, FollowSendRequest entity, boolean isMyFollower, boolean isFollowingYou){
        return new FollowUserResponseDTO(
                user,
                this.toFollowResponseDTO(entity, isMyFollower, isFollowingYou)
        );
    }

    public SimpleUserDTO toSimpleUserDTO(User user) {
        if (user == null) return null;

        return new SimpleUserDTO(user);
    }

    public FollowResponse toFollowResponse(User user, FollowUserView view){
        return new FollowResponse(
                user.getId(),
                user.getUsername(),
                user.getBiography(),
                user.getProfile(),
                integerToBoolean(view.getIsFollower()),
                integerToBoolean(view.getIsFollowing()),
                integerToBoolean(view.getIsPending())
        );
    }

    private boolean integerToBoolean(Integer value){
        return value != null && value == 1;
    }
}
