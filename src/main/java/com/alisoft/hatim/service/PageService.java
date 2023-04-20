package com.alisoft.hatim.service;

import com.alisoft.hatim.config.security.jwt.JwtUser;
import com.alisoft.hatim.domain.Juz;
import com.alisoft.hatim.domain.Page;
import com.alisoft.hatim.domain.User;
import com.alisoft.hatim.domain.reference.PageStatus;
import com.alisoft.hatim.exception.NotFoundException;

import java.util.List;
import java.util.UUID;

public interface PageService {

    Page get(UUID id) throws NotFoundException;
    void createPagesForJuz(Juz juz, JwtUser jwtUser) throws NotFoundException;

    Page pageToDo(Page page);

    void pagesToDo(List<Page> pages);
    Page bookPage(Page page, User user);
    Page pageInProgress(Page page, User user);
    Page pageDone(Page page, User user);

    List<Page> getAllByJuz(Juz juz);

    Boolean isAllPagesDone(Juz juz);

    List<Page> findAllByUserAndStatus(User user, PageStatus pageStatus);

    List<Page> findAllByStatus(PageStatus status);

    List<Page> findAllByJuzAndStatus(Juz juz, PageStatus status);

    List<Page> findAllWhichBookedFiveMinutesAgo();
    List<Page> findAllWhichProgressedHourAgo(int hour);
}
