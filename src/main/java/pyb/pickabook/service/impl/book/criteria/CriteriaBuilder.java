package pyb.pickabook.service.impl.book.criteria;

import pyb.pickabook.service.dto.preference.AbstractPreferenceDTO;

/**
 * Build a sub query to sort books.
 *
 */
public interface CriteriaBuilder<C extends AbstractPreferenceDTO> {
	String build(C criterion);
}
