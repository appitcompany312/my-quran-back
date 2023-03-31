package com.alisoft.hatim.mapper;

import com.alisoft.hatim.domain.Juz;
import com.alisoft.hatim.dto.response.JuzResponseDto;

import java.util.List;

public interface JuzMapper {

    JuzResponseDto juzToResponseDto(Juz juz);

    List<JuzResponseDto> juzsToResponseDtos(List<Juz> juzs);
}
