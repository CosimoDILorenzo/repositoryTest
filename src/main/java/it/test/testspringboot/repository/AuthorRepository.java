package it.test.testspringboot.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import it.test.testspringboot.model.Author;

public interface AuthorRepository extends JpaRepository<Author, Long>{
    public List<Author> findByLastname(String lastname);
}
