package com.alisoft.hatim.domain.common;

import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@MappedSuperclass
public abstract class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        BaseEntity that = (BaseEntity) o;

        return Objects.equals(id, that.id);
    }

    public boolean isPersisted() {
        return Objects.nonNull(this.id) && this.id > 0;
    }

    @Override
    public int hashCode() {
        var tempId = -1;

        if (Objects.nonNull(this.id)) {
            tempId = this.id.intValue();
        }

        return tempId * 5;
    }
}
