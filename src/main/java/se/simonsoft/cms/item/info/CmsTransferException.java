package se.simonsoft.cms.item.info;

/**
 * For interrupted or incomplete operation when everything looked alright at connect.
 */
public class CmsTransferException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public CmsTransferException(Exception e) {
		super(e);
	}
	
}
