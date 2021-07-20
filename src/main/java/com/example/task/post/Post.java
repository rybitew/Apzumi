package com.example.task.post;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Post {
    @Id
    @Column
    private Integer id;

    @Column
    private Integer userId;

    @Column
    private String title;

    @Column
    private String body;

    @Column
    private Boolean edited = false;

    public Post(Integer id, Integer userId, String title, String body) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.body = body;
    }
}
