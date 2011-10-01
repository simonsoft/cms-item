package se.simonsoft.cms.version;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;

public class CmsComponentsTest {

	@Test
	public void testGetVersionNoMetaInf() {
		CmsComponentVersion v = CmsComponents.getVersion("cms-nonexistent");
		// minimize the risk of causing init errors in calling components
		assertNotNull("Should never return null version info", v);
		// If this test fails there might be a meta-inf folder in classpath, uncommon but not impossible
		assertEquals(CmsComponentVersionNopackage.class, v.getClass());
		assertFalse(CmsComponents.hasComponent("cms-nonexistent"));
	}

	@Test
	public void testLogAllVersions() {
		CmsComponents.logAllVersions();
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testLogAllVersionsNoManifest() {
		ClassLoader mockcl = mock(ClassLoader.class);
		CmsComponents.logAllVersions(mockcl);
	}

}
