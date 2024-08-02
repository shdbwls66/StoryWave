package com.ormi.storywave.board;

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