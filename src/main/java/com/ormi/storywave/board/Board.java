package com.ormi.storywave.board;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ormi.storywave.category.Category;
import com.ormi.storywave.posts.Posts;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "board")
@JsonIgnoreProperties({"posts", "categories"}) // 순환 참조 방지
public class Board {
    @Id
    @Column(name = "post_type_id")
    private Long postTypeId;

    private Integer viewPost;

    @OneToMany(mappedBy = "board")
    private Set<Posts> posts;

    @OneToMany(mappedBy = "board")
    private Set<Category> categories;
}
