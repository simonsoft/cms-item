package se.simonsoft.cms.item.export;

public abstract class CmsExportJobBase {
	
	protected final CmsExportPrefix jobPrefix;
    protected final String jobName;
    protected final String jobExtension;
    

	public CmsExportJobBase(CmsExportPrefix jobPrefix, String jobName, String jobExtension) {
		
		if (jobName == null || jobName.isEmpty()) {
			throw new IllegalArgumentException("Not a valid export job name: " + jobName);
		}
		
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
