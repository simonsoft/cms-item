package se.simonsoft.cms.item;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.Test;

public class RepoRevisionTest {

	@Test
	public void testDateIso() {
		RepoRevision r = new RepoRevision(3333, new Date(0));
		assertEquals("1970-01-01T00:00:00", r.getDateIso());
	}

}
