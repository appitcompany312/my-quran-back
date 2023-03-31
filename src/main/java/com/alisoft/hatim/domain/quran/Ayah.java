package com.alisoft.hatim.domain.quran;

import com.alisoft.hatim.domain.common.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "ayah")
public class Ayah extends BaseEntity {

    @Column(name = "number")
    private Integer number;

    @Column(name = "ayah")
    private String ayah;

    @ManyToOne
    @JoinColumn(name = "quran_page_id", nullable = false)
    private QuranPage quranPage;

    @ManyToOne
    @JoinColumn(name = "surah_id", nullable = false)
    private Surah surah;

    @Column(name = "sajda")
    private Boolean sajda;

    @Column(name = "ruku")
    private Integer ruku;

    @Column(name = "hizb_quarter")
    private Integer hizbQuarter;
}
