package it.test.testspringboot;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import it.test.testspringboot.model.Author;
import it.test.testspringboot.model.Comment;
import it.test.testspringboot.model.Post;
import it.test.testspringboot.repository.AuthorRepository;
import it.test.testspringboot.repository.CommentRepository;
import it.test.testspringboot.repository.PagingAndSortingCommentRepository;
import it.test.testspringboot.repository.PagingAndSortingPostRepository;
import it.test.testspringboot.repository.PostRepository;
import jakarta.persistence.EntityManager;


@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class TestSpringBootApplicationTests {

	@Autowired
	private EntityManager entityManager;

	@Autowired
	private AuthorRepository authorRepository;

	@Autowired
	private PostRepository postRepository;

	@Autowired
	private CommentRepository commentRepository;

	@Autowired
	private PagingAndSortingPostRepository pAndSPostRepository;

	@Autowired
	private PagingAndSortingCommentRepository pAndSCommentRepository;

	@BeforeEach
	public void createData() {
		Author a = new Author();
		a.setFirstname("Cosimo");
		a.setLastname("Di Lorenzo");
		a.setEmail("cosimo@cosimo.it");

		entityManager.persist(a);

		Post p = new Post();
		p.setAuthor(a);
		p.setTitle("Il nuovo iphone 15");
		p.setBody("Il miglior iphone mai creato");
		p.setPublishDate("20230411");

		Post p1 = new Post();
		p1.setAuthor(a);
		p1.setTitle("Aliexpress");
		p1.setBody("lorem ipsum");
		p1.setPublishDate("20230411");

		entityManager.persist(p);
		entityManager.persist(p1);

		Comment c1 = new Comment();
		c1.setEmail("luca@luca.it");
		c1.setPost(p);
		c1.setBody("non mi piace l'iphone");
		c1.setDate("20231104");

		Comment c2 = new Comment();
		c2.setEmail("luca@luca.it");
		c2.setPost(p);
		c2.setBody("mi piace");
		c2.setDate("20231204");

		Comment c3 = new Comment();
		c3.setEmail("alberto@alberto.it");
		c3.setPost(p);
		c3.setBody("mi piace");
		c3.setDate("20231504");


		entityManager.persist(c1);
		entityManager.persist(c2);
		entityManager.persist(c3);

	}

	@Test
	void initialCheck() {
		List<Author> authors = entityManager.createQuery("SELECT a FROM Author a", Author.class).getResultList();

		assertThat(authors).hasSize(1);
	}

	@Test
	void postCheck() {
		List<Post> posts = entityManager.createQuery("SELECT p FROM Post p", Post.class).getResultList();
		assertThat(posts).hasSize(1);
	}

	@Test
	void commentCheck() {
		List<Comment> comments = entityManager.createQuery("SELECT c FROM Comment c", Comment.class).getResultList();
		assertThat(comments).hasSize(2);
	}

	@Test
	void commentForPostCheck() {
		List<Post> posts = entityManager.createQuery("SELECT p FROM Post p", Post.class).getResultList();
		List<Comment> commentsPost1 = entityManager.createQuery("SELECT c FROM Comment c WHERE c.post.id = ?1", Comment.class).setParameter(1, posts.get(0).getId()).getResultList();

		assertThat(commentsPost1).hasSize(2);
	}

	@Test
	void authorRepositoryCheck() {
		assertThat(authorRepository.count()).isEqualTo(1);
		assertThat(authorRepository.findAll()).hasSize(1);
	}

	@Test
	void findByLastname() {
		assertThat(authorRepository.findByLastname("Di Lorenzo")).first().extracting("lastname").isEqualTo("Di Lorenzo");
	}

	@Test
	void findByTitle() {
		assertThat(postRepository.findByTitle("Il nuovo iphone 15")).first().extracting("title").isEqualTo("Il nuovo iphone 15");
	}

	@Test
	void findByEmail() {
		assertThat(commentRepository.findByEmail("luca@luca.it")).first().extracting("email").isEqualTo("luca@luca.it");
	}

	@Test
	void findByDate() {
		assertThat(commentRepository.findByDate("20231104")).first().extracting("date").isEqualTo("20231104");		
	}

	@Test
	void sortByTitle() {
		assertThat(pAndSPostRepository.findAll(Sort.by("title"))).first().extracting("title").isEqualTo("Aliexpress");
	}

	@Test
	void sortByEmailAndDate() {
		assertThat(pAndSCommentRepository.findAll(Sort.by("email", "date"))).extracting("email").contains("alberto@alberto.it", atIndex(0)).contains("luca@luca.it", atIndex(2));
	}

	@Test
	void paging() {
		commentRepository.deleteAll();

		for (int i = 0; i < 1000 ; i++) {
			Comment c = new Comment();
			c.setEmail(String.valueOf(i));
			c.setBody(String.valueOf(i));
			c.setDate(String.valueOf(i));

			commentRepository.save(c);
		}

		assertThat(commentRepository.count()).isEqualTo(1000);
		assertThat(pAndSCommentRepository.findAll(PageRequest.of(13,10)).getContent()).extracting("email").contains("135",atIndex(5));

	}



}
