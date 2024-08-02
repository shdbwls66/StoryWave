package com.ormi.storywave.board;
import lombok.*;

import java.io.Serializable;

/**
 * DTO for {@link Board}
 */
@Value
@NoArgsConstructor(force = true)
@AllArgsConstructor
@Getter
@Setter
public class BoardDto implements Serializable {
    Long postTypeId;
    Integer viewPost;
}