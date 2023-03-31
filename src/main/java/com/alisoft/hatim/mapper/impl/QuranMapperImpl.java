package com.alisoft.hatim.mapper.impl;

import com.alisoft.hatim.domain.quran.Ayah;
import com.alisoft.hatim.dto.response.QuranPageResponseDto;
import com.alisoft.hatim.mapper.QuranMapper;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Data
@Service
public class QuranMapperImpl implements QuranMapper {
    @Override
    public QuranPageResponseDto ayahToQuranPageResponseDto(Ayah ayah) {
        return QuranPageResponseDto
                .builder()
                .verse_key(ayah.getSurah().getNumber() + ":" + ayah.getNumber())
                .text_uthmani(ayah.getAyah())
                .build();
    }

    @Override
    public List<QuranPageResponseDto> ayahsToQuranPageResponseDtos(List<Ayah> ayahs) {
        List<QuranPageResponseDto> pageResponseDtoList = new ArrayList<>();
        for (Ayah ayah: ayahs) {
            pageResponseDtoList.add(ayahToQuranPageResponseDto(ayah));
        }
        return pageResponseDtoList;
    }
}
