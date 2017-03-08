package pyb.pickabook.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import javax.persistence.EntityManager;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultHandler;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import pyb.pickabook.PickabookApp;
import pyb.pickabook.domain.Author;
import pyb.pickabook.domain.Book;
import pyb.pickabook.domain.enumeration.BookGenre;
import pyb.pickabook.repository.AuthorRepository;
import pyb.pickabook.repository.BookRepository;
import pyb.pickabook.service.BookService;
import pyb.pickabook.service.dto.BookDTO;
import pyb.pickabook.service.dto.preference.ReadingPreferencesDTO;
import pyb.pickabook.service.mapper.AuthorMapper;
import pyb.pickabook.service.mapper.BookMapper;
import pyb.pickabook.web.rest.errors.ExceptionTranslator;
/**
 * Test class for the BookResource REST controller.
 *
 * @see BookResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PickabookApp.class)
public class BookResourceIntTest {

	private final Logger log = LoggerFactory.getLogger(BookResourceIntTest.class);

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

	private static final BookGenre DEFAULT_GENRE = BookGenre.EDUCATION;
    private static final BookGenre UPDATED_GENRE = BookGenre.FANTASY;

    private static final Integer DEFAULT_NB_PAGES = 1;
    private static final Integer UPDATED_NB_PAGES = 2;

    private static final Integer DEFAULT_PUBLICATION_YEAR = -220;
    private static final Integer UPDATED_PUBLICATION_YEAR = -219;

    private static final Integer DEFAULT_RATING = 1;
    private static final Integer UPDATED_RATING = 2;
    
    private static final String SUGGESTIONS_URL = "/api/books/suggestions";

    @Autowired
	private AuthorRepository authorRepository;

	@Autowired
    private BookRepository bookRepository;

    @Autowired
	private AuthorMapper authorMapper;

	@Autowired
    private BookMapper bookMapper;

    @Autowired
    private BookService bookService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restBookMockMvc;

    private Book book;

	private Author AEvanVogt = new Author("Alfred Elton", "van Vogt");

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        BookResource bookResource = new BookResource(bookService);
        this.restBookMockMvc = MockMvcBuilders.standaloneSetup(bookResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Book createEntity(EntityManager em) {
        Book book = new Book()
                .title(DEFAULT_TITLE)
                .genre(DEFAULT_GENRE)
                .nbPages(DEFAULT_NB_PAGES)
                .publicationYear(DEFAULT_PUBLICATION_YEAR)
                .rating(DEFAULT_RATING);
        // Add required entity
        Author author = AuthorResourceIntTest.createEntity(em);
        em.persist(author);
        em.flush();
        book.setAuthor(author);
        return book;
    }

    @Before
    public void initTest() {
        book = createEntity(em);
		AEvanVogt = new Author("Alfred Elton", "van Vogt");
    }

    @Test
    @Transactional
    public void createBook() throws Exception {
        int databaseSizeBeforeCreate = bookRepository.findAll().size();

        // Create the Book
        BookDTO bookDTO = bookMapper.bookToBookDTO(book);

        restBookMockMvc.perform(post("/api/books")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bookDTO)))
            .andExpect(status().isCreated());

        // Validate the Book in the database
        List<Book> bookList = bookRepository.findAll();
        assertThat(bookList).hasSize(databaseSizeBeforeCreate + 1);
        Book testBook = bookList.get(bookList.size() - 1);
        assertThat(testBook.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testBook.getGenre()).isEqualTo(DEFAULT_GENRE);
        assertThat(testBook.getNbPages()).isEqualTo(DEFAULT_NB_PAGES);
        assertThat(testBook.getPublicationYear()).isEqualTo(DEFAULT_PUBLICATION_YEAR);
        assertThat(testBook.getRating()).isEqualTo(DEFAULT_RATING);
    }

    @Test
    @Transactional
    public void createBookWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = bookRepository.findAll().size();

        // Create the Book with an existing ID
        Book existingBook = new Book();
        existingBook.setId(1L);
        BookDTO existingBookDTO = bookMapper.bookToBookDTO(existingBook);

        // An entity with an existing ID cannot be created, so this API call must fail
        restBookMockMvc.perform(post("/api/books")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingBookDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Book> bookList = bookRepository.findAll();
        assertThat(bookList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = bookRepository.findAll().size();
        // set the field null
        book.setTitle(null);

        // Create the Book, which fails.
        BookDTO bookDTO = bookMapper.bookToBookDTO(book);

        restBookMockMvc.perform(post("/api/books")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bookDTO)))
            .andExpect(status().isBadRequest());

        List<Book> bookList = bookRepository.findAll();
        assertThat(bookList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkGenreIsRequired() throws Exception {
        int databaseSizeBeforeTest = bookRepository.findAll().size();
        // set the field null
        book.setGenre(null);

        // Create the Book, which fails.
        BookDTO bookDTO = bookMapper.bookToBookDTO(book);

        restBookMockMvc.perform(post("/api/books")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bookDTO)))
            .andExpect(status().isBadRequest());

        List<Book> bookList = bookRepository.findAll();
        assertThat(bookList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNbPagesIsRequired() throws Exception {
        int databaseSizeBeforeTest = bookRepository.findAll().size();
        // set the field null
        book.setNbPages(null);

        // Create the Book, which fails.
        BookDTO bookDTO = bookMapper.bookToBookDTO(book);

        restBookMockMvc.perform(post("/api/books")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bookDTO)))
            .andExpect(status().isBadRequest());

        List<Book> bookList = bookRepository.findAll();
        assertThat(bookList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPublicationYearIsRequired() throws Exception {
        int databaseSizeBeforeTest = bookRepository.findAll().size();
        // set the field null
        book.setPublicationYear(null);

        // Create the Book, which fails.
        BookDTO bookDTO = bookMapper.bookToBookDTO(book);

        restBookMockMvc.perform(post("/api/books")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bookDTO)))
            .andExpect(status().isBadRequest());

        List<Book> bookList = bookRepository.findAll();
        assertThat(bookList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkRatingIsRequired() throws Exception {
        int databaseSizeBeforeTest = bookRepository.findAll().size();
        // set the field null
        book.setRating(null);

        // Create the Book, which fails.
        BookDTO bookDTO = bookMapper.bookToBookDTO(book);

        restBookMockMvc.perform(post("/api/books")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bookDTO)))
            .andExpect(status().isBadRequest());

        List<Book> bookList = bookRepository.findAll();
        assertThat(bookList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllBooks() throws Exception {
        // Initialize the database
        bookRepository.saveAndFlush(book);

        // Get all the bookList
        restBookMockMvc.perform(get("/api/books?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(book.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
            .andExpect(jsonPath("$.[*].genre").value(hasItem(DEFAULT_GENRE.toString())))
            .andExpect(jsonPath("$.[*].nbPages").value(hasItem(DEFAULT_NB_PAGES)))
            .andExpect(jsonPath("$.[*].publicationYear").value(hasItem(DEFAULT_PUBLICATION_YEAR)))
            .andExpect(jsonPath("$.[*].rating").value(hasItem(DEFAULT_RATING)));
    }

    @Test
    @Transactional
    public void getBook() throws Exception {
        // Initialize the database
        bookRepository.saveAndFlush(book);

        // Get the book
        restBookMockMvc.perform(get("/api/books/{id}", book.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(book.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
            .andExpect(jsonPath("$.genre").value(DEFAULT_GENRE.toString()))
            .andExpect(jsonPath("$.nbPages").value(DEFAULT_NB_PAGES))
            .andExpect(jsonPath("$.publicationYear").value(DEFAULT_PUBLICATION_YEAR))
            .andExpect(jsonPath("$.rating").value(DEFAULT_RATING));
    }

    @Test
    @Transactional
    public void getNonExistingBook() throws Exception {
        // Get the book
        restBookMockMvc.perform(get("/api/books/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBook() throws Exception {
        // Initialize the database
        bookRepository.saveAndFlush(book);
        int databaseSizeBeforeUpdate = bookRepository.findAll().size();

        // Update the book
        Book updatedBook = bookRepository.findOne(book.getId());
        updatedBook
                .title(UPDATED_TITLE)
                .genre(UPDATED_GENRE)
                .nbPages(UPDATED_NB_PAGES)
                .publicationYear(UPDATED_PUBLICATION_YEAR)
                .rating(UPDATED_RATING);
        BookDTO bookDTO = bookMapper.bookToBookDTO(updatedBook);

        restBookMockMvc.perform(put("/api/books")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bookDTO)))
            .andExpect(status().isOk());

        // Validate the Book in the database
        List<Book> bookList = bookRepository.findAll();
        assertThat(bookList).hasSize(databaseSizeBeforeUpdate);
        Book testBook = bookList.get(bookList.size() - 1);
        assertThat(testBook.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testBook.getGenre()).isEqualTo(UPDATED_GENRE);
        assertThat(testBook.getNbPages()).isEqualTo(UPDATED_NB_PAGES);
        assertThat(testBook.getPublicationYear()).isEqualTo(UPDATED_PUBLICATION_YEAR);
        assertThat(testBook.getRating()).isEqualTo(UPDATED_RATING);
    }

    @Test
    @Transactional
    public void updateNonExistingBook() throws Exception {
        int databaseSizeBeforeUpdate = bookRepository.findAll().size();

        // Create the Book
        BookDTO bookDTO = bookMapper.bookToBookDTO(book);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restBookMockMvc.perform(put("/api/books")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bookDTO)))
            .andExpect(status().isCreated());

        // Validate the Book in the database
        List<Book> bookList = bookRepository.findAll();
        assertThat(bookList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteBook() throws Exception {
        // Initialize the database
        bookRepository.saveAndFlush(book);
        int databaseSizeBeforeDelete = bookRepository.findAll().size();

        // Get the book
        restBookMockMvc.perform(delete("/api/books/{id}", book.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Book> bookList = bookRepository.findAll();
        assertThat(bookList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
	@Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Book.class);
    }

	/**
	 * No reading preferences is equivalent to no book matching the preferences.
	 * 
	 * @throws Exception
	 */
	@Test
	@Transactional
	public void getSuggestionsWithNoSuggestions() throws Exception {
		getSuggestionsMatchingNone();
	}

	// TODO: criteria with same rank
	// @Test
	// @Transactional
	// public void getSuggestionsWithInvalidSuggestions() throws Exception {}

	@Test
	@Transactional
	public void getSuggestionsWithNoBooks() throws Exception {
		// Arrange
		bookRepository.deleteAll();
		int databaseSize = bookRepository.findAll().size();
		assertThat(databaseSize)
			.withFailMessage("The database needs no book for this test to run, found %s book(s).", databaseSize)
			.isZero();

		em.persist(AEvanVogt);
		authorRepository.saveAndFlush(AEvanVogt);

		ReadingPreferencesDTO readingPreferencesDTO = new ReadingPreferencesDTO();
		readingPreferencesDTO.author(authorMapper.authorToAuthorDTO(AEvanVogt));

		log.debug("PYB> HTTP query " + readingPreferencesDTO);

		// Act
		restBookMockMvc.perform(
				suggestionsHttpMethod(SUGGESTIONS_URL)
	            .contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(readingPreferencesDTO)))
				// Assert
				.andExpect(status().isOk())
				.andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
				.andExpect(content().string("[]"));
	}

	// TODO: getSuggestionsWithNonExistingAuthor

	@Test
	@Transactional
	public void getSuggestions1stCriterionByAuthorAndMatching() throws Exception {
		// Arrange
		em.persist(book);
		em.persist(AEvanVogt);

		Book bookByAEVV = new Book()
				.author(AEvanVogt)
				.title("The Book of Ptath")
				.genre(BookGenre.SCIFI)
				.nbPages(221)
				.publicationYear(1947)
				.rating(4);
		em.persist(bookByAEVV);

		int databaseSize = bookRepository.findAll().size();
		assertThat(databaseSize)
			.withFailMessage("The database needs at least 2 books for this test to run, found %s book(s).", databaseSize)
			.isGreaterThan(1);
		
		ReadingPreferencesDTO readingPreferencesDTO = new ReadingPreferencesDTO();
		readingPreferencesDTO.author(authorMapper.authorToAuthorDTO(AEvanVogt));

		log.debug("PYB> HTTP query " + readingPreferencesDTO);

		// Act
		restBookMockMvc.perform(
				suggestionsHttpMethod(SUGGESTIONS_URL)
	            .contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(readingPreferencesDTO)))
				.andDo(new ResultHandler() {

					@Override
					public void handle(MvcResult result) throws Exception {
						log.debug("PYB> " + result.getResponse().getContentAsString());
					}
				})
				// Assert
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.length()").value(databaseSize))
				// 1st book: matches Author
				.andExpect(bookAtJsonPosition(bookByAEVV, 0))
				// 2nd book: no match
				.andExpect(bookAtJsonPosition(book, 1));
	}

	@Test
	@Transactional
	public void getSuggestions1stCriterionByAuthorAndMatching2ndByPages()
			throws Exception {

		// Arrange
		em.persist(book);
		em.persist(AEvanVogt);
		
		Book bookByAEVV = new Book()
				.author(AEvanVogt)
				.title("The Book of Ptath")
				.genre(BookGenre.SCIFI)
				.nbPages(221)
				.publicationYear(1947)
				.rating(4);
		em.persist(bookByAEVV);
		
		Book secondBookByAEVV = new Book()
				.author(AEvanVogt)
				.title("The House That Stood Still")
				.genre(BookGenre.SCIFI)
				.nbPages(210)
				.publicationYear(1950)
				.rating(5);
		em.persist(secondBookByAEVV);

		int databaseSize = bookRepository.findAll().size();
		assertThat(databaseSize)
			.withFailMessage("The database needs at least 3 books for this test to run, found %s book(s).", databaseSize)
			.isGreaterThan(2);
		
		ReadingPreferencesDTO readingPreferencesDTO = new ReadingPreferencesDTO();
		readingPreferencesDTO.author(authorMapper.authorToAuthorDTO(AEvanVogt));
		// TODO: implement second reading preference, e.g. nb of pages

		log.debug("PYB> HTTP query " + readingPreferencesDTO);

		// Act
		restBookMockMvc.perform(
				suggestionsHttpMethod(SUGGESTIONS_URL)
	            .contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(readingPreferencesDTO)))
				.andDo(new ResultHandler() {

					@Override
					public void handle(MvcResult result) throws Exception {
						log.debug("PYB> " + result.getResponse().getContentAsString());
					}
				})
				// Assert
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.length()").value(databaseSize))
				// 1st book: matches Author
				.andExpect(bookAtJsonPosition(bookByAEVV, 0))
				// 2nd book: no match
				.andExpect(bookAtJsonPosition(book, 1));
	}

	/**
	 * No book satisfies the search criteria. Tests the default book ordering.
	 * 
	 * @throws Exception
	 */
	@Test
	@Transactional
	public void getSuggestionsMatchingNone() throws Exception {
		// Arrange
		em.persist(AEvanVogt);
		
		Book bookByAEVV = new Book()
				.author(AEvanVogt)
				.title("The Book of Ptath")
				.genre(BookGenre.SCIFI)
				.nbPages(221)
				.publicationYear(1947)
				.rating(4);
		
		Book secondBookByAEVV = new Book()
				.author(AEvanVogt)
				.title("The House That Stood Still")
				.genre(BookGenre.SCIFI)
				.nbPages(210)
				.publicationYear(1950)
				.rating(5);
		
		// Persist the 2nd book first to validate we don't get books in insertion order
		em.persist(bookByAEVV);
		em.persist(secondBookByAEVV);

		int databaseSize = bookRepository.findAll().size();
		assertThat(databaseSize)
			.withFailMessage("The database needs at least 2 books for this test to run, found %s book(s).", databaseSize)
			.isGreaterThan(1);
		
		// Act
		ReadingPreferencesDTO readingPreferencesDTO = new ReadingPreferencesDTO();
		// object voluntarily left empty

		log.debug("PYB> HTTP query " + readingPreferencesDTO);

		// Act
		restBookMockMvc.perform(
				suggestionsHttpMethod(SUGGESTIONS_URL)
	            .contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(readingPreferencesDTO)))
				.andDo(new ResultHandler() {

					@Override
					public void handle(MvcResult result) throws Exception {
						log.debug("PYB> " + result.getResponse().getContentAsString());
					}
				})
				// Assert
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.length()").value(databaseSize))
				// 1st book: "The Book of Ptath" is alphabetically before "The House That Stood Still"
				.andExpect(bookAtJsonPosition(bookByAEVV, 0))
				// 2nd book: "The House That Stood Still"
				.andExpect(bookAtJsonPosition(secondBookByAEVV, 1));
	}

	/** Abstracts the HTTP method used for the /books/suggestions endpoint. */
	private static MockHttpServletRequestBuilder suggestionsHttpMethod(String urlTemplate, Object... uriVars) {
		return post(urlTemplate, uriVars);
	}

	/**
	 * Test the presence of a book at a given position in a JSON array.
	 * 
	 * @param book
	 * @param jsonPosition
	 *            Position of the book in a JSON array, starts at 0.
	 * @return
	 */
	private ResultMatcher bookAtJsonPosition(Book b, int jsonPosition) {
		return new ResultMatcher() {
			@Override
			public void match(MvcResult result) {
				jsonPath("$.[" + jsonPosition + "].id").value(b.getId().intValue());
				jsonPath("$.[" + jsonPosition + "].title").value(b.getTitle());
				jsonPath("$.[" + jsonPosition + "].genre").value(b.getGenre().name());
				jsonPath("$.[" + jsonPosition + "].nbPages").value(b.getNbPages());
				jsonPath("$.[" + jsonPosition + "].publicationYear").value(b.getPublicationYear());
				jsonPath("$.[" + jsonPosition + "].rating").value(b.getRating());
			}
		};
	}
}
