package com.alisoft.hatim.domain.reference;

import com.alisoft.hatim.domain.common.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "language")
public class Language extends BaseEntity {

    @Column(name = "code")
    private String code;

    @Column(name = "title")
    private String title;
}
