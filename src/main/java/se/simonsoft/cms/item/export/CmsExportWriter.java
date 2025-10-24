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

import java.nio.file.Path;

public interface CmsExportWriter {

	// TODO: CMS 6.0 Consider specifying that the Writer should call job.prepare() unless already job.isReady().
    void prepare(CmsExportJob job);

    boolean isReady();

    void write();

    public interface LocalFileSystem extends CmsExportWriter {
    
    	Path getExportPath();
    }
    
    // TODO: Deprecate. The server-relative requirement was related to DAV.
    public interface ResultUrl extends CmsExportWriter {
        
    	/**
    	 * @return an href to the exported file, must be String to support a server-relative reference.
    	 */
    	String getExportUrl();
    }
    
    public interface ResultSingle extends CmsExportWriter {
        
    	/**
    	 * @return the job name and path to the exported file, relative to writer prefixes (version, CloudId etc).
    	 */
    	CmsImportJob getImportJob();
    }
}
