package com.lion.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.BindParam;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookEsDto {
    private BookEs bookEs;
    private float matchScore;
}
