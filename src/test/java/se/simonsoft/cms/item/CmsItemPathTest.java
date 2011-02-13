package se.simonsoft.cms.item;

import static org.junit.Assert.*;

import org.junit.Test;

public class CmsItemPathTest {

	@Test
	public void testConstructorValidChars() {
		new CmsItemPath("/azAZ09_-.,()"); //add more
	}
	
	@Test
	public void testConstructorNoLeadingSlash() {
		try {
			new CmsItemPath("f.txt");
			fail("TODO define behavior");
		} catch (IllegalArgumentException e) {
			fail("TODO define behavior");
		}
	}
	
	@Test
	public void testConstructorFilesystemRelative() {
		try {
			new CmsItemPath("../f.txt");
			fail("support for relative file system path not implemented");
		} catch (IllegalArgumentException e) {
			// expected
		}
	}
	
	@Test
	public void testConstructorAsterisk() {
		// TODO declare exception in constructor
		try {
			new CmsItemPath("/a*"); fail("* should be invalid in path");
		} catch (IllegalArgumentException e) {}
		// add more
	}
	
	@Test
	public void testConstructorUrlEncoded() {
		//what is the constructor for this? ("/folder/a%20b%C3%A4.xml");
		//assertEquals("Name should be decoded", "/folder/a bä.xml", p.getName());
	}

	@Test
	public void testGetPath() {
		assertEquals("/a/f.txt", new CmsItemPath("/a/f.txt").getPath());
		// should we normalize?
		assertEquals("/a/b c", new CmsItemPath("/a/b c").getPath());
		assertEquals("/a/b c/", new CmsItemPath("/a/b c/").getPath());
	}

	@Test
	public void testGetPathUrlEncoded() {
		CmsItemPath p = new CmsItemPath("/folder name/Fälla träd 100% (admon).xml");
		assertEquals("/folder%20name/F%C3%A4lla%20tr%C3%A4d%20100%25%20(admon).xml", p.getPathUrlEncoded());
	}

	@Test
	public void testGetPathTrimmed() {
		assertEquals("a/b c", new CmsItemPath("/a/b c/").getPathTrimmed());
	}

	@Test
	public void testGetPathTrimmedBooleanBoolean() {
		// if we normalize we don't need this method
	}

	@Test
	public void testEquals() {
		assertTrue(new CmsItemPath("/x").equals(new CmsItemPath("/x")));
		assertTrue(new CmsItemPath("/a/b c/").equals(new CmsItemPath("/a/b c/")));
		// TODO compare normalized?
		assertTrue(new CmsItemPath("/a/b c").equals(new CmsItemPath("/a/b c/")));
	}
	
	@Test
	public void testToString() {
		assertEquals("/a/b c/", "" + new CmsItemPath("/a/b c/"));
	}
	
	@Test
	public void testGetName() {
		assertEquals("a b.xml", new CmsItemPath("/a b.xml"));
		assertEquals("e f", new CmsItemPath("/a b/c d/e f/"));
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
		assertEquals("y", new CmsItemPath("/x/y.ditamap").getNameBase());
	}

	@Test
	public void testGetExtension() {
		assertEquals("ditamap", new CmsItemPath("/x/y/z.ditamap").getExtension());
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
		assertEquals(new CmsItemPath("/a/"), new CmsItemPath("/a/b c/d/").getParent().getParent());
	}

	@Test
	public void testAppend() {
		assertEquals(new CmsItemPath("/a/b/c d.xml"), new CmsItemPath("/a/").append("b").append("c d.xml"));
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
