package se.simonsoft.cms.version;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Version information factory.
 * Identifies simonsoft components and returns version info given component ID.
 * 
 * Note that the default Manifest file lookup won't find a webapp's own manifest. For that see
 * http://stackoverflow.com/questions/615493/how-do-i-read-the-manifest-file-for-a-webapp-running-in-apache-tomcat
 * and use constructor {@link CmsComponentVersionManifest#CmsComponentVersionManifest(Manifest)}.
 */
public abstract class CmsComponents {

	public static final String COMPONENT_ID_ATTRIBUTE = "Simonsoft-Component-Id";
	
	private static final Logger logger = LoggerFactory.getLogger(CmsComponents.class);
	
	private static Map<String,CmsComponentVersion> all = null;
	
	/**
	 * Gets the build information for a named simonsoft component.
	 * 
	 * When running code that is not packaged, there will be no version
	 * info att all and this method will return null.
	 * 
	 * When running packaged version but not from a build server
	 * there will be a version object reporting 'dev'.
	 * 
	 * See class comment regarding webapp support.
	 * 
	 * @param simonsoftComponentId The component name, a.k.a. maven artifactId
	 * @return info if found, null if not
	 */
	public static CmsComponentVersion getVersion(String simonsoftComponentId) {
		Map<String,CmsComponentVersion> v = getAll();
		if (v != null && v.containsKey(simonsoftComponentId)) {
			return v.get(simonsoftComponentId);
		}
		return null;
	}
	
	public static void logAllVersions() {
		logAllVersions(getDefaultClassLoader());
	}
	
	/**
	 *@deprecated Dev method to be able to test different classloaders if manifest reading fails.
	 */
	public static void logAllVersions(ClassLoader manifestLoader) {
		Map<String,CmsComponentVersion> v = getAll();
		if (v == null) {
			logger.info("No component versions found");
			return;
		}
		for (String id : v.keySet()) {
			logger.info("Version {}: {}", id, v.get(id));
		}
	}
	
	private static ClassLoader getDefaultClassLoader() {
		return CmsComponents.class.getClassLoader();
	}
	
	private static Map<String,CmsComponentVersion> getAll() {
		return getAll(getDefaultClassLoader());
	}
	
	private static Map<String,CmsComponentVersion> getAll(ClassLoader manifestLoader) {
		//no caching until we've sorted out the classloader issue//if (all == null) {
			all = readAll(CmsComponents.class.getClassLoader());
			if (all == null) {
				logger.warn("Version info was requested but no package info is available");
				return null;
			}
		//}
		return all;
	}
	
	private static Map<String,CmsComponentVersion> readAll(ClassLoader manifestLoader) {
		Map<String,CmsComponentVersion> found = new HashMap<String,CmsComponentVersion>();
		Enumeration<URL> resources;
		try {
			resources = manifestLoader.getResources("META-INF/MANIFEST.MF");
		} catch (IOException e) {
			throw new RuntimeException("Read error when attempting to load package information", e);
		}

		int total = 0;
		while (resources.hasMoreElements()) {
			total++;
			Manifest m = readManifest(resources.nextElement());
			addIfRecognizedComponent(m, found);
		}
		
		logger.info("Simonsoft build info found in {} of {} manifests", found.size(), total);
		
		if (found.size() == 0) {
			return null;
		}
		return found;
	}

	private static Manifest readManifest(URL source) {
		Manifest m;
		try {
			InputStream in = source.openStream();
			m = new Manifest(in);
			in.close();
		} catch (IOException e) {
			throw new RuntimeException("Error reading manifest file " + source, e);
		}
		return m;
	}
	
	private static void addIfRecognizedComponent(Manifest m, Map<String,CmsComponentVersion> found) {
		Attributes a = m.getMainAttributes();
		String id = a.getValue(COMPONENT_ID_ATTRIBUTE);
		if (id != null) {
			found.put(id, new CmsComponentVersionManifest(a));
		}
	}
	
}
