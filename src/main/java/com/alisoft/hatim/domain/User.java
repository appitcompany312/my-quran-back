package com.alisoft.hatim.domain;

import com.alisoft.hatim.domain.common.BaseEntity;
import com.alisoft.hatim.domain.reference.Gender;
import com.alisoft.hatim.domain.reference.Language;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User extends BaseEntity {

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "gender")
    private Gender gender;

    @ManyToOne
    @JoinColumn(name = "language_id", nullable = false)
    private Language language;

    @JoinColumn(name = "role_id")
    @ManyToOne(fetch = FetchType.EAGER)
    private Role role;

    @Column(name = "language_code")
    private String languageCode;
}
