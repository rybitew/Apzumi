package com.example.task.post;

import com.example.task.exception.InvalidParamException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(PostController.class)
class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private PostService postService;

    @Test
    void getAllPosts_Return200() throws Exception {
        var posts = List.of(
                new PostDto(1, "t1", "b1"),
                new PostDto(2, "t2", "b2"),
                new PostDto(3, "t3", "b3"),
                new PostDto(4, "t4", "b4")
        );
        Mockito.when(postService.getAll()).thenReturn(posts);
        mockMvc.perform(get("/posts/all"))
                .andExpect(status().isOk())
                .andExpect(content().string(mapper.writeValueAsString(posts)));
    }

    @Test
    void getFilteredPosts_Return200_WhenValid() throws Exception {
        var post = List.of(new PostDto(1, "title", "b1"));
        Mockito.when(postService.getByTitle("title")).thenReturn(post);
        mockMvc.perform(get("/posts/get?title=title"))
                .andExpect(status().isOk())
                .andExpect(content().string(mapper.writeValueAsString(post)));
    }

    @Test
    void getFilteredPosts_Return400_WhenInvalid() throws Exception {
        Mockito.when(postService.getByTitle("")).thenThrow(InvalidParamException.class);
        Mockito.when(postService.getByTitle("  ")).thenThrow(InvalidParamException.class);
        mockMvc.perform(get("/posts/get?title=")).andExpect(status().isBadRequest());
        mockMvc.perform(get("/posts/get?title=  ")).andExpect(status().isBadRequest());
    }

    @Test
    void editPost_Return200_WhenValid() throws Exception {
        var returnedPost = new PostDto(1, "title", "body");
        var sentPost = new PostDto(1, null, null);
        Mockito.when(postService.editPost(sentPost)).thenReturn(returnedPost);
        mockMvc.perform(put("/posts/edit").contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsBytes(sentPost)))
                .andExpect(status().isOk())
                .andExpect(content().string(mapper.writeValueAsString(returnedPost)));
    }

    @Test
    void editPost_Return400_WhenInvalid() throws Exception {
        var sentPost = new PostDto(null, "title", "body");
        Mockito.when(postService.editPost(sentPost)).thenThrow(InvalidParamException.class);
        Mockito.when(postService.editPost(null)).thenThrow(InvalidParamException.class);
        mockMvc.perform(put("/posts/edit").contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsBytes(sentPost))).andExpect(status().isBadRequest());
        mockMvc.perform(put("/posts/edit").contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsBytes(""))).andExpect(status().isBadRequest());
    }

    @Test
    void updatePosts_Return200() throws Exception {
        Mockito.when(postService.updatePosts()).thenReturn(10);
        mockMvc.perform(put("/posts/update"))
                .andExpect(status().isOk())
                .andExpect(content().string("10"));
    }

    @Test
    void deletePost_Return200_WhenValid() throws Exception {
        mockMvc.perform(delete("/posts/delete/1"))
                .andExpect(status().isOk());
    }

    @Test
    void deletePost_Return400_WhenInvalid() throws Exception {
        mockMvc.perform(delete("/posts/delete/" + null))
                .andExpect(status().isBadRequest());
    }
}