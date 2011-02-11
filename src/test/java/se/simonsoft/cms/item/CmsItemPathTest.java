package se.simonsoft.cms.item;

import static org.junit.Assert.*;

import org.junit.Test;

public class CmsItemPathTest {

	@Test
	public void testConstructorValidation() {
		
	}
	
	@Test
	public void testConstructorUrlEncoded() {
		//what is the constructor for this? ("/folder/a%20b%C3%A4.xml");
		//assertEquals("Name should be decoded", "/folder/a bä.xml", p.getName());
	}
	
	@Test
	public void testHashCode() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetPath() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetPathUrlEncoded() {
		CmsItemPath p = new CmsItemPath("/folder name/Fälla träd 100% (admon).xml");
		assertEquals("/folder%20name/F%C3%A4lla%20tr%C3%A4d%20100%25%20(admon).xml", p.getPathUrlEncoded());
	}

	@Test
	public void testGetPathTrimmed() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetPathTrimmedBooleanBoolean() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetName() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetNameSegmentPosition() {
		CmsItemPath p = new CmsItemPath("/folder/file.xml");
		//assertEquals("folder", p.getName(0)); // "index"
		//assertEquals("file.xml", p.getName(1)); // "index"
		assertEquals("folder", p.getName(1));
		assertEquals("file.xml", p.getName(2));
		assertEquals("file.xml", p.getName(Integer.MAX_VALUE)); // deprecated behavior
		// new behavior
		assertEquals("file.xml", p.getName(-1));
		assertEquals("folder", p.getName(-2));
	}

	@Test
	public void testGetNameBase() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetExtension() {
		fail("Not yet implemented");
	}
	
	@Test
	public void testGetNameBaseAndExtension() {
		CmsItemPath p = new CmsItemPath("/.somefile.verylongextension");
		assertEquals("verylongextension", p.getExtension());
		assertEquals(".somefile", p.getNameBase());
	}
	
	@Test
	public void testGetNameBaseAndExtensionDotFirstOnly() {
		CmsItemPath p = new CmsItemPath("/.hiddenfile");
		assertEquals("", p.getExtension());
		assertEquals(".hiddenfile", p.getNameBase());
	}

	@Test
	public void testGetParent() {
		fail("Not yet implemented");
	}

	@Test
	public void testAppend() {
		fail("Not yet implemented");
	}

	@Test
	public void testEqualsObject() {
		fail("Not yet implemented");
	}

	@Test
	public void testToString() {
		fail("Not yet implemented");
	}

	// from BrowseEntry
	@Test
	public void testPathComponents() {
		CmsItemPath path = new CmsItemPath("/folder name/subfolder/file with space.xml");
		assertEquals("file with space.xml", path.getName(-1));
		assertEquals("/folder name/subfolder", path.getParent().toString()); // TBD trailing slash?
	}
	
	@Test
	public void testPathComponentsRoot() {
		CmsItemPath path = new CmsItemPath("/");
		assertNull(path.getName(-1));
		assertNull(path.getParent());
	}
	
	@Test
	public void testPathComponentsEmpty() {
		CmsItemPath path = new CmsItemPath(""); // validation error?
		assertNull(path.getName(-1));
		assertNull(path.getParent());
	}
	
	@Test
	public void testPathComponentsRootFile() {
		CmsItemPath path = new CmsItemPath("/file with space.xml");
		assertEquals("file with space.xml", path.getName(-1));
		assertNull(path.getParent());
	}
	
	@Test
	public void testPathComponentsRootDir() {
		CmsItemPath path = new CmsItemPath("/folder name/");
		assertEquals("folder name", path.getName(-1));
		assertNull(path.getParent());
	}
	
}
