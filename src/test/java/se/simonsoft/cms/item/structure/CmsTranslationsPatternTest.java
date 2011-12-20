package se.simonsoft.cms.item.structure;

import static org.junit.Assert.*;

import org.junit.Test;

public class CmsTranslationsPatternTest {

	@Test
	public void testGraphicsPattern() {
		CmsTranslationsPattern p = new CmsTranslationsPattern("/*/Trans");
		assertEquals(new CmsTranslationsPattern("Trans"), p.getGraphicsPattern());
		try {
			new CmsTranslationsPattern("Trans").getGraphicsPattern();
		} catch (UnsupportedOperationException e) {
			assertEquals("The pattern 'Trans' is already relative and can't be converted to a graphics translation pattern", e.getMessage());
		}
	}

}
