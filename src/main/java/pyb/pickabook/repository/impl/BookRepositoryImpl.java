package pyb.pickabook.repository.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import pyb.pickabook.domain.Book;
import pyb.pickabook.repository.BookRepository;

public class BookRepositoryImpl extends SimpleJpaRepository<Book, Long> implements BookRepository {

	public BookRepositoryImpl(EntityManager em) {
		super(Book.class, em);
	}

	@PersistenceContext
	private EntityManager em;

	// FIXME: use Query object
	@Override
	public List<Book> findSuggestions(String orderBy) {
		return em.createQuery("SELECT b FROM Book b ORDER BY " + orderBy).getResultList();
	}
}
