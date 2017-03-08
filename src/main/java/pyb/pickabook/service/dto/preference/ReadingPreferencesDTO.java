package pyb.pickabook.service.dto.preference;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import pyb.pickabook.service.dto.AuthorDTO;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Describes a {@link User} taste in literature.
 */
public class ReadingPreferencesDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private AuthorPreferenceDTO authorPreference;

	public AuthorPreferenceDTO getAuthorPreference() {
		return authorPreference;
	}

	public void setAuthorPreference(AuthorPreferenceDTO authorPreference) {
		this.authorPreference = authorPreference;
	}

	public ReadingPreferencesDTO author(AuthorDTO authorDTO) {
		setAuthorPreference(new AuthorPreferenceDTO(authorDTO));
		return this;
	}

	/**
	 * Get reading preferences in ranking order.
	 * 
	 * @return
	 */
	public List<AbstractPreferenceDTO> buildCriteriaListRanked() {
		List<AbstractPreferenceDTO> result = new ArrayList<>();
		if (authorPreference != null) {
			result.add(authorPreference);
		}
		return result;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + authorPreference.hashCode();
		return result;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		
		ReadingPreferencesDTO other = (ReadingPreferencesDTO) o;
		
		if (!Objects.equals(authorPreference, other.getAuthorPreference())) {
			return false;
		}

		return true;
	}

	@Override
	public String toString() {
		return "ReadingPreferences [authorPreference=" + authorPreference + "]";
	}

}
