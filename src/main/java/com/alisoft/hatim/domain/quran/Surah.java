package com.alisoft.hatim.domain.quran;

import com.alisoft.hatim.domain.common.BaseEntity;
import com.alisoft.hatim.domain.reference.RevelationType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "surah")
public class Surah extends BaseEntity {

    @Column(name = "number")
    private Integer number;

    @Column(name = "name")
    private String name;

    @Column(name = "english_name")
    private String englishName;

    @Column(name = "english_name_translation")
    private String englishNameTranslation;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "revelation_type")
    private RevelationType revelationType;

    @Column(name = "number_of_ayahs")
    private Integer numberOfAyahs;
}
