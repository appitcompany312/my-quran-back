package com.alisoft.hatim.service;

import com.alisoft.hatim.dto.request.BookPageRequestDto;
import com.alisoft.hatim.dto.request.PageRequestDto;
import com.alisoft.hatim.exception.NotFoundException;
import com.alisoft.hatim.exception.PermissionDeniedException;

import java.util.UUID;

public interface WsService {
    void getPagesByJuz(UUID juzId) throws NotFoundException;
    void getJuzByHatim(UUID hatimId) throws NotFoundException;
    void setPageToDo(BookPageRequestDto bookPageRequestDto) throws NotFoundException, PermissionDeniedException;
    void bookPage(BookPageRequestDto bookPageRequestDto) throws NotFoundException;
    void setPageInProgress(PageRequestDto pageRequestDto) throws NotFoundException;
    void setPageDone(PageRequestDto pageRequestDto) throws NotFoundException;
    void getUserPages(String username);
    void rollbackExpiredBookedPages();
    void rollbackExpiredProgressedPages();
}
