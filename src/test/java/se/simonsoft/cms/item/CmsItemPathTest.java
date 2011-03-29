package se.simonsoft.cms.item;

import static org.junit.Assert.*;

import org.junit.Ignore;
import org.junit.Test;

public class CmsItemPathTest {

	@Test
	public void testConstructorValidChars() {
		new CmsItemPath("/azAZ09_-.,()%"); //add more
	}

	@Test
	public void testConstructorRootOrEmpty() {
		try {
			new CmsItemPath("/");
			fail("Single slash without path segment should not be allowed");
		} catch (IllegalArgumentException e) {
		}
		try {
			new CmsItemPath("");
			fail("Empty path or root should be undefined");
		} catch (IllegalArgumentException e) {
		}
	}
	
	@Test
	public void testConstructorEndWithWhitespace() {
		try {
			new CmsItemPath("/x ");
			fail("Should not allow paths ending with whitespace");
		} catch (IllegalArgumentException e) {
		}
		try {
			new CmsItemPath("/a /b");
			fail("Should not allow path segment ending with whitespace");
		} catch (IllegalArgumentException e) {
		}
		try {
			new CmsItemPath("/x\t");
			fail("Should not allow paths ending with tab");
		} catch (IllegalArgumentException e) {
		}
	}
	
	@Test
	public void testConstructorNoLeadingSlash() {
		try {
			new CmsItemPath("f.txt");
			fail("Should require leading slash");
		} catch (IllegalArgumentException e) {
		}
	}
	
	@Test
	public void testConstructorFilesystemRelativeStart() {
		try {
			new CmsItemPath("../f.txt");
			fail("support for relative file system path not implemented");
		} catch (IllegalArgumentException e) {
			// expected
		}
	}
	
	@Test
	@Ignore("not yet validating relative stuff")
	public void testConstructorFilesystemRelative() {
		try {
			new CmsItemPath("/a/../f.txt");
			fail("support for relative file system path not implemented");
		} catch (IllegalArgumentException e) {
			// expected
		}
	}
	
	@Test
	public void testConstructorAsterisk() {
		try {
			new CmsItemPath("/a*"); 
			fail("* should be invalid in path");
		} catch (IllegalArgumentException e) {}
		// add more
	}
	
