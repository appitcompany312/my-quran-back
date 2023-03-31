package com.alisoft.hatim.service.impl.reference;

import com.alisoft.hatim.domain.reference.Language;
import com.alisoft.hatim.exception.NotFoundException;
import com.alisoft.hatim.repository.reference.LanguageRepository;
import com.alisoft.hatim.service.reference.LanguageService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Data
@Service
@Slf4j
public class LanguageServiceImpl implements LanguageService {
    private final LanguageRepository languageRepository;

    @Override
    @Transactional(readOnly = true)
    public Language get(Long id) throws NotFoundException {
        return languageRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(Language.class, id, "id"));
    }
}
