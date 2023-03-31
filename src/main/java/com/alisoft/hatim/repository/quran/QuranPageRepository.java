package com.alisoft.hatim.repository.quran;

import com.alisoft.hatim.domain.quran.QuranPage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuranPageRepository extends JpaRepository<QuranPage, Long> {
    QuranPage findByJuzAndPage(Integer juz, Integer page);
}
