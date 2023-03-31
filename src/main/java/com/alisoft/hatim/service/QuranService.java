package com.alisoft.hatim.service;

import com.alisoft.hatim.config.security.jwt.JwtUser;
import com.alisoft.hatim.dto.response.QuranPageResponseDto;
import com.alisoft.hatim.exception.NotFoundException;

import java.util.List;

public interface QuranService {

    List<QuranPageResponseDto> getInProgressPages(JwtUser jwtUser) throws NotFoundException;
}
