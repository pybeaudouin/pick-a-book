package pyb.pickabook.service.dto;


import java.io.Serializable;
import java.util.Objects;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import pyb.pickabook.domain.enumeration.BookGenre;

/**
 * A DTO for the Book entity.
 */
public class BookDTO implements Serializable {

    private Long id;

    @NotNull
    private String title;

    @NotNull
    private BookGenre genre;

    @NotNull
    @Min(value = 1)
    private Integer nbPages;

    @NotNull
    @Min(value = -220)
    private Integer publicationYear;

    @NotNull
    @Min(value = 1)
    @Max(value = 5)
    private Integer rating;

	private AuthorDTO author;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public BookGenre getGenre() {
        return genre;
    }

    public void setGenre(BookGenre genre) {
        this.genre = genre;
    }
    public Integer getNbPages() {
        return nbPages;
    }

    public void setNbPages(Integer nbPages) {
        this.nbPages = nbPages;
    }
    public Integer getPublicationYear() {
        return publicationYear;
    }

    public void setPublicationYear(Integer publicationYear) {
        this.publicationYear = publicationYear;
    }
    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

	public AuthorDTO getAuthor() {
		return author;
    }

	public void setAuthor(AuthorDTO author) {
		this.author = author;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        BookDTO bookDTO = (BookDTO) o;

        if ( ! Objects.equals(id, bookDTO.id)) { return false; }

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "BookDTO{" +
            "id=" + id +
            ", title='" + title + "'" +
            ", genre='" + genre + "'" +
            ", nbPages='" + nbPages + "'" +
            ", publicationYear='" + publicationYear + "'" +
            ", rating='" + rating + "'" +
            '}';
    }
}
