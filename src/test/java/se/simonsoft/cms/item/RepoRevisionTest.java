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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;

public class RepoRevisionTest {

	@Test
	public void testDateIso() {
		RepoRevision r = new RepoRevision(3333, new Date(0));
		assertEquals("1970-01-01T00:00:00", r.getDateIso());
		assertEquals("3333", r.toString());
	}

	@Test
	public void testRevisionFromDatetime() throws ParseException {
		DateFormat parse = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
		// In practice no svn repository used by humans will get this many revisions
		RepoRevision r = new RepoRevision(parse.parse("1990-01-01T00:00:00.000+0000"));
		assertEquals("1990-01-01T00:00:00Z", r.toString());
		assertEquals("Must always provide a number, instead of changing API to return Long with null possible",
				631152000000L, r.getNumber());
		try {
			new RepoRevision(parse.parse("1980-01-01T00:00:00.000+0000"));
			fail("Should not allow date-only revisions with early dates because svn revision numbers could reach those highs");
		} catch (IllegalArgumentException e) {
			// expected
		}
	}
	
	@Test
	public void testEquals() {
		assertTrue(new RepoRevision(1L, new Date(1)).equals(new RepoRevision(1L, new Date(1))));
		assertFalse(new RepoRevision(2L, new Date(1)).equals(new RepoRevision(1L, new Date(1))));
		assertFalse(new RepoRevision(1L, new Date(2)).equals(new RepoRevision(1L, new Date(1))));
		assertFalse(new RepoRevision(1L, new Date(1)).equals(new RepoRevision(2L, new Date(1))));
		assertFalse(new RepoRevision(1L, new Date(1)).equals(new RepoRevision(1L, new Date(2))));
	}
	
	@Test
	public void testEqualsNullDate() {
		assertFalse(new RepoRevision(1L, new Date(1)).equals(new RepoRevision(1L, null)));
		assertFalse(new RepoRevision(1L, null).equals(new RepoRevision(1L, new Date(1))));
		assertTrue(new RepoRevision(1L, null).equals(new RepoRevision(1L, null)));
	}
	
	@Test
	public void testIsNewer() {
		assertTrue(new RepoRevision(2L, new Date(2)).isNewer(new RepoRevision(1L, new Date(1))));
		assertFalse(new RepoRevision(2L, new Date(2)).isNewer(new RepoRevision(2L, new Date(2))));
		assertFalse(new RepoRevision(2L, new Date(2)).isNewer(new RepoRevision(3L, new Date(3))));
		
		assertTrue(new RepoRevision(2L, null).isNewer(new RepoRevision(1L, null)));
		assertFalse(new RepoRevision(2L, null).isNewer(new RepoRevision(2L, null)));
		
		Date now = new Date();
		Date old = new Date(now.getTime() - 9);
		assertTrue(new RepoRevision(now).isNewer(new RepoRevision(old)));
		assertFalse(new RepoRevision(now).isNewer(new RepoRevision(now)));
		
		assertTrue("Revision number should have precedence because revision timestamp is, in svn, only a revprop and might be out of sync in for example merged repositories",
				new RepoRevision(2L, new Date(2L)).isNewer(new RepoRevision(1L, new Date(3))));
		assertFalse(new RepoRevision(1L, new Date(3)).isNewer(new RepoRevision(2L, new Date(2))));
		
		assertTrue(new RepoRevision(2L, now).isNewer(new RepoRevision(old)));
		assertFalse(new RepoRevision(2L, now).isNewer(new RepoRevision(now)));
		assertFalse(new RepoRevision(old).isNewer(new RepoRevision(2L, now)));
		assertTrue(new RepoRevision(now).isNewer(new RepoRevision(2L, old)));
		
		try {
			new RepoRevision(1, null).isNewer(new RepoRevision(now));
			fail("can't be comparable");
		} catch (IllegalArgumentException e) {
			// expected
		}
		
		try {
			new RepoRevision(now).isNewer(new RepoRevision(1, null));
			fail("can't be comparable");
		} catch (IllegalArgumentException e) {
			// expected
		}
	}
	
	@Test
	public void testParseISO() {
		RepoRevision.parse("2012-10-03T03:53:54.616837Z");
		assertEquals(1000L, RepoRevision.parse("1970-01-01T00:00:01Z").getTime());
		assertEquals(1002L, RepoRevision.parse("1970-01-01T00:00:01.002Z").getTime());
		assertEquals(1003L, RepoRevision.parse("1970-01-01T00:00:01.003000Z").getTime());
	}
	
}
