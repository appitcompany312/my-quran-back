package com.alisoft.hatim.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DashboardResponseDto {
    Integer allDoneHatims;
    Integer allDonePages;
    Integer donePages;

}
