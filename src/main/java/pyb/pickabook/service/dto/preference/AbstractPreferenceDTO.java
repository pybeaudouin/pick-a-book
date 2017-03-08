package pyb.pickabook.service.dto.preference;


/**
 * Base of criteria classes.
 */
public abstract class AbstractPreferenceDTO {

	/** Rank of the criterion compared to other criteria. */
	protected byte rank;

	public byte getRank() {
		return rank;
	}

	public void setRank(byte rank) {
		this.rank = rank;
	}
}
