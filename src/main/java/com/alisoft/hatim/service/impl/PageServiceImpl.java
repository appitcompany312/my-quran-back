package com.alisoft.hatim.service.impl;

import com.alisoft.hatim.config.security.jwt.JwtUser;
import com.alisoft.hatim.domain.Juz;
import com.alisoft.hatim.domain.Page;
import com.alisoft.hatim.domain.User;
import com.alisoft.hatim.domain.reference.PageStatus;
import com.alisoft.hatim.exception.NotFoundException;
import com.alisoft.hatim.repository.PageRepository;
import com.alisoft.hatim.service.PageService;
import com.alisoft.hatim.service.UserService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Data
@Service
@Slf4j
public class PageServiceImpl implements PageService {

    private final PageRepository pageRepository;
    private final UserService userService;

    @Override
    @Transactional(readOnly = true)
    public Page get(UUID id) throws NotFoundException {
        return pageRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(Page.class, id, "id"));
    }

    @Override
    @Transactional
    public void createPagesForJuz(Juz juz, JwtUser jwtUser) throws NotFoundException {
        int number = 1;
        int pageSize = 20;
        if (juz.getNumber() == 1) pageSize = 21;
        if (juz.getNumber() == 30) pageSize = 23;
        for (int i = number; i <= pageSize; i++) {
            Page page = new Page();
            if (juz.getNumber() == 1) {
                page.setNumber(number);
            } else {
                page.setNumber(((juz.getNumber()-1) * 20) + number + 1);
            }
            page.setStatus(PageStatus.TODO);
            page.setJuz(juz);
            page.setUser(null);
            pageRepository.save(page);
            number++;
        }
    }

    @Override
    @Transactional
    public Page pageToDo(Page page) {
        page.setStatus(PageStatus.TODO);
        page.setUser(null);
        return pageRepository.save(page);
    }

    @Override
    @Transactional
    public Page bookPage(Page page, User user) {
        page.setStatus(PageStatus.BOOKED);
        page.setUser(user);
        return pageRepository.save(page);
    }

    @Override
    @Transactional
    public Page pageInProgress(Page page, User user) {
        page.setStatus(PageStatus.IN_PROGRESS);
        page.setUser(user);
        return pageRepository.save(page);
    }

    @Override
    @Transactional
    public Page pageDone(Page page, User user) {
        page.setStatus(PageStatus.DONE);
        page.setUser(user);
        return pageRepository.save(page);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Page> getAllByJuz(Juz juz) {
        return pageRepository.findAllByJuzOrderByNumber(juz);
    }

    @Override
    @Transactional(readOnly = true)
    public Boolean isAllPagesDone(Juz juz) {
        List<Page> pages = pageRepository.findAllByJuzOrderByNumber(juz);
        for (Page page: pages) {
            if (page.getStatus() != PageStatus.DONE) return false;
        }
        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Page> findAllByUserAndStatus(User user, PageStatus pageStatus) {
        return pageRepository.findAllByUserAndStatus(user, pageStatus);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Page> findAllByStatus(PageStatus status) {
        return pageRepository.findAllByStatus(status);
    }

    @Override
    public List<Page> findAllByJuzAndStatus(Juz juz, PageStatus status) {
        return pageRepository.findAllByJuzAndStatus(juz, status);
    }
}
