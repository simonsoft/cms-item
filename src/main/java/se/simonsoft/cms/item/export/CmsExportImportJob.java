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

/**
 * Interface defining aspects common to import and export operations.
 *
 */
public interface CmsExportImportJob {

	// The prefix and extension should not be in this interface since CmsExportJobBase allow them to be null;
	
	/**
	 * The job name which may represent a path including slashes.
	 * The job name and job path be identical in the simplest form.
	 * @return the job name which can include slashes but not start with slash
	 */
	public String getJobName();
    
    /**
     * The job path includes 'jobPrefix' (if used) AND is relative to any other prefixes that an export Reader/Writer pair might define (e.g. version, CloudId).
     * @return the job path including extension (if it has one)
     */
    public String getJobPath();
}
