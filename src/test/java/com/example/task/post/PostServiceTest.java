package com.example.task.post;

import com.example.task.exception.InvalidParamException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PostServiceTest {

    @Autowired
    private PostService postService;

    @MockBean
    private PostRepository postRepository;
    
    @Test
    void getByTitle_ThrowInvalidParamException_WhenNullOrBlank() {
        assertThrows(InvalidParamException.class, () -> postService.getByTitle(null));
        assertThrows(InvalidParamException.class, () -> postService.getByTitle(""));
        assertThrows(InvalidParamException.class, () -> postService.getByTitle(" "));
    }

    @Test
    void getByTitle_ReturnPostDto_WhenMatch() {
        List<Post> posts = List.of(
                new Post(1, 1, "ambiguous 1", ""),
                new Post(2, 2, "ambiguous 2", "")
        );
        Mockito.when(postRepository.findByTitleStartingWithIgnoreCase("ambiguous"))
                .thenReturn(posts);

        List<PostDto> postDtos = posts.stream().map(PostDto::new).collect(Collectors.toList());
        assertEquals(postDtos, postService.getByTitle("ambiguous"));
    }

    @Test
    void getByTitle_ReturnEmptyList_WhenNoMatch() {
        Mockito.when(postRepository.findByTitleStartingWithIgnoreCase("no match"))
                .thenReturn(new ArrayList<>());

        assertEquals(new ArrayList<PostDto>(), postService.getByTitle("no match"));
    }


    @Test
    void getAll() {
        Iterable<Post> posts = List.of(
                new Post(1, 1, "title 1", ""),
                new Post(2, 1, "title 2", ""),
                new Post(3, 2, "title 3", "")
        );
        Mockito.when(postRepository.findAll()).thenReturn(posts);

        List<PostDto> postDtos = StreamSupport.stream(posts.spliterator(), false)
                .map(PostDto::new).collect(Collectors.toList());
        assertEquals(postDtos, postService.getAll());
    }

    @Test
    void editPost_ThrowInvalidParamException_WhenNull() {
        assertThrows(InvalidParamException.class, () -> postService.editPost(null));

    }

    @Test
    void editPost_ThrowInvalidParamException_WhenIdNull() {
        assertThrows(InvalidParamException.class, () -> postService.editPost(new PostDto(null, "title", "body")));
    }

    @Test
    void editPost_ThrowNoSuchElementException_WhenIdNoMatch() {
        assertThrows(NoSuchElementException.class, () -> postService.editPost(new PostDto(-1, "title", "body")));
    }

    @Test
    void editPost_ReturnEditedTitle_WhenIdAndTitleNotNull() {
        Post post = new Post(1, 1, "Title", "Body");
        Mockito.when(postRepository.findById(1)).thenReturn(Optional.of(post));
        Mockito.when(postRepository.save(Mockito.any(Post.class)))
                .thenAnswer(i -> i.getArguments()[0]);

        PostDto postToEdit = new PostDto(1, "Edited title", null);
        assertEquals(new PostDto(1, postToEdit.getTitle(), post.getBody()), postService.editPost(postToEdit));
    }

    @Test
    void editPost_ReturnEditedTitle_WhenIdAndBodyNotNull() {
        Post post = new Post(1, 1, "Title", "Body");
        Mockito.when(postRepository.findById(1)).thenReturn(Optional.of(post));
        Mockito.when(postRepository.save(Mockito.any(Post.class)))
                .thenAnswer(i -> i.getArguments()[0]);

        PostDto postToEdit = new PostDto(1, null, "Edited body");
        assertEquals(new PostDto(1, post.getTitle(), postToEdit.getBody()), postService.editPost(postToEdit));
    }

    @Test
    void editPost_ReturnEditedTitle_WhenIdNotNull() {
        Post post = new Post(1, 1, "Title", "Body");
        Mockito.when(postRepository.findById(1)).thenReturn(Optional.of(post));
        Mockito.when(postRepository.save(Mockito.any(Post.class)))
                .thenAnswer(i -> i.getArguments()[0]);

        PostDto postToEdit = new PostDto(1, null, null);
        assertEquals(new PostDto(post), postService.editPost(postToEdit));
    }

    @Test
    void deletePost_ThrowInvalidParamException_WhenIdNull() {
        assertThrows(InvalidParamException.class, () -> postService.deletePost(null));
    }

    @Test
    void deletePost_NoException_WhenIdNotNull() {
        assertDoesNotThrow(() -> postService.deletePost(1));
    }
}