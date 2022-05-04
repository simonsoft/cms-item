/**
 * Copyright (C) 2009-2017 Simonsoft Nordic AB
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
package se.simonsoft.cms.item.structure;

import static org.junit.Assert.*;

import java.util.Iterator;
import java.util.TreeSet;

import org.junit.Test;

public class CmsLabelVersionTest {
	
	@Test
	public void testEqualsObject() {
		assertTrue(new CmsLabelVersion("X").equals(new CmsLabelVersion("X")));
		assertFalse(new CmsLabelVersion("X").equals(new CmsLabelVersion("x")));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNull() {
		new CmsLabelVersion(null);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testEmpty() {
		new CmsLabelVersion("");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testWhitespace() {
		new CmsLabelVersion(" ");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testLeading() {
		new CmsLabelVersion(".A.1");
	}
	@Test(expected = IllegalArgumentException.class)
	public void testTrailing() {
		new CmsLabelVersion("A.1.");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConsecutive() {
		new CmsLabelVersion("A..1");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testLong1() {
		new CmsLabelVersion("123456781234567812345678");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testLong3() {
		new CmsLabelVersion("12345678.12345678.12345678");
	}
	
	@Test
	public void testSimple1() {
		CmsLabelVersion l = new CmsLabelVersion("A");
		assertEquals("A", l.getLabel());
		assertEquals("000000000A", l.getLabelSort());
		assertEquals(1, l.getSegments().size());
		assertEquals("A", l.getSegments().get(0));
		assertEquals("000000000A", l.getSegmentsSort().get(0));
		
		assertEquals("A", l.toString());
	}

	@Test
	public void testSimple2() {
		CmsLabelVersion l = new CmsLabelVersion("A.2");
		assertEquals("A.2", l.getLabel());
		assertEquals("000000000A.0000000002", l.getLabelSort());
		assertEquals(2, l.getSegments().size());
		assertEquals("A", l.getSegments().get(0));
		assertEquals("2", l.getSegments().get(1));
		assertEquals("000000000A", l.getSegmentsSort().get(0));
		assertEquals("0000000002", l.getSegmentsSort().get(1));
	}
	
	@Test
	public void testSimple3() {
		CmsLabelVersion l = new CmsLabelVersion("A.2.1234");
		assertEquals("A.2.1234", l.getLabel());
		assertEquals("000000000A.0000000002.0000001234", l.getLabelSort());
		assertEquals(3, l.getSegments().size());
		assertEquals("A", l.getSegments().get(0));
		assertEquals("2", l.getSegments().get(1));
		assertEquals("1234", l.getSegments().get(2));
		assertEquals("000000000A", l.getSegmentsSort().get(0));
		assertEquals("0000000002", l.getSegmentsSort().get(1));
		assertEquals("0000001234", l.getSegmentsSort().get(2));
	}
	
	@Test
	public void testSimple5() {
		CmsLabelVersion l = new CmsLabelVersion("A.2.1234.b-876.arm"); // The b-876 will be zero-padded before the b, only . should be treated as a separator.
		assertEquals("A.2.1234.b-876.arm", l.getLabel());
		assertEquals("000000000A.0000000002.0000001234.00000b-876.0000000arm", l.getLabelSort());
		assertEquals(5, l.getSegments().size());
		assertEquals("A", l.getSegments().get(0));
		assertEquals("2", l.getSegments().get(1));
		assertEquals("1234", l.getSegments().get(2));
		assertEquals("b-876", l.getSegments().get(3));
		assertEquals("arm", l.getSegments().get(4));
		assertEquals("000000000A", l.getSegmentsSort().get(0));
		assertEquals("0000000002", l.getSegmentsSort().get(1));
		assertEquals("0000001234", l.getSegmentsSort().get(2));
		assertEquals("00000b-876", l.getSegmentsSort().get(3));
		assertEquals("0000000arm", l.getSegmentsSort().get(4));
	}
	
	@Test
	public void testLongSegment() { // Long segments simply not padded.
		CmsLabelVersion l = new CmsLabelVersion("A.abcde-fghijk.1234");
		assertEquals("A.abcde-fghijk.1234", l.getLabel());
		assertEquals("000000000A.abcde-fghijk.0000001234", l.getLabelSort());
		assertEquals(3, l.getSegments().size());
		assertEquals("A", l.getSegments().get(0));
		assertEquals("abcde-fghijk", l.getSegments().get(1));
		assertEquals("1234", l.getSegments().get(2));
		assertEquals("000000000A", l.getSegmentsSort().get(0));
		assertEquals("abcde-fghijk", l.getSegmentsSort().get(1));
		assertEquals("0000001234", l.getSegmentsSort().get(2));
	}
	
	@Test
	public void testCompare1() {
		CmsLabelVersion l1 = new CmsLabelVersion("5.5");
		CmsLabelVersion l2 = new CmsLabelVersion("5.11");
		assertFalse("string sort not desirable", l1.toString().compareTo(l2.toString()) < 1);
		assertTrue("sortable gettor", l1.getLabelSort().compareTo(l2.getLabelSort()) < 1);
		assertTrue("sortable objects", l1.compareTo(l2) < 1);
	}

	
	@Test
	public void testSemverPre1() { // Semver 2.0.0 introduced prerelease strings.
		CmsLabelVersion l = new CmsLabelVersion("A.3.10-beta.11");
		assertEquals("A.3.10-beta.11", l.getLabel());
		//assertEquals("000000000A.0000000003.0000000010-beta.11", l.getLabelSort());
		//assertEquals(3, l.getSegments().size());
		assertEquals("A", l.getSegments().get(0));
		assertEquals("3", l.getSegments().get(1));
		//assertEquals("10", l.getSegments().get(2));
		assertEquals("000000000A", l.getSegmentsSort().get(0));
		assertEquals("0000000003", l.getSegmentsSort().get(1));
		//assertEquals("0000000010", l.getSegmentsSort().get(2));
		
		// Release of the same Beta
		CmsLabelVersion lR = new CmsLabelVersion("A.3.10");

		TreeSet<CmsLabelVersion> set = new TreeSet<CmsLabelVersion>();
		set.add(lR);
		set.add(l);
		Iterator<CmsLabelVersion> it = set.iterator();
		assertEquals("A.3.10-beta.11", it.next().getLabel());
		assertEquals("A.3.10", it.next().getLabel());
		
		
		assertTrue("sortable label in this case", l.getLabel().compareTo(lR.getLabel()) < 1);
		assertTrue("sortable gettor", l.getLabelSort().compareTo(lR.getLabelSort()) < 1);
		assertTrue("sortable objects", l.compareTo(lR) < 1);
	}
	
	@Test
	public void testSemverPreSort1() { // Semver 2.0.0 introduced prerelease strings.
		TreeSet<CmsLabelVersion> set = new TreeSet<CmsLabelVersion>();
		// Sorting example from SemVer 2.0.0
		// 1.0.0-alpha < 1.0.0-alpha.1 < 1.0.0-alpha.beta < 1.0.0-beta < 1.0.0-beta.2 < 1.0.0-beta.11 < 1.0.0-rc.1 < 1.0.0.
		set.add(new CmsLabelVersion("1.0.5"));
		set.add(new CmsLabelVersion("1.0.10"));
		set.add(new CmsLabelVersion("1.0"));
		set.add(new CmsLabelVersion("1.0.5-alpha"));
		set.add(new CmsLabelVersion("1.0.5-alpha.1"));
		set.add(new CmsLabelVersion("1.0.5-alpha.beta"));
		set.add(new CmsLabelVersion("1.0.5-beta"));
		set.add(new CmsLabelVersion("1.0.5-beta.2"));
		set.add(new CmsLabelVersion("1.0.5-beta.11"));
		set.add(new CmsLabelVersion("1.0.5-rc.1"));
		// The following are CMS additions where we allow additional identifiers in the release version.
		set.add(new CmsLabelVersion("1.0.5.0"));
		set.add(new CmsLabelVersion("1.0.5.a"));
		
		Iterator<CmsLabelVersion> it = set.iterator();
		assertEquals("1.0", it.next().getLabel());
		assertEquals("1.0.5-alpha", it.next().getLabel());
		assertEquals("1.0.5-alpha.1", it.next().getLabel());
		assertEquals("1.0.5-alpha.beta", it.next().getLabel());
		assertEquals("1.0.5-beta", it.next().getLabel());
		assertEquals("1.0.5-beta.2", it.next().getLabel());
		assertEquals("1.0.5-beta.11", it.next().getLabel());
		assertEquals("1.0.5-rc.1", it.next().getLabel());
		assertEquals("1.0.5", it.next().getLabel());
		assertEquals("1.0.5.0", it.next().getLabel());
		assertEquals("1.0.5.a", it.next().getLabel());
	}
	
	@Test
	public void testSortSeparators() {
		// Need a special indicator for "end-of-string" sorting before . and 0 but after - (or its replacement)
		// Last - becomes , or +
		// End indicated by -
		TreeSet<String> set = new TreeSet<String>();
		set.add(".");
		set.add(",");
		set.add("-");
		set.add("_");
		set.add("+");
		set.add(":");
		set.add(";");
		
		Iterator<String> it = set.iterator();
		assertEquals("+", it.next());
		assertEquals("-", it.next());
		assertEquals(".", it.next());
		assertEquals(":", it.next());
		assertEquals(";", it.next());
		assertEquals("_", it.next());
	}
}
