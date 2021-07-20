package com.example.task.post;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("posts")
public class PostController {

    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping(value = "/all")
    public ResponseEntity<List<PostDto>> getAllPosts() {
        return ResponseEntity.ok(postService.getAll());
    }

    @GetMapping("/get")
    public ResponseEntity<List<PostDto>> getFilteredPosts(@RequestParam("title") String title) {
        return ResponseEntity.ok(postService.getByTitle(title));
    }

    @PutMapping("/edit")
    public ResponseEntity<PostDto> editPost(@RequestBody PostDto data) {
        return ResponseEntity.ok(postService.editPost(data));
    }

    @PutMapping("/update")
    public ResponseEntity<Integer> updatePosts() {
        return ResponseEntity.ok(postService.updatePosts());
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Integer id) {
        postService.deletePost(id);
        return ResponseEntity.ok().build();
    }
}
