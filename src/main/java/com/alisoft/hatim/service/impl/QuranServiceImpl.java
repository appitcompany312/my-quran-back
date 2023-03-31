package com.alisoft.hatim.service.impl;

import com.alisoft.hatim.config.security.jwt.JwtUser;
import com.alisoft.hatim.domain.Page;
import com.alisoft.hatim.domain.User;
import com.alisoft.hatim.domain.quran.Ayah;
import com.alisoft.hatim.domain.quran.QuranPage;
import com.alisoft.hatim.domain.reference.PageStatus;
import com.alisoft.hatim.dto.response.QuranPageResponseDto;
import com.alisoft.hatim.exception.NotFoundException;
import com.alisoft.hatim.mapper.QuranMapper;
import com.alisoft.hatim.repository.quran.AyahRepository;
import com.alisoft.hatim.repository.quran.QuranPageRepository;
import com.alisoft.hatim.repository.quran.SurahRepository;
import com.alisoft.hatim.service.PageService;
import com.alisoft.hatim.service.QuranService;
import com.alisoft.hatim.service.UserService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Data
@Service
@Slf4j
public class QuranServiceImpl implements QuranService {

    private final QuranPageRepository quranPageRepository;
    private final SurahRepository surahRepository;
    private final AyahRepository ayahRepository;
    private final UserService userService;
    private final PageService pageService;
    private final QuranMapper quranMapper;

    @Override
    public List<QuranPageResponseDto> getInProgressPages(JwtUser jwtUser) throws NotFoundException {
        List<QuranPageResponseDto> quranPageResponseDtos = new ArrayList<>();
        User user = userService.get(jwtUser.getId());
        List<Page> inProgressPages = pageService.findAllByUserAndStatus(user, PageStatus.IN_PROGRESS);
        for (Page page: inProgressPages) {
            QuranPage quranPage = quranPageRepository.findByJuzAndPage(page.getJuz().getNumber(), page.getNumber());
            List<Ayah> ayahs = ayahRepository.findAllByQuranPage(quranPage);
            quranPageResponseDtos.addAll(quranMapper.ayahsToQuranPageResponseDtos(ayahs));
        }
        return quranPageResponseDtos;
    }
}
