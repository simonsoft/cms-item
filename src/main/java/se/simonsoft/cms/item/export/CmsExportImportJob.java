package se.simonsoft.cms.item.export;

/**
 * Interface defining aspects common to import and export operations.
 *
 */
public interface CmsExportImportJob {

	// The prefix and extension should likely not be in this interface since CmsExportJobBase allow them to be null;
	
	public String getJobName();
    
    public String getJobPath();
}
