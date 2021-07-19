package com.example.task.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class PostService {
    private final PostRepository postRepository;

    @Autowired
    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public List<PostDto> getByTitle(String title) {
        return new LinkedList<>();
    }

    public List<PostDto> getAll() {
        return new LinkedList<>();
    }

    public PostDto editPost(PostDto data) {
        return null;
    }

    public void deletePost(Integer id) {
    }

    public void updatePosts() {

    }
}
