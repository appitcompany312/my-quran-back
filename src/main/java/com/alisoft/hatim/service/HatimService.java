package com.alisoft.hatim.service;

import com.alisoft.hatim.config.security.jwt.JwtUser;
import com.alisoft.hatim.domain.Hatim;
import com.alisoft.hatim.domain.reference.HatimStatus;
import com.alisoft.hatim.dto.response.HatimResponseDto;
import com.alisoft.hatim.dto.response.JuzResponseDto;
import com.alisoft.hatim.exception.NotFoundException;

import java.util.List;
import java.util.UUID;


public interface HatimService {

    Hatim get(UUID id) throws NotFoundException;

    HatimResponseDto joinToHatim(JwtUser jwtUser) throws NotFoundException;

    void setHatimStatusDone(Hatim hatim);

    List<Hatim> findAll();
    List<Hatim> findAllByStatus(HatimStatus status);
    List<JuzResponseDto> getJuzByHatim(UUID id);

    Hatim saveGeneralHatim();

}
