package com.alisoft.hatim.repository.quran;

import com.alisoft.hatim.domain.quran.Ayah;
import com.alisoft.hatim.domain.quran.QuranPage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AyahRepository extends JpaRepository<Ayah, Long> {

    List<Ayah> findAllByQuranPage(QuranPage quranPage);
}
