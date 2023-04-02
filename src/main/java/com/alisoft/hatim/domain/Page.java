package com.alisoft.hatim.domain;

import com.alisoft.hatim.domain.common.BaseAuditedEntity;
import com.alisoft.hatim.domain.reference.PageStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "page")
public class Page extends BaseAuditedEntity {

    @Column(name = "number")
    private Integer number;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "status")
    private PageStatus status;

    @ManyToOne
    @JoinColumn(name = "juz_id", nullable = false)
    private Juz juz;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "booked_at")
    private LocalDateTime bookedAt;

    @Column(name = "progressed_at")
    private LocalDateTime progressedAt;
}
