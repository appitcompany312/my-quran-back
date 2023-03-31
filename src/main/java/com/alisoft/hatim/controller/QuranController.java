package com.alisoft.hatim.controller;

import com.alisoft.hatim.config.security.jwt.JwtUser;
import com.alisoft.hatim.dto.response.QuranPageResponseDto;
import com.alisoft.hatim.exception.NotFoundException;
import com.alisoft.hatim.service.QuranService;
import lombok.Data;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Data
@RestController
@RequestMapping("/api/v1/quran")
public class QuranController {

    private final QuranService quranService;

    @GetMapping("/get/in_progress/pages")
    public List<QuranPageResponseDto> getInProgressPages(
            @AuthenticationPrincipal JwtUser jwtUser
    ) throws NotFoundException {
        return quranService.getInProgressPages(jwtUser);
    }
}
