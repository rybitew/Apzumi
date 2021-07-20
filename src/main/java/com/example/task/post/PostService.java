package com.example.task.post;

import com.example.task.exception.InvalidParamException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class PostService {
    private final PostRepository postRepository;
    private final RestTemplate restTemplate;

    @Value("${postUrl}")
    private String postUrl;

    public PostService(PostRepository postRepository, RestTemplateBuilder restTemplateBuilder) {
        this.postRepository = postRepository;
        this.restTemplate = restTemplateBuilder.build();
    }

    public List<PostDto> getByTitle(String title) {
        if (title == null || title.isBlank()) throw new InvalidParamException();

        return postRepository.findByTitleStartingWithIgnoreCase(title).stream()
                .map(PostDto::new).collect(Collectors.toList());
    }

    public List<PostDto> getAll() {
        return StreamSupport.stream(postRepository.findAll().spliterator(), false)
                .map(PostDto::new).collect(Collectors.toList());
    }

    public PostDto editPost(PostDto data) {
        if (data == null || data.getId() == null) throw new InvalidParamException();

        Post post = postRepository.findById(data.getId()).orElseThrow();

        if (data.getTitle() != null) post.setTitle(data.getTitle());
        if (data.getBody() != null) post.setBody(data.getBody());
        if (data.getTitle() != null || data.getBody() != null) post.setEdited(true);

        return new PostDto(postRepository.save(post));
    }

    public void deletePost(Integer id) {
        if (id == null) throw new InvalidParamException();

        postRepository.deleteById(id);
    }

    @Scheduled(fixedDelay = 24 * 60 * 60 * 1000)
    public Integer updatePosts() {
        Post[] response = restTemplate.getForObject(postUrl, Post[].class);
        System.out.println("updating");
        List<Integer> uneditedPosts = postRepository.findUneditedIds();

        List<Post> postsToUpdate;
        if (uneditedPosts.size() == 0) {
            postsToUpdate = Optional.ofNullable(response).map(Arrays::asList)
                    .orElseGet(ArrayList::new);
        } else {
            postsToUpdate = Optional.ofNullable(response).map(Arrays::asList)
                    .orElseGet(ArrayList::new).stream()
                    .filter(p -> uneditedPosts.contains(p.getId()))
                    .collect(Collectors.toList());
        }

        postRepository.saveAll(postsToUpdate);
        return postsToUpdate.size();
    }
}
