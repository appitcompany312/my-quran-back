package com.alisoft.hatim.controller;

import com.alisoft.hatim.dto.request.BookPageRequestDto;
import com.alisoft.hatim.dto.request.PageRequestDto;
import com.alisoft.hatim.exception.NotFoundException;
import com.alisoft.hatim.exception.PermissionDeniedException;
import com.alisoft.hatim.service.WsService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;

@Data
@Slf4j
@Controller
public class WSController {
    private final WsService wsService;

    @MessageMapping("/get_juz_by_hatim")
    public void getJuzByHatim(
            @RequestBody UUID hatimId
    ) throws NotFoundException {
        wsService.getJuzByHatim(hatimId);
    }

    @MessageMapping("/get_pages_by_juz")
    public void getPagesByJuz(@RequestBody UUID juzId) throws NotFoundException {
        wsService.getPagesByJuz(juzId);
    }

    @MessageMapping("/to_do")
    public void toDo(@RequestBody BookPageRequestDto bookPageRequestDto) throws NotFoundException, PermissionDeniedException {
        wsService.setPageToDo(bookPageRequestDto);
    }

    @MessageMapping("/book")
    public void check(@RequestBody BookPageRequestDto bookPageRequestDto) throws NotFoundException {
        wsService.bookPage(bookPageRequestDto);
    }

    @MessageMapping("/in_progress")
    public void inProgress(@RequestBody PageRequestDto pageRequestDto) throws NotFoundException {
        wsService.setPageInProgress(pageRequestDto);
    }

    @MessageMapping("/done")
    public void done(@RequestBody PageRequestDto pageRequestDto) throws NotFoundException {
        wsService.setPageDone(pageRequestDto);
    }

}
