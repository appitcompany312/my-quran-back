package com.alisoft.hatim.repository.quran;

import com.alisoft.hatim.domain.quran.Surah;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SurahRepository extends JpaRepository<Surah, Long> {
}
