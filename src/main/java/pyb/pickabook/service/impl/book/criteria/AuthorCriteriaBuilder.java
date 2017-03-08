package pyb.pickabook.service.impl.book.criteria;

import pyb.pickabook.service.dto.preference.AuthorPreferenceDTO;

public class AuthorCriteriaBuilder implements CriteriaBuilder<AuthorPreferenceDTO> {

	// FIXME: use Query and the Criteria API: CriteriaBuilder.Case.when(...)
	@Override
	public String build(AuthorPreferenceDTO criterion) {
		AuthorPreferenceDTO authorCriterion = criterion;
		return "CASE WHEN b.author.id = '" + authorCriterion.getAuthor().getId() + "' THEN 0 ELSE 1 END";
	}

}
