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

/**
 * Abstract Job suitable as basis for both import and export jobs.
 *
 */
public abstract class CmsExportJobBase {
	
	protected final CmsExportPrefix jobPrefix;
    protected final String jobName;
    protected final String jobExtension;
    

	public CmsExportJobBase(CmsExportPrefix jobPrefix, String jobName, String jobExtension) {
		
		if (jobName == null || jobName.isEmpty()) {
			throw new IllegalArgumentException("Not a valid export job name: " + jobName);
		}
		
	    // TODO: Regex validating the export name. No slashes allowed etc.
		
		if (jobExtension.isEmpty()) {
			throw new IllegalArgumentException("Export job extension must not be empty, null is allowed.");
		}
		
		if (jobExtension.startsWith(".")) {
			throw new IllegalArgumentException("Export job extension must not start with '.'");
		}
		
		this.jobPrefix = jobPrefix; // null allowed
		this.jobName = jobName;
		this.jobExtension = jobExtension; // null allowed
	}

	public CmsExportPrefix getJobPrefix() {
        return jobPrefix;
    }
	
    public String getJobName() {
        return jobName;
    }

    public String getJobExtension() {
        return jobExtension;
    }
    
    public String getJobPath() {
    	StringBuilder sb = new StringBuilder();
    	if (getJobPrefix() != null) {
            sb.append(getJobPrefix());
            sb.append("/");
        }
    	sb.append(getJobName());
        if (getJobExtension() != null) {
        	sb.append('.');
        	sb.append(getJobExtension());
        }

        return sb.toString();
    	
    }
    
}
