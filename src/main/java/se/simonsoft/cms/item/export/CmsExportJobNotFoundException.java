/**
 * Copyright (C) 2009-2016 Simonsoft Nordic AB
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

public class CmsExportJobNotFoundException extends RuntimeException {

	/**
	 * Takes a CmsExportJob and sets it as a field so that the receiver of the exception may access it.  
	 */
	private static final long serialVersionUID = 1L;
	private final CmsExportImportJob exportJob;
	
	
	public CmsExportJobNotFoundException(CmsExportImportJob exportJob, String message, Throwable cause) {
		super(message, cause);
		this.exportJob = exportJob;
	}
	
	public CmsExportImportJob getExportJob() {
		return this.exportJob;
	}
	
	
}