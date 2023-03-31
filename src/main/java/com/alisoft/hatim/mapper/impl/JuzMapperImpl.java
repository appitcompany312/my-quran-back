package com.alisoft.hatim.mapper.impl;

import com.alisoft.hatim.domain.Juz;
import com.alisoft.hatim.domain.reference.PageStatus;
import com.alisoft.hatim.dto.response.JuzResponseDto;
import com.alisoft.hatim.mapper.JuzMapper;
import com.alisoft.hatim.service.PageService;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Data
@Service
public class JuzMapperImpl implements JuzMapper {

    private final PageService pageService;

    @Override
    public JuzResponseDto juzToResponseDto(Juz juz) {
        return JuzResponseDto.builder()
                .id(juz.getId())
                .number(juz.getNumber())
                .status(juz.getStatus())
                .toDo(pageService.findAllByJuzAndStatus(juz, PageStatus.TODO).size())
                .inProgress(pageService.findAllByJuzAndStatus(juz, PageStatus.BOOKED).size()
                        + pageService.findAllByJuzAndStatus(juz, PageStatus.IN_PROGRESS).size())
                .done(pageService.findAllByJuzAndStatus(juz, PageStatus.DONE).size())
                .build();
    }

    @Override
    public List<JuzResponseDto> juzsToResponseDtos(List<Juz> juzs) {
        List<JuzResponseDto> juzResponseDtos = new ArrayList<>();
        for(Juz juz : juzs) {
            juzResponseDtos.add(juzToResponseDto(juz));
        }
        return juzResponseDtos;
    }
}
