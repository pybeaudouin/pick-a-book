package pyb.pickabook.service.mapper;

import pyb.pickabook.domain.*;
import pyb.pickabook.service.dto.BookDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Book and its DTO BookDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface BookMapper {

    @Mapping(source = "author.id", target = "authorId")
    BookDTO bookToBookDTO(Book book);

    List<BookDTO> booksToBookDTOs(List<Book> books);

    @Mapping(source = "authorId", target = "author")
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
}
