package com.alisoft.hatim.domain;

import com.alisoft.hatim.domain.common.BaseAuditedEntity;
import com.alisoft.hatim.domain.reference.HatimStatus;
import com.alisoft.hatim.domain.reference.HatimType;
import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
@Table(name = "hatim")
public class Hatim extends BaseAuditedEntity {

    @Enumerated(value = EnumType.STRING)
    @Column(name = "hatim_type")
    private HatimType type;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "status")
    private HatimStatus status;
}
