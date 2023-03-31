package com.alisoft.hatim.dto.response;

import com.alisoft.hatim.domain.reference.HatimStatus;
import com.alisoft.hatim.domain.reference.HatimType;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class HatimResponseDto {
    UUID id;
    HatimStatus status;
    HatimType type;
}
