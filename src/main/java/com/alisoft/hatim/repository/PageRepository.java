package com.alisoft.hatim.repository;

import com.alisoft.hatim.domain.Page;
import com.alisoft.hatim.domain.Juz;
import com.alisoft.hatim.domain.User;
import com.alisoft.hatim.domain.reference.PageStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PageRepository extends JpaRepository<Page, UUID> {
    List<Page> findAllByJuzOrderByNumber(Juz juz);

    List<Page> findAllByUserAndStatus(User user, PageStatus pageStatus);

    List<Page> findAllByStatus(PageStatus status);

    List<Page> findAllByJuzAndStatus(Juz juz, PageStatus status);
}
