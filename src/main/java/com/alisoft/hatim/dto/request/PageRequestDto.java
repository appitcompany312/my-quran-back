package com.alisoft.hatim.dto.request;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class PageRequestDto {
    private List<UUID> pageIds;
    private String username;
}
