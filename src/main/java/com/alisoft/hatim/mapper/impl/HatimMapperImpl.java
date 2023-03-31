package com.alisoft.hatim.mapper.impl;

import com.alisoft.hatim.domain.Hatim;
import com.alisoft.hatim.dto.response.HatimResponseDto;
import com.alisoft.hatim.mapper.HatimMapper;
import lombok.Data;
import org.springframework.stereotype.Service;

@Data
@Service
public class HatimMapperImpl implements HatimMapper {
    @Override
    public HatimResponseDto hatimToResponseDto(Hatim hatim) {
        return HatimResponseDto
                .builder()
                .id(hatim.getId())
                .status(hatim.getStatus())
                .type(hatim.getType())
                .build();
    }
}
