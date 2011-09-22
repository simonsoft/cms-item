package se.simonsoft.cms.version;

import java.util.jar.Attributes;
import java.util.jar.Manifest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Gets build identification from the MANIFEST.MF file,
 * if it includes the fiels configured in the top level project.
 * This assumes artifacts produced by the build server.
 */
public class CmsComponentVersionManifest implements CmsComponentVersion {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public static final String ATTRIBUTE_VERSION = "Implementation-Version";
	public static final String ATTRIBUTE_BUILD_NAME = "Simonsoft-Build-Name";
	public static final String ATTRIBUTE_BUILD_NUMBER = "Simonsoft-Build-Number";
	public static final String ATTRIBUTE_BUILD_REVISION = "Simonsoft-Build-Revision";
	public static final String ATTRIBUTE_BUILD_TAG = "Simonsoft-Build-Tag";
	
	public static final String VERSION_SNAPSHOT_SUFFIX = "-SNAPSHOT";
	
	private String version;
	private String buildName;
	private Integer buildNumber;
	private Integer sourceRevision;
	private String tag;
	
	public CmsComponentVersionManifest(Manifest manifest) {
		this(manifest.getMainAttributes());		
	}
	
	public CmsComponentVersionManifest(Attributes manifestAttributes) {
		this.version = getString(manifestAttributes, ATTRIBUTE_VERSION, UNKNOWN_VERSION);
		this.buildName = getString(manifestAttributes, ATTRIBUTE_BUILD_NAME, "");
		this.buildNumber = getInt(manifestAttributes, ATTRIBUTE_BUILD_NUMBER);
		this.sourceRevision = getInt(manifestAttributes, ATTRIBUTE_BUILD_REVISION);
		this.tag = getString(manifestAttributes, ATTRIBUTE_BUILD_TAG, "");
	}

	@Override
	public String getVersion() {
		return version;
	}

	@Override
	public boolean isSnapshot() {
		if (version == null || UNKNOWN_VERSION.equals(version)) {
			return true;
		}
		if (version.endsWith(VERSION_SNAPSHOT_SUFFIX)) {
			return true;
		}
		return false;
	}	
	
	@Override
	public boolean isKnown() {
		return version != UNKNOWN_VERSION
				&& buildName != null && buildName.length() > 0 
				&& buildNumber != null && sourceRevision != null
				&& tag != null && tag.length() > 0;
	}
	
	@Override
	public String getBuildName() {
		return buildName;
	}

	@Override
	public Integer getBuildNumber() {
		return buildNumber;
	}

	@Override
	public Integer getSourceRevision() {
		return sourceRevision;
	}

	@Override
	public String getBuildTag() {
		return tag;
	}
	
	@Override
	public String getLabel() {
		if (!isKnown()) {
			if (getVersion() == UNKNOWN_VERSION) {
				return "unknown";
			}
			return getVersion() + " unofficial build";
		}
		if (isSnapshot()) {
			return getVersion() + " " + getBuildName() + " revision " + getSourceRevision() + " build " + getBuildNumber();
		}
		return getVersion();
	}	

	@Override
	public String toString() {
		return getBuildName() + " " + getLabel();
	}
	
	private String getString(Attributes manifestAttributes, String key, String dft) {
		String v = manifestAttributes.getValue(key);
		if (v == null || v.length() == 0) {
			return dft;
		}
		return v;
	}
	
	private Integer getInt(Attributes manifestAttributes, String key) {
		String v = getString(manifestAttributes, key, null);
		if (v == null) {
			return null;
		}
		try {
			return Integer.parseInt(v);
		} catch (NumberFormatException e) {
			logger.error("Failed integer parse of manifest attribute value '{}'", v, e);
			return null;
		}
	}	

}
