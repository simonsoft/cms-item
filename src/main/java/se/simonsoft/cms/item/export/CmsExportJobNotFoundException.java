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
	 * Takes a CmsExportJob and presents the name of the job in the message.
	 */
	private static final long serialVersionUID = 1L;
	private final CmsExportJob exportJob;
	
	
	public CmsExportJobNotFoundException(CmsExportJob exportJob, String message, Throwable cause) {
		super(message, cause);
		this.exportJob = exportJob;
	}
	
	public CmsExportJob getExportJob() {
		return this.exportJob;
	}
	
	
}
