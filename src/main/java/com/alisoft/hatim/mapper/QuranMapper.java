package com.alisoft.hatim.mapper;

import com.alisoft.hatim.domain.quran.Ayah;
import com.alisoft.hatim.dto.response.QuranPageResponseDto;

import java.util.List;

public interface QuranMapper {

    QuranPageResponseDto ayahToQuranPageResponseDto(Ayah ayah);
    List<QuranPageResponseDto> ayahsToQuranPageResponseDtos(List<Ayah> ayahs);
}
