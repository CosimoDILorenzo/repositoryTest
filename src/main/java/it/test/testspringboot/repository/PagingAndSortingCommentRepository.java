package it.test.testspringboot.repository;

import org.springframework.data.repository.ListPagingAndSortingRepository;

import it.test.testspringboot.model.Comment;

public interface PagingAndSortingCommentRepository extends ListPagingAndSortingRepository<Comment,Long>{
    
}
