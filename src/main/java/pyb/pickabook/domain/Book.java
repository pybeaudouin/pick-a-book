package pyb.pickabook.domain;


import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

import pyb.pickabook.domain.enumeration.BookGenre;

/**
 * A Book.
 */
@Entity
@Table(name = "book")
public class Book implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "title")
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(name = "genre")
    private BookGenre genre;

    @Min(value = 1)
    @Column(name = "nb_pages")
    private Integer nbPages;

    @Min(value = -220)
    @Column(name = "publication_year")
    private Integer publicationYear;

    @Min(value = 1)
    @Max(value = 5)
    @Column(name = "rating")
    private Integer rating;

    @OneToOne
    @JoinColumn(unique = true)
    private Author author;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public Book title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public BookGenre getGenre() {
        return genre;
    }

    public Book genre(BookGenre genre) {
        this.genre = genre;
        return this;
    }

    public void setGenre(BookGenre genre) {
        this.genre = genre;
    }

    public Integer getNbPages() {
        return nbPages;
    }

    public Book nbPages(Integer nbPages) {
        this.nbPages = nbPages;
        return this;
    }

    public void setNbPages(Integer nbPages) {
        this.nbPages = nbPages;
    }

    public Integer getPublicationYear() {
        return publicationYear;
    }

    public Book publicationYear(Integer publicationYear) {
        this.publicationYear = publicationYear;
        return this;
    }

    public void setPublicationYear(Integer publicationYear) {
        this.publicationYear = publicationYear;
    }

    public Integer getRating() {
        return rating;
    }

    public Book rating(Integer rating) {
        this.rating = rating;
        return this;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public Author getAuthor() {
        return author;
    }

    public Book author(Author author) {
        this.author = author;
        return this;
    }

    public void setAuthor(Author author) {
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
        Book book = (Book) o;
        if (book.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, book.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Book{" +
            "id=" + id +
            ", title='" + title + "'" +
            ", genre='" + genre + "'" +
            ", nbPages='" + nbPages + "'" +
            ", publicationYear='" + publicationYear + "'" +
            ", rating='" + rating + "'" +
            '}';
    }
}
