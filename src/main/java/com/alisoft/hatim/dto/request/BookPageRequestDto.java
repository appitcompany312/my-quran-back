package com.alisoft.hatim.dto.request;

import lombok.Data;

import java.util.UUID;

@Data
public class BookPageRequestDto {
    private UUID pageId;
    private String username;
}
