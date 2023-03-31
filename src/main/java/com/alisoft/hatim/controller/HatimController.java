package com.alisoft.hatim.controller;

import com.alisoft.hatim.config.security.jwt.JwtUser;
import com.alisoft.hatim.domain.Hatim;
import com.alisoft.hatim.dto.response.HatimResponseDto;
import com.alisoft.hatim.dto.response.JuzResponseDto;
import com.alisoft.hatim.exception.NotFoundException;
import com.alisoft.hatim.service.HatimService;
import lombok.Data;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@Data
@RestController
@RequestMapping("/api/v1/hatim")
public class HatimController {

    private final HatimService hatimService;

    @GetMapping("/{id}")
    public Hatim get(
            @PathVariable UUID id
    ) throws NotFoundException {
        return hatimService.get(id);
    }


    @GetMapping("/join_to_hatim")
    public HatimResponseDto joinToHatim(@AuthenticationPrincipal JwtUser jwtUser) throws NotFoundException {
        return hatimService.joinToHatim(jwtUser);
    }

    @GetMapping("/get_juz_by_hatim/{hatimId}")
    public List<JuzResponseDto> getJuzByHatim(@PathVariable UUID hatimId) {
        return hatimService.getJuzByHatim(hatimId);
    }
}
