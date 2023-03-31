package com.alisoft.hatim.service.impl;

import com.alisoft.hatim.domain.Hatim;
import com.alisoft.hatim.domain.Juz;
import com.alisoft.hatim.domain.Page;
import com.alisoft.hatim.domain.reference.JuzStatus;
import com.alisoft.hatim.domain.reference.PageStatus;
import com.alisoft.hatim.dto.request.BookPageRequestDto;
import com.alisoft.hatim.dto.request.PageRequestDto;
import com.alisoft.hatim.exception.NotFoundException;
import com.alisoft.hatim.exception.PermissionDeniedException;
import com.alisoft.hatim.mapper.PageMapper;
import com.alisoft.hatim.mapper.JuzMapper;
import com.alisoft.hatim.service.*;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@Service
@Slf4j
public class WsServiceImpl implements WsService {
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final UserService userService;
    private final HatimService hatimService;
    private final JuzService juzService;
    private final JuzMapper juzMapper;
    private final PageService pageService;
    private final PageMapper pageMapper;
    private final TaskScheduler taskScheduler;

    @Override
    public void getJuzByHatim(UUID hatimId) throws NotFoundException {
        listOfJuz(hatimService.get(hatimId));
    }

    @Override
    @Transactional
    public void setPageToDo(BookPageRequestDto bookPageRequestDto) throws NotFoundException, PermissionDeniedException {
        Page page = pageService.get(bookPageRequestDto.getPageId());
        if (!page.getUser().getUsername().equals(bookPageRequestDto.getUsername())) throw new PermissionDeniedException("You don’t have permission to access.");
        page = pageService.pageToDo(page);
        listOfJuz(page.getJuz().getHatim());
//        listOfPage(page.getJuz());
        listOfUsernamePage(page.getJuz(), bookPageRequestDto.getUsername());
        userPages(bookPageRequestDto.getUsername());
    }

    @Override
    @Transactional
    public void bookPage(BookPageRequestDto bookPageRequestDto) throws NotFoundException {
        Page page = pageService.get(bookPageRequestDto.getPageId());
        page = pageService.bookPage(page, userService.findByUserName(bookPageRequestDto.getUsername()));
        Juz juz = juzService.get(page.getJuz().getId());
        if (juz.getStatus() != JuzStatus.IN_PROGRESS) {
            juzService.setJuzStatusInProgress(juz);
        }
        listOfJuz(juz.getHatim());
        listOfUsernamePage(page.getJuz(), bookPageRequestDto.getUsername());
        userPages(bookPageRequestDto.getUsername());
    }

    @Override
    public void getPagesByJuz(UUID juzId) throws NotFoundException {
        listOfPage(juzService.get(juzId));
    }

    @Override
    @Transactional
    public void setPageInProgress(PageRequestDto pageRequestDto) throws NotFoundException {
        for (UUID pageId: pageRequestDto.getPageIds()) {
            Page page = pageService.get(pageId);
            page = pageService.pageInProgress(page, userService.findByUserName(pageRequestDto.getUsername()));
            Juz juz = juzService.get(page.getJuz().getId());
            if (juz.getStatus() != JuzStatus.IN_PROGRESS) {
                juzService.setJuzStatusInProgress(juz);
            }
            listOfJuz(juz.getHatim());
            listOfUsernamePage(page.getJuz(), pageRequestDto.getUsername());
            userPages(pageRequestDto.getUsername());
        }
    }

    @Override
    @Transactional
    public void setPageDone(PageRequestDto pageRequestDto) throws NotFoundException {
        for (UUID pageId: pageRequestDto.getPageIds()) {
            Page page = pageService.get(pageId);
            page = pageService.pageDone(page, userService.findByUserName(pageRequestDto.getUsername()));
            Juz juz = juzService.get(page.getJuz().getId());
            if (pageService.isAllPagesDone(juz)) {
                juzService.setJuzStatusDone(juz);
                if (juzService.isAllJuzsDone(juz.getHatim())) {
                    hatimService.setHatimStatusDone(juz.getHatim());
                }
            }
            listOfJuz(juz.getHatim());
            listOfUsernamePage(page.getJuz(), pageRequestDto.getUsername());
            userPages(pageRequestDto.getUsername());
        }

    }

    @Override
    public void getUserPages(String username) {
        userPages(username);
    }

    @Override
    @Transactional
    public void checkBookedPage(BookPageRequestDto bookPageRequestDto) {
        taskScheduler.schedule(
                () -> {
                    Page page;
                    try {
                        page = pageService.get(bookPageRequestDto.getPageId());
                    } catch (NotFoundException e) {
                        throw new RuntimeException(e);
                    }
                    if (page.getStatus().equals(PageStatus.BOOKED)) {
                        pageService.pageToDo(page);
                        listOfJuz(page.getJuz().getHatim());
                        listOfUsernamePage(page.getJuz(), bookPageRequestDto.getUsername());
                        userPages(bookPageRequestDto.getUsername());
                    }
                },
                new Date(OffsetDateTime.now().plusMinutes(5).toInstant().toEpochMilli())
        );
    }

    @Override
    @Transactional
    public void checkInProgressPages(PageRequestDto pageRequestDto) {
        taskScheduler.schedule(
                () -> {
                    for (UUID pageId: pageRequestDto.getPageIds()) {
                        Page page;
                        try {
                            page = pageService.get(pageId);
                        } catch (NotFoundException e) {
                            throw new RuntimeException(e);
                        }
                        if (page.getStatus().equals(PageStatus.IN_PROGRESS)) {
                            page = pageService.pageToDo(page);
                            listOfJuz(page.getJuz().getHatim());
                            listOfUsernamePage(page.getJuz(), pageRequestDto.getUsername());
                            userPages(pageRequestDto.getUsername());
                        }
                    }
                },
                new Date(OffsetDateTime.now().plusMinutes(2880).toInstant().toEpochMilli())
        );
    }

    private void listOfJuz(Hatim hatim) {
        simpMessagingTemplate
                .convertAndSend(
                        "/topic/" + hatim.getId() + "/list_of_juz",
                        juzMapper.juzsToResponseDtos(juzService.getByHatim(hatim)
                        )
                );
    }

    private void listOfPage(Juz juz) {
        simpMessagingTemplate
                .convertAndSend(
                        "/topic/" + juz.getId() + "/list_of_page",
                        pageMapper.pagesToResponseDtos(pageService.getAllByJuz(juz))
                        );
    }

    private void listOfUsernamePage(Juz juz, String username) {
        simpMessagingTemplate
                .convertAndSend(
                        "/topic/" + juz.getId() + "/list_of_page",
                        pageMapper.pagesToResponseDtosWithUser(pageService.getAllByJuz(juz), username)
                );
    }

    private void userPages(String username) {
        List<Page> pages = pageService.findAllByUserAndStatus(userService.findByUserName(username), PageStatus.IN_PROGRESS);
        pages.addAll(pageService.findAllByUserAndStatus(userService.findByUserName(username), PageStatus.BOOKED));
        simpMessagingTemplate
                .convertAndSend(
                        "/topic/" + username + "/user_pages",
                        pageMapper.pagesToResponseDtosWithUser(pages, username)
                );
    }
}
