package pyb.pickabook.service.dto.preference;

import java.io.Serializable;
import java.util.Objects;

import pyb.pickabook.service.dto.AuthorDTO;

/**
 * Preference to read a specific author.
 * <p>
 * Could be enhanced with an MatchingType object so we could also exclude
 * specific authors.
 * </p>
 */
public class AuthorPreferenceDTO extends AbstractPreferenceDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	private AuthorDTO author;

	public AuthorPreferenceDTO() {
	}

	public AuthorPreferenceDTO(AuthorDTO author) {
		super();
		this.author = author;
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

        AuthorPreferenceDTO authorCriterionDTO = (AuthorPreferenceDTO) o;

		if (!Objects.equals(rank, authorCriterionDTO.getRank())) {
			return false;
		}
		if (!Objects.equals(author, authorCriterionDTO.getAuthor())) {
			return false;
		}

        return true;
    }

    @Override
    public int hashCode() {
		return Objects.hash(rank, author);
    }

	@Override
	public String toString() {
		return "AuthorPreferenceDTO{rank = " + rank + ", author = " + author + "}";
	}
}
