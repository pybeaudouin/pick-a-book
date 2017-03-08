package pyb.pickabook.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import pyb.pickabook.domain.Book;

/**
 * Spring Data JPA repository for the Book entity.
 */
public interface BookRepository extends JpaRepository<Book,Long> {

	// FIXME: to be replaced with a Query object
	List<Book> findSuggestions(String orderBy);
}
