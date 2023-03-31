package com.alisoft.hatim.domain.quran;

import com.alisoft.hatim.domain.common.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "quran_page")
public class QuranPage extends BaseEntity {

    @Column(name = "juz")
    private Integer juz;

    @Column(name = "page")
    private Integer page;
}
