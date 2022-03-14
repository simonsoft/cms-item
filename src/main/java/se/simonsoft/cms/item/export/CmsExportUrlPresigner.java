package se.simonsoft.cms.item.export;

import java.net.URL;
import java.time.Duration;

public interface CmsExportUrlPresigner {

	// CmsExportImportJob.getJobPath() is relative to any other prefixes (CmsExportPrefix, CloudId)
	// The CmsExportUrlPresigner implementation must originate from the same CmsExportProvider (or identical config) as the Reader/Writer.
	
	public URL getUrl(CmsExportImportJob job);
	
	public URL getPresignedGet(CmsExportImportJob job, Duration duration);
	
	public URL getPresignedPut(CmsExportImportJob job, Duration duration);
	
}
