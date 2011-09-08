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
	
	public static final String ATTRIBUTE_BUILD_NAME = "Simonsoft-Build-Name";
	public static final String ATTRIBUTE_BUILD_NUMBER = "Simonsoft-Build-Number";
	public static final String ATTRIBUTE_BUILD_REVISION = "Simonsoft-Build-Revision";
	public static final String ATTRIBUTE_BUILD_TAG = "Simonsoft-Build-Tag";
	
	private String buildName;
	private Integer buildNumber;
	private Integer sourceRevision;
	private String tag;
	
	public CmsComponentVersionManifest(Manifest manifest) {
		this(manifest.getMainAttributes());		
	}
	
	public CmsComponentVersionManifest(Attributes manifestAttributes) {
		this.buildName = getString(manifestAttributes, ATTRIBUTE_BUILD_NAME, null);
		this.buildNumber = getInt(manifestAttributes, ATTRIBUTE_BUILD_NUMBER);
		this.sourceRevision = getInt(manifestAttributes, ATTRIBUTE_BUILD_REVISION);
		this.tag = getString(manifestAttributes, ATTRIBUTE_BUILD_TAG, DEFAULT_TAG);
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

	@Override
	public boolean isKnown() {
		return buildName != null && buildNumber != null && sourceRevision != null
				&& tag != DEFAULT_TAG; // instance, not equals
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
	public String getTag() {
		return tag;
	}

	@Override
	public String toString() {
		return getTag() + "@" + getSourceRevision();
	}

}
