package com.alisoft.hatim.service.reference;

import com.alisoft.hatim.domain.reference.Language;
import com.alisoft.hatim.exception.NotFoundException;

public interface LanguageService {
    Language get(Long id) throws NotFoundException;
}
