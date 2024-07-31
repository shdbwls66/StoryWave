package com.ormi.storywave.category;

import com.ormi.storywave.board.BoardDto;
import lombok.*;

import java.io.Serializable;

/** DTO for {@link Category} */

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CategoryDto implements Serializable {
  private Long categoryId;
  private BoardDto board;
  private String categoryName;
}
