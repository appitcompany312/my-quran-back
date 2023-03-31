package com.alisoft.hatim.repository.reference;

import com.alisoft.hatim.domain.reference.Language;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LanguageRepository extends JpaRepository<Language, Long> {
}
