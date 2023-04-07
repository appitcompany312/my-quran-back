package com.alisoft.hatim.service.impl;

import com.alisoft.hatim.config.security.jwt.JwtUser;
import com.alisoft.hatim.domain.Hatim;
import com.alisoft.hatim.domain.Juz;
import com.alisoft.hatim.domain.reference.JuzStatus;
import com.alisoft.hatim.domain.reference.PageStatus;
import com.alisoft.hatim.exception.NotFoundException;
import com.alisoft.hatim.repository.JuzRepository;
import com.alisoft.hatim.service.JuzService;
import com.alisoft.hatim.service.PageService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Data
@Service
@Slf4j
public class JuzServiceImpl implements JuzService {

    private static final int MAX_JUZ_NUMBER = 30;

    private final JuzRepository juzRepository;
    private final PageService pageService;

    @Override
    @Transactional(readOnly = true)
    public Juz get(UUID id) throws NotFoundException {
        return juzRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(Juz.class, id, "id"));
    }

    @Override
    @Transactional
    public void createJuzsForHatim(Hatim hatim, JwtUser jwtUser) throws NotFoundException {
        int number = 1;
        for (int i = number; i <= MAX_JUZ_NUMBER; i++) {
            Juz juz = new Juz();
            juz.setHatim(hatim);
            juz.setNumber(number);
            juz.setStatus(JuzStatus.TODO);
            juzRepository.save(juz);
            number++;
            pageService.createPagesForJuz(juz, jwtUser);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Juz> getByHatim(Hatim hatim) {
        return juzRepository.findAllByHatimOrderByNumber(hatim);
    }

    @Override
    @Transactional
    public void setJuzStatusInProgress(Juz juz) {
        juz.setStatus(JuzStatus.IN_PROGRESS);
        juzRepository.save(juz);
    }

    @Override
    @Transactional
    public void setJuzStatusDone(Juz juz) {
        juz.setStatus(JuzStatus.DONE);
        juzRepository.save(juz);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isAllJuzsDone(Hatim hatim) {
        List<Juz> juzs = juzRepository.findAllByHatimOrderByNumber(hatim);
        return juzs.stream()
                .noneMatch(juz -> juz.getStatus() != JuzStatus.DONE);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isJuzExistsToDoPage(Hatim hatim) {
        List<Juz> juzs = juzRepository.findAllByHatimOrderByNumber(hatim);
        return juzs.stream()
                .flatMap(juz -> pageService.getAllByJuz(juz).stream())
                .anyMatch(page -> page.getStatus() == PageStatus.TODO);
    }
}
