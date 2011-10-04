package se.simonsoft.cms.version;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.jar.Manifest;

import org.junit.Test;

public class CmsComponentVersionManifestTest {

	// replaced \r\n -> \\r\\n" +\n\t\t"
	String mf_jar = 
			"Manifest-Version: 1.0\r\n" +
			"Archiver-Version: Plexus Archiver\r\n" +
			"Created-By: Apache Maven\r\n" +
			"Built-By: hudson\r\n" +
			"Build-Jdk: 1.6.0_20\r\n";
	String mf_jar_snapshot = // with addDefaultImplementationEntries
			"Implementation-Title: Simonsoft CMS Subversion adapter\r\n" +
			"Implementation-Version: 1.1-SNAPSHOT\r\n" +
			"Implementation-Vendor-Id: se.simonsoft.adapters\r\n";
	String mf_jar_release = // with addDefaultImplementationEntries
			"Implementation-Title: Simonsoft CMS Subversion adapter\r\n" +
			"Implementation-Version: 1.1\r\n" +
			"Implementation-Vendor-Id: se.simonsoft.adapters\r\n";	
	String mf_hudson =
			"Hudson-Build-Number: 666\r\n" +
			"Hudson-Project: don't use hudson's own tags\r\n" +
			"Hudson-Version: 1.373\r\n";
	String mf_custom = 
			"Simonsoft-Build-Name: svnadapter-1.1.x\r\n" +
			"Simonsoft-Build-Number: 272\r\n" +
			"Simonsoft-Build-Revision: 1625\r\n" +
			"Simonsoft-Build-Tag: hudson-svnadapter-272\r\n" +
			"Simonsoft-Component-Id: svnadapter\r\n";
	String mf_custom_unofficial = 
			"Simonsoft-Build-Name: \r\n" +
			"Simonsoft-Build-Number: \r\n" +
			"Simonsoft-Build-Revision: \r\n" +
			"Simonsoft-Build-Tag: \r\n" +
			"Simonsoft-Component-Id: svnadapter\r\n";
	
	@Test
	public void testCmsComponentVersionManifestFullSnapshot() throws IOException {
		String mf = mf_jar + mf_jar_snapshot + mf_hudson + mf_custom;
		ByteArrayInputStream in = new ByteArrayInputStream(mf.getBytes());
		Manifest m = new Manifest(in);
		//for (Entry<Object, Object> a : m.getMainAttributes().entrySet()) {
		//	System.out.println(a.getKey() + ": " + a.getValue());
		//}
		CmsComponentVersion v = new CmsComponentVersionManifest(m);
		assertTrue(v.isKnown());
		assertTrue(v.isSnapshot());
		assertEquals("1.1-SNAPSHOT", v.getVersion());
		assertEquals(new Integer(1625), v.getSourceRevision());
		assertEquals("svnadapter-1.1.x", v.getBuildName());
		assertEquals(new Integer(272), v.getBuildNumber());
		assertEquals("hudson-svnadapter-272", v.getBuildTag());
		assertEquals("1.1-SNAPSHOT svnadapter-1.1.x revision 1625 build 272", v.getLabel());
	}

	@Test
	public void testCmsComponentVersionManifestLocalSnapshot() throws IOException {
		String mf = mf_jar + mf_jar_snapshot + mf_custom_unofficial;
		ByteArrayInputStream in = new ByteArrayInputStream(mf.getBytes());
		Manifest m = new Manifest(in);
		CmsComponentVersion v = new CmsComponentVersionManifest(m);
		assertFalse("Local builds are not considered known versions", v.isKnown());
		assertTrue(v.isSnapshot());
		assertEquals("1.1-SNAPSHOT", v.getVersion());
		assertEquals(null, v.getSourceRevision());
		assertEquals("", v.getBuildName());
		assertEquals(null, v.getBuildNumber());
		assertEquals("", v.getBuildTag());
		assertEquals("1.1-SNAPSHOT unofficial build", v.getLabel());
	}

	@Test
	public void testCmsComponentVersionManifestNotSimonsoftJarSettings() throws IOException {
		String mf = "";
		ByteArrayInputStream in = new ByteArrayInputStream(mf.getBytes());
		Manifest m = new Manifest(in);
		CmsComponentVersion v = new CmsComponentVersionManifest(m);
		assertFalse("Local builds are not considered known versions", v.isKnown());
		assertTrue("Better report snapshot than release if we don't know anything", v.isSnapshot());
		assertEquals("dev", v.getVersion());
		assertEquals(null, v.getSourceRevision());
		assertEquals("", v.getBuildName());
		assertEquals(null, v.getBuildNumber());
		assertEquals("", v.getBuildTag());
		assertEquals("unknown", v.getLabel()); // Odd case when component version lookup worked but the meta file is empty
	}
	
	@Test
	public void testCmsComponentVersionManifestRelease() throws IOException {
		String mf = mf_jar + mf_jar_release + mf_hudson + mf_custom;
		ByteArrayInputStream in = new ByteArrayInputStream(mf.getBytes());
		Manifest m = new Manifest(in);
		CmsComponentVersion v = new CmsComponentVersionManifest(m);
		assertTrue(v.isKnown());
		assertFalse(v.isSnapshot());
		assertEquals("1.1", v.getVersion());
		assertEquals(new Integer(1625), v.getSourceRevision());
		assertEquals("svnadapter-1.1.x", v.getBuildName());
		assertEquals(new Integer(272), v.getBuildNumber());
		assertEquals("hudson-svnadapter-272", v.getBuildTag());
		assertEquals("1.1", v.getLabel());
	}
	
	@Test
	public void testCmsComponentVersionManifestReleaseLocal() throws IOException {
		String mf = mf_jar + mf_jar_release + mf_custom_unofficial;
		ByteArrayInputStream in = new ByteArrayInputStream(mf.getBytes());
		Manifest m = new Manifest(in);
		CmsComponentVersion v = new CmsComponentVersionManifest(m);
		assertEquals("1.1", v.getVersion());
		assertEquals(null, v.getSourceRevision());
		assertEquals("", v.getBuildName());
		assertEquals(null, v.getBuildNumber());
		assertEquals("", v.getBuildTag());
		assertEquals("1.1 unofficial build", v.getLabel());
		assertFalse(v.isKnown());
		assertFalse(v.isSnapshot());
	}

}
