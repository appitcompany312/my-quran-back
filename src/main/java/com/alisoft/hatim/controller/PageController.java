package com.alisoft.hatim.controller;

import com.alisoft.hatim.config.security.jwt.JwtUser;
import com.alisoft.hatim.domain.Juz;
import com.alisoft.hatim.domain.Page;
import com.alisoft.hatim.dto.request.PageRequestDto;
import com.alisoft.hatim.dto.response.PageResponseDto;
import com.alisoft.hatim.exception.NotFoundException;
import com.alisoft.hatim.mapper.PageMapper;
import com.alisoft.hatim.service.JuzService;
import com.alisoft.hatim.service.PageService;
import com.alisoft.hatim.service.WsService;
import lombok.Data;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Data
@RestController
@RequestMapping("/api/v1/page")
public class PageController {
    private final PageService pageService;
    private final PageMapper pageMapper;
    private final JuzService juzService;
    private final WsService wsService;

    @GetMapping("/{id}")
    public Page get(
            @PathVariable UUID id
    ) throws NotFoundException {
        return pageService.get(id);
    }

    @GetMapping("/get_by_juz/{id}")
    public List<PageResponseDto> getByJuz(
            @AuthenticationPrincipal JwtUser jwtUser,
            @PathVariable UUID id
    ) throws NotFoundException {
        Juz juz = juzService.get(id);
        return pageMapper.pagesToResponseDtos(pageService.getAllByJuz(juz));
    }

    @PostMapping("/in_progress")
    public void pageInProgress(
            @RequestBody PageRequestDto pageRequestDto
    ) throws NotFoundException {
        wsService.setPageInProgress(pageRequestDto);
        wsService.checkInProgressPages(pageRequestDto);
    }

    @PostMapping("/done")
    public void pageDone(
            @RequestBody PageRequestDto pageRequestDto
    ) throws NotFoundException {
        wsService.setPageDone(pageRequestDto);
    }
}
