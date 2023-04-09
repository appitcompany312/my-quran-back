package com.alisoft.hatim.repository;

import com.alisoft.hatim.domain.Hatim;
import com.alisoft.hatim.domain.reference.HatimStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface HatimRepository extends JpaRepository<Hatim, UUID> {
    List<Hatim> findAllByStatus(HatimStatus status);
    List<Hatim> findAllByStatusOrderByCreatedAt(HatimStatus status);

}
