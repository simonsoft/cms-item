package se.simonsoft.cms.item;

import static org.junit.Assert.*;

import org.junit.Test;

public class CmsItemKindTest {

	@Test
	public void testGetKind() {
		assertEquals("toString should have uppercase first because there's always toLowerCase",
				"File", CmsItemKind.File.toString());
		assertEquals("file", CmsItemKind.File.getKind());
		assertEquals("dir", CmsItemKind.Folder.getKind());
		assertEquals("dir", CmsItemKind.Repository.getKind());
		assertEquals("Folder", CmsItemKind.Folder.toString());
	}

	@Test
	public void testIsFolder() {
		assertTrue(CmsItemKind.File.isFile());
		assertFalse(CmsItemKind.Folder.isFile());
		assertFalse(CmsItemKind.File.isFolder());
		assertTrue(CmsItemKind.Folder.isFolder());
		assertTrue(CmsItemKind.Repository.isFolder());
	}

}
