package com.example.task.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostDto {
    private Integer id;
    private String title;
    private String body;

    public PostDto(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.body = post.getBody();
    }
}
