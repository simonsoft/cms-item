package se.simonsoft.cms.item.structure;

import static org.junit.Assert.*;

import org.junit.Test;

import se.simonsoft.cms.item.impl.CmsItemIdArg;

public class CmsItemClassificationFiletypesHardcodedTest {

	@Test
	public void testIsGraphic() {
		CmsItemClassification c = new CmsItemClassificationAdapterFiletypes();
		assertFalse(c.isGraphic(new CmsItemIdArg("x-svn://y.y/svn/r^/f/file.xml")));
		assertTrue(c.isGraphic(new CmsItemIdArg("x-svn://y.y/svn/r^/f/file.png")));
	}

	@Test
	public void testIsXml() {
		CmsItemClassification c = new CmsItemClassificationAdapterFiletypes();
		assertTrue(c.isXml(new CmsItemIdArg("x-svn://y.y/svn/r^/f/file.xml")));
		assertFalse(c.isXml(new CmsItemIdArg("x-svn://y.y/svn/r^/f/file.png")));
	}

	@Test(expected=UnsupportedOperationException.class)
	public void testIsPublishable() {
		new CmsItemClassificationAdapterFiletypes().isPublishable(null);
	}

}
