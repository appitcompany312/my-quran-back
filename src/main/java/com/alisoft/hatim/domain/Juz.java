package com.alisoft.hatim.domain;

import com.alisoft.hatim.domain.common.BaseAuditedEntity;
import com.alisoft.hatim.domain.reference.JuzStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "juz")
public class Juz extends BaseAuditedEntity {

    @Column(name = "number")
    private Integer number;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "status")
    private JuzStatus status;

    @ManyToOne
    @JoinColumn(name = "hatim_id", nullable = false)
    private Hatim hatim;
}

