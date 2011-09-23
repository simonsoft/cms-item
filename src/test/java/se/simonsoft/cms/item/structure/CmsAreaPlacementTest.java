package se.simonsoft.cms.item.structure;

import static org.junit.Assert.*;

import org.junit.Test;

public class CmsAreaPlacementTest {

	@Test
	public void testGetAreaName() {
		assertEquals("m", new CmsAreaPlacement("/*/m").getAreaName());
		assertEquals("Area51", new CmsAreaPlacement("/Area51").getAreaName());
	}

	@Test
	public void testGetAreaPathSegmentIndex() {
		assertEquals(2, new CmsAreaPlacement("/*/m").getAreaPathSegmentIndex());
		assertEquals(1, new CmsAreaPlacement("/Area51").getAreaPathSegmentIndex());
	}

	@Test
	public void testToString() {
		assertEquals("/*/m", "" + new CmsAreaPlacement("/*/m"));
		assertEquals("/Area51", "" + new CmsAreaPlacement("/Area51"));
	}

	@Test
	public void testEqualsObject() {
		assertTrue(new CmsAreaPlacement("/*/lang").equals(new CmsAreaPlacement("/*/lang")));
		assertFalse(new CmsAreaPlacement("/*/lan").equals(new CmsAreaPlacement("/*/lang")));
	}

}
