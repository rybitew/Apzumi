package com.example.task.user;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
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
}
