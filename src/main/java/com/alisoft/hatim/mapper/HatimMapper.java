package com.alisoft.hatim.mapper;

import com.alisoft.hatim.domain.Hatim;
import com.alisoft.hatim.dto.response.HatimResponseDto;

public interface HatimMapper {

    HatimResponseDto hatimToResponseDto(Hatim hatim);
}