	@Test
	public void testConstructorBackslash() {
		try {
			new CmsItemPath("/a\\b.ext"); 
			fail("backslash should be invalid in path");
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
		assertEquals("/a/b c", new CmsItemPath("/a/b c").getPath());
		assertEquals("should be normalized", "/a/b c", new CmsItemPath("/a/b c/").getPath());
	}

	
	@Test
	@Ignore("Decided to not expose UrlEncoding in this API")
	public void testGetPathUrlEncoded() {
		//assertEquals("whitespace should be encoded using %20", "/a/b%20c", new CmsItemPath("/a/b c").getPathUrlEncoded());
	}
	
	@Test
	@Ignore("Decided to not expose UrlEncoding in this API")
	public void testGetPathUrlEncodedLogicalIdRule() {	
		@SuppressWarnings("unused")
		CmsItemPath p = new CmsItemPath("/folder name/Fälla träd 100% (admon).xml");
		//assertEquals("/folder%20name/F%C3%A4lla%20tr%C3%A4d%20100%25%20(admon).xml", p.getPathUrlEncoded());
		assertEquals("/double%20%20spaces", new CmsItemPath("/double  spaces"));
	}

	@Test
	public void testGetPathTrimmed() {
		assertEquals("a/b c", new CmsItemPath("/a/b c/").getPathTrimmed());
	}

	@Test
	public void testEquals() {
		assertTrue(new CmsItemPath("/x").equals(new CmsItemPath("/x")));
		assertTrue(new CmsItemPath("/a/b c/").equals(new CmsItemPath("/a/b c/")));
		assertTrue(new CmsItemPath("/a/b c").equals(new CmsItemPath("/a/b c/")));
	}
	
	@Test
	public void testToString() {
		assertEquals("/a/b c", "" + new CmsItemPath("/a/b c/"));
	}
	
	@Test
	public void testGetName() {
		assertEquals("a b.xml", new CmsItemPath("/a b.xml").getName());
		assertEquals("e f", new CmsItemPath("/a b/c d/e f/").getName());
	}

	@Test
	public void testGetNameSegmentPosition() {
		CmsItemPath p = new CmsItemPath("/folder/file.xml");
		//assertEquals("folder", p.getName(0)); // old "index"
		//assertEquals("file.xml", p.getName(1)); // old "index"
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
		assertEquals(new CmsItemPath("/a"), new CmsItemPath("/a/b c/d").getParent().getParent());
		assertEquals(new CmsItemPath("/a"), new CmsItemPath("/a/b c/d/").getParent().getParent());
		assertEquals(new CmsItemPath("/a/b c"), new CmsItemPath("/a/b c/d/f.txt").getParent().getParent());
	}

	@Test
	public void testAppend() {
		assertEquals(new CmsItemPath("/a/b/c d.xml"), new CmsItemPath("/a/").append("b").append("c d.xml"));
	}

	@Test
	public void testAppendWithSlash() {
		try {
			new CmsItemPath("/a").append("b/");
			fail("Slashes should not be allowed in the append segment");
		} catch (IllegalArgumentException e) {
		}
	}
	
	@Test
	public void testAppendInvalid() {
		try {
			new CmsItemPath("/a").append("b*");
			fail("Append segment should be validated for allowed chars");
		} catch (IllegalArgumentException e) {
		}
	}
	
	@Test
	public void testAppendTrailingWhitespace() {
		try {
			new CmsItemPath("/a").append("b ");
			fail("Append segment should be allowed to end with whitespace");
		} catch (IllegalArgumentException e) {
		}
	}
	
	// from BrowseEntry
	@Test
	public void testPathComponents() {
		CmsItemPath path = new CmsItemPath("/folder name/subfolder/file with space.xml");
		assertEquals("file with space.xml", path.getName(-1));
		assertEquals("/folder name/subfolder", path.getParent().toString());
	}
	
	@Test
	@Ignore("root is still undefined")
	public void testPathComponentsRoot() {
		CmsItemPath path = new CmsItemPath("/");
		assertNull(path.getName(-1));
		assertNull(path.getParent());
	}
	
	@Test
	public void testPathComponentsEmpty() {
		CmsItemPath path;
		try {
			path = new CmsItemPath("");
		} catch (Exception e) {
			// original behavior, might need to be changed
		}
		path = new CmsItemPath("/f").getParent();
		if (path == null) {
			// expected if root/empty path is not defined
			return;
		}
		assertNull(path.getName(-1));
		assertNull(path.getParent());
		assertNull(path.getName()); // better to return null than empty string?
		// should we have an isEmpty getter? or a public static final CmsItemPath EMPTY?
	}
	
	@Test
	public void testPathComponentsRootFile() {
		CmsItemPath path = new CmsItemPath("/file with space.xml");
		assertEquals("file with space.xml", path.getName(-1));
		assertEquals("file with space.xml", path.getName(1));
		assertNull(path.getParent());
	}
	
	@Test
	public void testPathComponentsRootDir() {
		CmsItemPath path = new CmsItemPath("/folder name/");
		assertEquals("folder name", path.getName(-1));
		assertEquals("folder name", path.getName(1));
		assertNull(path.getParent());
	}
	
	@Test
	public void testAppendPath() {
		assertEquals(new CmsItemPath("/f/g/h"), 
				new CmsItemPath("/f").append(new CmsItemPath("/g/h")));		
		assertEquals(new CmsItemPath("/f/g/h"), 
				new CmsItemPath("/f/").append(new CmsItemPath("/g/h/")));
	}
	
}
