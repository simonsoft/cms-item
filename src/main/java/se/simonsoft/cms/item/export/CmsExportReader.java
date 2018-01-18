package se.simonsoft.cms.item.export;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

public interface CmsExportReader {

	
	/**
	 * Prepares and validates feasibility of import.
	 * 
	 * @param job providing the location / key for the import
	 */
	void prepare(CmsImportJob job);

	// Currently no need for isReady, the import files must be available already.
    //boolean isReady();
    
    /**
     * @return map of key-value metadata, null when reader does not support metadata.
     */
    public Map<CmsExportMetaKey, String> getMeta();

    
    // Getting contents from cms-backend: OutputStream as parameter. (applicable to Writer)
 	// Committing with cms-backend: InputStream is suitable.
 	// Transforming with cms-xmlsource: Reader required, can use new InputStreamReader(InputStream)
 	// Transforming with cms-xmlsource using contents as parameter: Typically need a String. 
 	
 	// Converting to String:
 	// http://stackoverflow.com/questions/309424/read-convert-an-inputstream-to-a-string
 	// Conclusion: Should use ByteArrayOutputStream and perform the read loop.
    
    public InputStream getContents();
    
    public void getContents(OutputStream receiver) throws IOException;
    
}
