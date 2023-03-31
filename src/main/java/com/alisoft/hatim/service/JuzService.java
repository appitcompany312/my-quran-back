package com.alisoft.hatim.service;

import com.alisoft.hatim.config.security.jwt.JwtUser;
import com.alisoft.hatim.domain.Hatim;
import com.alisoft.hatim.domain.Juz;
import com.alisoft.hatim.exception.NotFoundException;

import java.util.List;
import java.util.UUID;

public interface JuzService {

    Juz get(UUID id) throws NotFoundException;

    void createJuzsForHatim(Hatim hatim, JwtUser jwtUser) throws NotFoundException;

    List<Juz> getByHatim(Hatim hatim);

    void setJuzStatusInProgress(Juz juz);

    void setJuzStatusDone(Juz juz);

    Boolean isAllJuzsDone(Hatim hatim);

    Boolean isJuzExistsToDoPage(Hatim hatim);
}
