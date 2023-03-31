package com.alisoft.hatim.mapper;

import com.alisoft.hatim.domain.User;
import com.alisoft.hatim.dto.response.UserResponseDto;

import java.util.List;

public interface UserMapper {

    UserResponseDto userToUserResponseDto(User user);

    List<UserResponseDto> userListToUserResponseDtoList(List<User> users);

}
