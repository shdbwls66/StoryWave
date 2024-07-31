package com.example.storywave.Dto;

import com.example.storywave.Entity.Category;
import lombok.*;

import java.io.Serializable;

/**
 * DTO for {@link Category}
 */
@Value
@NoArgsConstructor(force = true)
@AllArgsConstructor
@Getter
@Setter
public class CategoryDto implements Serializable {
    Long id;
    BoardDto board;
    String name;
}