package com.alisoft.hatim.controller;

import com.alisoft.hatim.domain.Hatim;
import com.alisoft.hatim.domain.Juz;
import com.alisoft.hatim.dto.response.JuzResponseDto;
import com.alisoft.hatim.exception.NotFoundException;
import com.alisoft.hatim.mapper.JuzMapper;
import com.alisoft.hatim.service.HatimService;
import com.alisoft.hatim.service.JuzService;
import lombok.Data;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@Data
@RestController
@RequestMapping("/api/v1/juz")
public class JuzController {
    private final JuzService juzService;
    private final JuzMapper juzMapper;
    private final HatimService hatimService;

    @GetMapping("/{id}")
    public Juz get(
            @PathVariable UUID id
    ) throws NotFoundException {
        return juzService.get(id);
    }

    @GetMapping("/get_by_hatim/{id}")
    public List<JuzResponseDto> getByHatim(
            @PathVariable UUID id
    ) throws NotFoundException {
        Hatim hatim = hatimService.get(id);
        return juzMapper.juzsToResponseDtos(juzService.getByHatim(hatim));
    }
}
