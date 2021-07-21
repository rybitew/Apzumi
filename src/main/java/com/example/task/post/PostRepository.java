package com.example.task.post;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends CrudRepository<Post, Integer> {
    List<Post> findByTitleStartingWithIgnoreCase(String title);

    @Query("select p.id from Post p where p.edited = false")
    List<Integer> findUneditedIds();
}
