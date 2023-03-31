package com.alisoft.hatim.service.impl;

import com.alisoft.hatim.config.security.jwt.JwtUser;
import com.alisoft.hatim.domain.User;
import com.alisoft.hatim.domain.reference.HatimStatus;
import com.alisoft.hatim.domain.reference.PageStatus;
import com.alisoft.hatim.dto.response.DashboardResponseDto;
import com.alisoft.hatim.exception.NotFoundException;
import com.alisoft.hatim.service.HatimService;
import com.alisoft.hatim.service.PageService;
import com.alisoft.hatim.service.ReportService;
import com.alisoft.hatim.service.UserService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Data
@Service
@Slf4j
public class ReportServiceImpl implements ReportService {

    private final UserService userService;
    private final HatimService hatimService;
    private final PageService pageService;

    @Override
    public DashboardResponseDto getDashboardReport(JwtUser jwtUser) throws NotFoundException {
        User user = userService.get(jwtUser.getId());
        return DashboardResponseDto.builder()
                .allDoneHatims(hatimService.findAllByStatus(HatimStatus.DONE).size())
                .allDonePages(pageService.findAllByStatus(PageStatus.DONE).size())
                .donePages(pageService.findAllByUserAndStatus(user, PageStatus.DONE).size())
                .build();
    }
}
