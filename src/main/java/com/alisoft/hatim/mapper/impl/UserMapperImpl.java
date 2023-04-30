package com.alisoft.hatim.mapper.impl;

import com.alisoft.hatim.domain.User;
import com.alisoft.hatim.dto.response.UserResponseDto;
import com.alisoft.hatim.mapper.UserMapper;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Data
@Service
public class UserMapperImpl implements UserMapper {

    @Override
    public UserResponseDto userToUserResponseDto(User user) {

        return UserResponseDto
            .builder()
                .id(user.getId())
                .userName(user.getUsername())
                .gender(user.getGender())
                .language(user.getLanguage())
                .confirmed(user.getConfirmed())
                .build();
    }

    @Override
    public List<UserResponseDto> userListToUserResponseDtoList(List<User> users) {
        List<UserResponseDto> usersDtoList = new ArrayList<>();
        for(User user : users) {
            usersDtoList.add(userToUserResponseDto(user));
        }
        return usersDtoList;
    }

}
