package pyb.pickabook.service.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import pyb.pickabook.domain.Author;
import pyb.pickabook.service.dto.AuthorDTO;

/**
 * Mapper for the entity Author and its DTO AuthorDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface AuthorMapper {

    AuthorDTO authorToAuthorDTO(Author author);

    List<AuthorDTO> authorsToAuthorDTOs(List<Author> authors);

    Author authorDTOToAuthor(AuthorDTO authorDTO);

    List<Author> authorDTOsToAuthors(List<AuthorDTO> authorDTOs);
}
