/**
 * Copyright (C) 2009-2017 Simonsoft Nordic AB
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
 	// Now have the slightly optimized ByteArrayInOutStream in cms-item (avoids a second copy on heap).
    
    public InputStream getContents();
    
    public void getContents(OutputStream receiver) throws IOException;
    
}
