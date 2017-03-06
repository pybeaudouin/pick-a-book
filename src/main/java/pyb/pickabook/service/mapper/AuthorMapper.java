package pyb.pickabook.service.mapper;

import pyb.pickabook.domain.*;
import pyb.pickabook.service.dto.AuthorDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Author and its DTO AuthorDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface AuthorMapper {

    AuthorDTO authorToAuthorDTO(Author author);

    List<AuthorDTO> authorsToAuthorDTOs(List<Author> authors);

    @Mapping(target = "books", ignore = true)
    Author authorDTOToAuthor(AuthorDTO authorDTO);

    List<Author> authorDTOsToAuthors(List<AuthorDTO> authorDTOs);
}
