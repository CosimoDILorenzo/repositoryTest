package it.test.testspringboot.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import it.test.testspringboot.model.Post;

public interface PostRepository extends JpaRepository<Post,Long>{
    public List<Post> findByTitle(String title);
}
