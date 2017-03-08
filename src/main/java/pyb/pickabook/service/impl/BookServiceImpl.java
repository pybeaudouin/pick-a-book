package pyb.pickabook.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pyb.pickabook.domain.Book;
import pyb.pickabook.repository.BookRepository;
import pyb.pickabook.service.BookService;
import pyb.pickabook.service.dto.BookDTO;
import pyb.pickabook.service.dto.preference.AbstractPreferenceDTO;
import pyb.pickabook.service.dto.preference.AuthorPreferenceDTO;
import pyb.pickabook.service.dto.preference.ReadingPreferencesDTO;
import pyb.pickabook.service.mapper.BookMapper;

/**
 * Service Implementation for managing Book.
 */
@Service
@Transactional
public class BookServiceImpl implements BookService{

    private final Logger log = LoggerFactory.getLogger(BookServiceImpl.class);

    private final BookRepository bookRepository;

    private final BookMapper bookMapper;

	private static final Map<Class<? extends AbstractPreferenceDTO>, CriteriaBuilder<? extends AbstractPreferenceDTO>> criteriaBuilders = new HashMap<>();
	static {
		criteriaBuilders.put(AuthorPreferenceDTO.class, new AuthorCriteriaBuilder());
	}

    public BookServiceImpl(BookRepository bookRepository, BookMapper bookMapper) {
        this.bookRepository = bookRepository;
        this.bookMapper = bookMapper;
    }

    /**
     * Save a book.
     *
     * @param bookDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public BookDTO save(BookDTO bookDTO) {
        log.debug("Request to save Book : {}", bookDTO);
        Book book = bookMapper.bookDTOToBook(bookDTO);
        book = bookRepository.save(book);
        BookDTO result = bookMapper.bookToBookDTO(book);
        return result;
    }

    /**
     *  Get all the books.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<BookDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Books");
        Page<Book> result = bookRepository.findAll(pageable);
        return result.map(book -> bookMapper.bookToBookDTO(book));
    }

    /**
     *  Get one book by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public BookDTO findOne(Long id) {
        log.debug("Request to get Book : {}", id);
        Book book = bookRepository.findOne(id);
        BookDTO bookDTO = bookMapper.bookToBookDTO(book);
        return bookDTO;
    }

    /**
     *  Delete the  book by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Book : {}", id);
        bookRepository.delete(id);
    }

	@Override
	public List<BookDTO> findSuggestions(ReadingPreferencesDTO readingPreferences) throws InvalidPreferenceException {
		log.debug("Request for suggestions : {}", readingPreferences);

		// TODO: to be plugged with BookRepository
		buildSuggestionQuery(readingPreferences);

		List<Book> suggestions;
		List<AbstractPreferenceDTO> preferences = readingPreferences.buildCriteriaListRanked();
		if (preferences.isEmpty()) {
			suggestions = bookRepository.findAllByOrderByTitle();
		} else {
			suggestions = bookRepository.findSuggestions(readingPreferences.getAuthorPreference().getAuthor().getId());
		}

		return bookMapper.booksToBookDTOs(suggestions);
	}

}
