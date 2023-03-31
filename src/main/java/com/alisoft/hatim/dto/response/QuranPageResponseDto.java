package com.alisoft.hatim.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class QuranPageResponseDto {
    String verse_key;
    String text_uthmani;
}
