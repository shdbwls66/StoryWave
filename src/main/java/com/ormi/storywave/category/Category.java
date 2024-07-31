package com.ormi.storywave.category;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.ormi.storywave.board.Board;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "category")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long categoryId;

    @ManyToOne
    @JoinColumn(name = "post_type_id")
    @JsonBackReference // 순환 참조 방지
    private Board board;

    @Column(name = "category_name")
    private String categoryName;
}