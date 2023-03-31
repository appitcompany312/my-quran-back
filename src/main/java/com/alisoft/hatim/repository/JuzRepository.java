package com.alisoft.hatim.repository;

import com.alisoft.hatim.domain.Hatim;
import com.alisoft.hatim.domain.Juz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface JuzRepository extends JpaRepository<Juz, UUID> {
    List<Juz> findAllByHatimOrderByNumber(Hatim hatim);
}
