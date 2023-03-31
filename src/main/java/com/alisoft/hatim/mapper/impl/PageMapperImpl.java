package com.alisoft.hatim.mapper.impl;

import com.alisoft.hatim.domain.Page;
import com.alisoft.hatim.dto.response.PageResponseDto;
import com.alisoft.hatim.mapper.PageMapper;
import com.alisoft.hatim.service.UserService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Data
@Slf4j
@Service
public class PageMapperImpl implements PageMapper {

    private final UserService userService;

    @Override
    public PageResponseDto pageToResponseDto(Page page) {
        boolean isMine = false;
        if (page.getUser() != null) isMine = page.getUser().getUsername().equals(userService.getCurrentUsersUsername());
        return PageResponseDto.builder()
                .id(page.getId())
                .status(page.getStatus())
                .number(page.getNumber())
                .isMine(isMine)
                .build();
    }

    @Override
    public PageResponseDto pageToResponseDtoWithUser(Page page, String username) {
        boolean isMine = false;
        if (page.getUser() != null) isMine = page.getUser().getUsername().equals(username);
        return PageResponseDto.builder()
                .id(page.getId())
                .status(page.getStatus())
                .number(page.getNumber())
                .isMine(isMine)
                .build();
    }

    @Override
    public List<PageResponseDto> pagesToResponseDtos(List<Page> pages) {
        List<PageResponseDto> pageResponseDtos = new ArrayList<>();
        for (Page page: pages) {
            pageResponseDtos.add(pageToResponseDto(page));
        }
        return pageResponseDtos;
    }

    @Override
    public List<PageResponseDto> pagesToResponseDtosWithUser(List<Page> pages, String username) {
        List<PageResponseDto> pageResponseDtos = new ArrayList<>();
        for (Page page: pages) {
            pageResponseDtos.add(pageToResponseDtoWithUser(page, username));
        }
        return pageResponseDtos;
    }
}
