package pyb.pickabook.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import pyb.pickabook.domain.Book;

/**
 * Spring Data JPA repository for the Book entity.
 */
public interface BookRepository extends JpaRepository<Book,Long> {

	@Query("SELECT b FROM Book b"
			+ " ORDER BY"
				+ " CASE WHEN b.author.id = :authorId THEN 0 ELSE 1 END, b.title") 
	List<Book> findSuggestions(@Param("authorId") Long authorId);

	List<Book> findAllByOrderByTitle();
}
