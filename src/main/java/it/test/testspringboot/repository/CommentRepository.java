package it.test.testspringboot.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import it.test.testspringboot.model.Comment;

public interface CommentRepository extends JpaRepository<Comment,Long>{
    public List<Comment> findByEmail(String email);
    public List<Comment> findByDate(String date);
}
