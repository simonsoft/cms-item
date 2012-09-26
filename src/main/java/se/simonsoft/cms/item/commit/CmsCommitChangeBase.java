package se.simonsoft.cms.item.commit;

public abstract class CmsCommitChangeBase implements CmsCommitChange {

	protected enum StatContent { _, M, A, R, D }
	protected enum StatProps { _, M }

	protected abstract StatContent getStatContents();
	
	protected abstract StatProps getStatProps();
	
	protected abstract boolean isCopy();

	@Override
	public String toString() {
		return "" + getStatContents() + getStatProps() + (isCopy() ? "+" : "_") + ":" + getPath();
	}
	
}
