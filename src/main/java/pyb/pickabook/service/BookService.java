package pyb.pickabook.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import pyb.pickabook.service.dto.BookDTO;
import pyb.pickabook.service.dto.preference.ReadingPreferencesDTO;
import pyb.pickabook.service.impl.InvalidPreferenceException;


/**
 * Service Interface for managing Book.
 */
public interface BookService {

    /**
     * Save a book.
     *
     * @param bookDTO the entity to save
     * @return the persisted entity
     */
    BookDTO save(BookDTO bookDTO);

    /**
     *  Get all the books.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<BookDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" book.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    BookDTO findOne(Long id);

    /**
     *  Delete the "id" book.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
	 * Give book suggestions based on reading preferences.
	 * 
	 * @throws InvalidPreferenceException
	 */
	List<BookDTO> findSuggestions(ReadingPreferencesDTO readingPreferences) throws InvalidPreferenceException;
}
