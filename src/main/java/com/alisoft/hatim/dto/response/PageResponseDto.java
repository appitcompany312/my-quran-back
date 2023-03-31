package com.alisoft.hatim.dto.response;

import com.alisoft.hatim.domain.reference.PageStatus;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class PageResponseDto {
    UUID id;
    PageStatus status;
    Integer number;
    boolean isMine;
}
