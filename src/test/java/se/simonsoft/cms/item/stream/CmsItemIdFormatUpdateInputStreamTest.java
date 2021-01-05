package se.simonsoft.cms.item.stream;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

import se.simonsoft.cms.item.CmsRepository;
import se.simonsoft.cms.item.impl.CmsItemIdArg;

public class CmsItemIdFormatUpdateInputStreamTest {

	String xml1 = "<!DOCTYPE document PUBLIC \"-//Simonsoft//DTD TechDoc Base V1.0 Techdoc//EN\" \"techdoc.dtd\">\n" + 
			"<doc cms:repo=\"x-svn:///svn/testaut1^/\" xmlns:cms=\"http://www.simonsoft.se/namespace/cms\"\n" + 
			"cms:rid=\"2gyvymn15kv0000\" cms:rlogicalid=\"x-svn:///svn/testaut1^/tms/xml/Docs/My%20First%20Novel.xml?p=5\">\n" + 
			"<graphic new=\"x-svn:///svn/testaut1/the%20file.png\" old=\"x-svn:///svn/testaut1^/the%20file.png\"></graphic>\n" + 
			"<elem>Text containing the id <q>x-svn:///svn/testaut1^/the%20file.png</q>.</elem>\n" + 
			"</doc>";
	
	
	@Test
	public void test() throws IOException {
		assertEquals("input size for reference",  483, xml1.getBytes().length);
		InputStream in = new ByteArrayInputStream(xml1.getBytes());
		CmsRepository cmsRepo = new CmsRepository("https://hostname.tld/svn/testaut1");
		assertEquals("must have 3 slashes and no traling slash",  "x-svn:///svn/testaut1", cmsRepo.getItemId(null, null).getLogicalId());
		InputStream filter = new CmsItemIdFormatUpdateInputStream(in, cmsRepo);
		ByteArrayInOutStream out = new ByteArrayInOutStream();
		IOUtils.copy(filter, out);
		assertTrue("replaced graphic", out.toString().contains("old=\"x-svn:///svn/testaut1/the%20file.png\""));
		assertTrue("new graphic unchanged", out.toString().contains("new=\"x-svn:///svn/testaut1/the%20file.png\""));
		
		assertTrue("non-attribute unchanged", out.toString().contains("x-svn:///svn/testaut1^/the%20file.png"));

		// Test the repository id.
		assertTrue("replaced repo, not quite correct with the traling slash", out.toString().contains("cms:repo=\"x-svn:///svn/testaut1/\""));
		assertEquals("Verify that we can parse and normalize traling slash",  "x-svn:///svn/testaut1", new CmsItemIdArg("x-svn:///svn/testaut1/").getLogicalId());

		assertEquals("result size slightly smaller",  483-3, out.toString().getBytes().length);
	}
	
	@Test
	public void testRepoNotMatching() throws IOException {
		assertEquals("input size for reference",  483, xml1.getBytes().length);
		InputStream in = new ByteArrayInputStream(xml1.getBytes());
		CmsRepository cmsRepo = new CmsRepository("https://hostname.tld/svn/testaut10");
		assertEquals("must have 3 slashes and no traling slash",  "x-svn:///svn/testaut10", cmsRepo.getItemId(null, null).getLogicalId());
		InputStream filter = new CmsItemIdFormatUpdateInputStream(in, cmsRepo);
		ByteArrayInOutStream out = new ByteArrayInOutStream();
		IOUtils.copy(filter, out);

		assertEquals("result size unchanged",  483, out.toString().getBytes().length);
	}

}
