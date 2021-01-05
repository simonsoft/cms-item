package se.simonsoft.cms.item.stream;

import java.io.InputStream;

import se.simonsoft.cms.item.CmsRepository;

/**
 * InputStream that updates CmsItemId format by removing the ^ character.
 * <ul>
 * <li>Matches only abbreviated CmsItemId because qualified CmsItemIds (with hostname) should never be stored in data.</li>
 * <li>Matches only attribute content by requiring a leading " (quote) character.</li>
 * <li>Repository CmsItemId will have an incorrect remaining trailing slash.</li>
 * </ul>
 */
public class CmsItemIdFormatUpdateInputStream extends ReplacingInputStream {

	public CmsItemIdFormatUpdateInputStream(InputStream in, CmsRepository cmsRepo) {
		
		super(in, "\"" + cmsRepo.getItemId(null, null).getLogicalId() + "^", "\"" + cmsRepo.getItemId(null, null).getLogicalId());
	}

}
