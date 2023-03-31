package com.alisoft.hatim.mapper;

import com.alisoft.hatim.domain.Page;
import com.alisoft.hatim.dto.response.PageResponseDto;

import java.util.List;

public interface PageMapper {
    PageResponseDto pageToResponseDto(Page page);
    PageResponseDto pageToResponseDtoWithUser(Page page, String username);

    List<PageResponseDto> pagesToResponseDtos(List<Page> pages);
    List<PageResponseDto> pagesToResponseDtosWithUser(List<Page> pages, String username);
}
