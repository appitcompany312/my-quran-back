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
import com.alisoft.hatim.mapper.JuzMapper;
import com.alisoft.hatim.mapper.PageMapper;
import com.alisoft.hatim.service.*;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

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
        listOfPage(page.getJuz());
        userPages(bookPageRequestDto.getUsername());
    }

    @Override
    @Transactional
    public void bookPage(BookPageRequestDto bookPageRequestDto) throws NotFoundException {
        Page page = pageService.get(bookPageRequestDto.getPageId());
        page = pageService.bookPage(page, userService.findByUserName(bookPageRequestDto.getUsername()));
        Juz juz = page.getJuz();
        if (juz.getStatus() != JuzStatus.IN_PROGRESS) {
            juzService.setJuzStatusInProgress(juz);
        }
        listOfJuz(juz.getHatim());
        listOfPage(juz);
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
            Juz juz = page.getJuz();
            if (juz.getStatus() != JuzStatus.IN_PROGRESS) {
                juzService.setJuzStatusInProgress(juz);
            }
            listOfJuz(juz.getHatim());
            listOfPage(juz);
            userPages(pageRequestDto.getUsername());
        }
    }

    @Override
    @Transactional
    public void setPageDone(PageRequestDto pageRequestDto) throws NotFoundException {
        for (UUID pageId: pageRequestDto.getPageIds()) {
            Page page = pageService.get(pageId);
            page = pageService.pageDone(page, userService.findByUserName(pageRequestDto.getUsername()));
            Juz juz = page.getJuz();
            if (pageService.isAllPagesDone(juz)) {
                juzService.setJuzStatusDone(juz);
                if (juzService.isAllJuzsDone(juz.getHatim())) {
                    hatimService.setHatimStatusDone(juz.getHatim());
                }
            }
            listOfJuz(juz.getHatim());
            listOfPage(juz);
            userPages(pageRequestDto.getUsername());
        }

    }

    @Override
    public void getUserPages(String username) {
        userPages(username);
    }

    @Override
    public void rollbackExpiredBookedPages() {
        setToDoExpiredPages(pageService.findAllWhichBookedFiveMinutesAgo());
    }

    @Override
    public void rollbackExpiredProgressedPages() {
        setToDoExpiredPages(pageService.findAllWhichProgressedTwoDayAgo());
    }

    private void setToDoExpiredPages(List<Page> expiredPages) {
        if (!expiredPages.isEmpty()) {
            Map<Juz, Map<String, List<Page>>> groupedPages = expiredPages.stream()
                    .collect(Collectors.groupingBy(Page::getJuz,
                            Collectors.groupingBy(p -> p.getUser().getUsername())));

            groupedPages.forEach((juz, userPagesMap) -> userPagesMap.forEach((username, pages) -> {
                pageService.pagesToDo(pages);
                listOfJuz(juz.getHatim());
                listOfPage(juz);
                userPages(username);
            }));
        }
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
