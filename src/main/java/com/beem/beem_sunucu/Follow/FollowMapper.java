package com.beem.beem_sunucu.Follow;

import com.beem.beem_sunucu.Follow.FollowRequest.FollowResponseDTO;
import com.beem.beem_sunucu.Follow.FollowRequest.FollowSendRequest;
import com.beem.beem_sunucu.Users.SimpleUserDTO;
import com.beem.beem_sunucu.Users.User;
import org.springframework.stereotype.Component;

@Component
public class FollowMapper {

    public FollowResponseDTO toFollowResponseDTO(FollowSendRequest entity) {
        if (entity == null) return null;

        FollowResponseDTO dto = new FollowResponseDTO();
        dto.setId(entity.getId());
        dto.setRequester(toSimpleUserDTO(entity.getRequester()));
        dto.setRequested(toSimpleUserDTO(entity.getRequested()));
        dto.setStatus(entity.getStatus());
        dto.setDate(entity.getDate());

        return dto;
    }


    public SimpleUserDTO toSimpleUserDTO(User user) {
        if (user == null) return null;

        return new SimpleUserDTO(user);
    }
}
