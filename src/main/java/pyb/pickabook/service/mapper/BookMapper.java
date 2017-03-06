package pyb.pickabook.service.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import pyb.pickabook.domain.Author;
import pyb.pickabook.domain.Book;
import pyb.pickabook.service.dto.AuthorDTO;
import pyb.pickabook.service.dto.BookDTO;

/**
 * Mapper for the entity Book and its DTO BookDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface BookMapper {

	@Mapping(source = "author", target = "author")
    BookDTO bookToBookDTO(Book book);

    List<BookDTO> booksToBookDTOs(List<Book> books);

	@Mapping(source = "author", target = "author")
    Book bookDTOToBook(BookDTO bookDTO);

    List<Book> bookDTOsToBooks(List<BookDTO> bookDTOs);

    default Author authorFromId(Long id) {
        if (id == null) {
            return null;
        }
        Author author = new Author();
        author.setId(id);
        return author;
    }

	AuthorDTO authorToAuthorDTO(Author author);

	@Mapping(target = "books", ignore = true)
	Author authorDTOToAuthor(AuthorDTO authorDTO);
}
