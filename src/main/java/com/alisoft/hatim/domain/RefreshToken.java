package com.alisoft.hatim.domain;

import com.alisoft.hatim.domain.common.BaseAuditedEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "refresh_token")
public class RefreshToken extends BaseAuditedEntity {

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
}
