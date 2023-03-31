package com.alisoft.hatim.controller;


import com.alisoft.hatim.config.security.jwt.JwtUser;
import com.alisoft.hatim.dto.response.DashboardResponseDto;
import com.alisoft.hatim.exception.NotFoundException;
import com.alisoft.hatim.service.ReportService;
import lombok.Data;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Data
@RestController
@RequestMapping("/api/v1/report")
public class ReportController {
    private final ReportService reportService;

    @GetMapping("/dashboard")
    public DashboardResponseDto getDashboardReport(
            @AuthenticationPrincipal JwtUser jwtUser
    ) throws NotFoundException {
        return reportService.getDashboardReport(jwtUser);
    }

}
