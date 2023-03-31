package com.alisoft.hatim.service;

import com.alisoft.hatim.config.security.jwt.JwtUser;
import com.alisoft.hatim.dto.response.DashboardResponseDto;
import com.alisoft.hatim.exception.NotFoundException;

public interface ReportService {
    DashboardResponseDto getDashboardReport(JwtUser jwtUser) throws NotFoundException;
}
