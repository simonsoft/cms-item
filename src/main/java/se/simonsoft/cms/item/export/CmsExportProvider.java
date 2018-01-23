package se.simonsoft.cms.item.export;

/**
 * Provider for Export Writer and Export/Import Reader instances.
 * 
 * Most services will require both Reader and Writer for various operations.
 *
 */
public interface CmsExportProvider {

	CmsExportReader getReader();
	
	CmsExportWriter getWriter();
	
}
