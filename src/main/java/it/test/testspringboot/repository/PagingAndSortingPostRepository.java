package it.test.testspringboot.repository;

import org.springframework.data.repository.ListPagingAndSortingRepository;

import it.test.testspringboot.model.Post;

public interface PagingAndSortingPostRepository extends ListPagingAndSortingRepository<Post,Long>{
        
}
