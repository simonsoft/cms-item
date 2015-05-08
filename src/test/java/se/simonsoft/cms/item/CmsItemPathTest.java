/**
 * Copyright (C) 2009-2013 Simonsoft Nordic AB
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
package se.simonsoft.cms.item;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

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
		assertEquals(2, p.getPathSegmentsCount());
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
	public void testGetNameBaseAndExtensionNoExtension() {
		CmsItemPath p = new CmsItemPath("/noextension");
		assertEquals("", p.getExtension());
		assertEquals("noextension", p.getNameBase());
	}
	
	@Test
	public void testGetNameBaseAndExtensionSingleChar() {
		CmsItemPath p = new CmsItemPath("/a");
		assertEquals("", p.getExtension());
		assertEquals("a", p.getNameBase());
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
	public void testGetAncestors() {
		CmsItemPath p = new CmsItemPath("/a/b c/d");
		List<CmsItemPath> ps = p.getAncestors();
		assertEquals("Paths should be sorted from root up", new CmsItemPath("/a"), ps.get(0));
		assertEquals(new CmsItemPath("/a/b c"), ps.get(1));
		assertEquals(2, ps.size());
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
	public void testPathSegmentsList() {
		CmsItemPath path = new CmsItemPath("/folder name/subfolder/file with space.xml");
		
		List<String> pathList = path.getPathSegments();
		assertEquals(3, pathList.size());
		assertEquals(3, path.getSize());
		assertEquals("folder name", pathList.get(0));
		assertEquals("subfolder", pathList.get(1));
		assertEquals("file with space.xml", pathList.get(2));
	}
	
	@Test
	public void testAppendPathList() {
		
		List<String> listSegments = new CmsItemPath("/g/h/").getPathSegments();
		List<String> listManual = new ArrayList<String>();
		listManual.add("g");
		listManual.add("h");
		
		assertEquals(new CmsItemPath("/f/g/h"), 
				new CmsItemPath("/f").append(listSegments));		
		assertEquals(new CmsItemPath("/f/g/h"), 
				new CmsItemPath("/f/").append(listManual));
	}
	
	@Test
	public void testSubPath() {
		CmsItemPath path = new CmsItemPath("/folder name/subfolder/file with space.xml");
		
		assertEquals(new CmsItemPath("/test/folder name/subfolder"), new CmsItemPath("/test").append(path.subPath(0, 2)));
		assertEquals(new CmsItemPath("/test/subfolder"), new CmsItemPath("/test").append(path.subPath(1, 2)));
		assertEquals(new CmsItemPath("/test/subfolder/file with space.xml"), new CmsItemPath("/test").append(path.subPath(1, 3)));
		assertEquals(new CmsItemPath("/test/subfolder/file with space.xml"), new CmsItemPath("/test").append(path.subPath(1)));
	}
	
	@Test
	public void testSubPathNegative() {
		CmsItemPath path = new CmsItemPath("/folder name/subfolder/file with space.xml");
		
		// Testing negative fromIndex.
		assertEquals(new CmsItemPath("/test/file with space.xml"), new CmsItemPath("/test").append(path.subPath(-1)));
		assertEquals(new CmsItemPath("/test/file with space.xml"), new CmsItemPath("/test").append(path.subPath(-1, 3)));
		assertEquals(new CmsItemPath("/test/subfolder/file with space.xml"), new CmsItemPath("/test").append(path.subPath(-2, 3)));
		assertEquals(new CmsItemPath("/test/folder name/subfolder/"), new CmsItemPath("/test").append(path.subPath(-3, 2)));
		// Testing negative toIndex;
		assertEquals(new CmsItemPath("/test/folder name/"), new CmsItemPath("/test").append(path.subPath(0, -2)));
		// Testing both negative Index;
		assertEquals(new CmsItemPath("/test/folder name/subfolder/"), new CmsItemPath("/test").append(path.subPath(-3, -1)));
		// For completeness: Both 0 is valid and returns an empty list.
		assertEquals(new CmsItemPath("/test/"), new CmsItemPath("/test").append(path.subPath(0, 0)));

		
	}
	
	@Test
	public void testIsAncestorOf() {
		
		CmsItemPath path = new CmsItemPath("/folder name/subfolder/file with space.xml");
		CmsItemPath siblingPath = new CmsItemPath("/folder name/subfolder/sibling.xml");
		CmsItemPath parentPath = new CmsItemPath("/folder name/subfolder");
		CmsItemPath grandParentPath = new CmsItemPath("/folder name");
		CmsItemPath nonParentPath = new CmsItemPath("/folder name/subfolder whatever");
		CmsItemPath deeperPath = new CmsItemPath("/folder name/subfolder/deeper/deep.xml");
		
		assertTrue("Direct parent", parentPath.isAncestorOf(path));
		assertTrue("Grandparent is also considered ancestor", grandParentPath.isAncestorOf(path));
		assertTrue("Grandparent is ancestor of parent", grandParentPath.isAncestorOf(parentPath));
		assertFalse("Sibling", siblingPath.isAncestorOf(path));
		assertFalse("Not a parent", nonParentPath.isAncestorOf(path));
		assertFalse("Deep nonparent", deeperPath.isAncestorOf(path));
		assertFalse("Deep nonparent reversed", path.isAncestorOf(deeperPath));
		assertFalse("A path is not an ancestor of itself", path.isAncestorOf(path));
		
	}
	
	@Test
	public void testIsParentOf() {
		
		CmsItemPath path = new CmsItemPath("/folder name/subfolder/file with space.xml");
		CmsItemPath parentPath = new CmsItemPath("/folder name/subfolder");
		CmsItemPath grandParentPath = new CmsItemPath("/folder name");
		CmsItemPath nonParentPath = new CmsItemPath("/folder name/subfolder whatever");
		
		assertTrue("Direct parent", parentPath.isParentOf(path));
		assertFalse("Grandparent is not a parent", grandParentPath.isParentOf(path));
		assertTrue("Grandparent is parent of parent", grandParentPath.isParentOf(parentPath));
		assertFalse("Not a parent", nonParentPath.isParentOf(path));
		assertFalse("A path is not a parent of itself", path.isParentOf(path));
		
	}
	
	@Test
	public void testPathSegmentsListOptimization() {
		CmsItemPath path = new CmsItemPath("/folder name/subfolder");
		assertSame("Should reuse instance", path.getPathSegments(), path.getPathSegments());
		try {
			path.getPathSegments().add("c");
			fail("should be immutable");
		} catch (UnsupportedOperationException e) {};
		try {
			path.getPathSegments().addAll(Arrays.asList("d"));
			fail("should be immutable");
		} catch (UnsupportedOperationException e) {};
		try {
			path.getPathSegments().add(1, "e");
			fail("should be immutable");
		} catch (UnsupportedOperationException e) {};
		try {
			path.getPathSegments().remove(1);
			fail("should be immutable");
		} catch (UnsupportedOperationException e) {};
		try {
			path.getPathSegments().remove("subfolder");
			fail("should be immutable");
		} catch (UnsupportedOperationException e) {};
		try {
			path.getPathSegments().removeAll(Arrays.asList("subfolder"));
			fail("should be immutable");
		} catch (UnsupportedOperationException e) {};
		try {
			path.getPathSegments().clear();
			fail("should be immutable");
		} catch (UnsupportedOperationException e) {};
		try {
			path.getPathSegments().set(1, "g");
			fail("should be immutable");
		} catch (UnsupportedOperationException e) {};
		try {
			path.getPathSegments().retainAll(Arrays.asList("subfolder"));
			fail("should be immutable");
		} catch (UnsupportedOperationException e) {};
	}
	
	@Test
	public void testCompare() {
		LinkedList<CmsItemPath> p = new LinkedList<CmsItemPath>();
		p.add(new CmsItemPath("/aa/a"));
		p.add(new CmsItemPath("/a/b"));
		p.add(new CmsItemPath("/ab/e"));
		p.add(new CmsItemPath("/aB/e"));
		p.add(new CmsItemPath("/ab/f"));
		p.add(new CmsItemPath("/ab"));
		p.add(new CmsItemPath("/ac/d"));
		Collections.sort(p);
		assertEquals("/a/b", "" + p.get(0));
		assertEquals("Path sorting should be case insensitive", "/aa/a", "" + p.get(1));
		assertEquals("/ab", "" + p.get(2));
		// Folders should be sorted together we should be smarter than this
		//also upper case would preferrable come before lower case
		assertEquals("/ab/e", "" + p.get(3));
		assertEquals("/aB/e", "" + p.get(4));
		assertEquals("/ab/f", "" + p.get(5));
	}

	@Test
	public void testRoot() {
		// important: No method should return the ROOT instance. It is used for map keys only.
		
		assertNotNull(CmsItemPath.ROOT);
		assertNull("For backwards compatibility getParent of root sub-item must still return null", new CmsItemPath("/dir").getParent());
		
		// TBD Really null? Dangerous at string concatenation. Also causes confusion with the fact that CmsItemId has a CmsItemPath==null for root. Return empty string instead?
		assertEquals("Root path is undefined as string", null, CmsItemPath.ROOT.getPath());
		
		assertEquals(0, CmsItemPath.ROOT.getPathSegmentsCount());
		assertNotNull(CmsItemPath.ROOT.getPathSegments());
		assertEquals(0, CmsItemPath.ROOT.getPathSegments().size());
		try {
			CmsItemPath.ROOT.getParent();
		} catch (IllegalStateException e) {
			assertEquals("Attempt to get parent for root path", e.getMessage());
		}
		assertEquals(new CmsItemPath("/dir"), CmsItemPath.ROOT.append("dir"));
		assertEquals("", CmsItemPath.ROOT.toString());
		assertTrue(CmsItemPath.ROOT.equals(CmsItemPath.ROOT));
		assertEquals(CmsItemPath.ROOT, CmsItemPath.ROOT);
		assertFalse("equals method should give the same result in both directions", CmsItemPath.ROOT.equals(null));
	}
	
	@Test(expected = IllegalStateException.class)
	public void getNameOnROOTShouldThrowException() {
		CmsItemPath.ROOT.getName();
	}

	@Test(expected = IllegalStateException.class)
	public void getNameBaseOnROOTShouldThrowException() {
		CmsItemPath.ROOT.getNameBase();
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void getNameWithSegPositionZeroShouldThrowException() {
		CmsItemPath.ROOT.getName(0);
		
	}
	
	@Test(expected = IllegalStateException.class)
	public void getAncestorsOnRootShouldThrowException() {
		CmsItemPath.ROOT.getAncestors();
		
	}
	
	@Test
	public void toStringAtRootReturnsEmptyString() {
		 assertEquals("" ,CmsItemPath.ROOT.toString());
	}
	
	@Test(expected = UnsupportedOperationException.class)
	public void getPathSegmentsAddAllWithPathAsNull() {
		
		List<String> emptyList = new ArrayList<String>();
		CmsItemPath.ROOT.getPathSegments().addAll(1, emptyList);
	}
	
	@Test
	public void testRootFromString() {
		try {
			new CmsItemPath("");
			fail("APIs should return null instead of a CmsItemPath instance for root");
		} catch (IllegalArgumentException e) {
			// expected
		}
	}

}
