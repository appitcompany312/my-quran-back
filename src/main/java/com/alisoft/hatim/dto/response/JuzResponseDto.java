package com.alisoft.hatim.dto.response;

import com.alisoft.hatim.domain.reference.JuzStatus;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class JuzResponseDto {
    UUID id;
    JuzStatus status;
    Integer number;
    Integer toDo;
    Integer inProgress;
    Integer done;

}
