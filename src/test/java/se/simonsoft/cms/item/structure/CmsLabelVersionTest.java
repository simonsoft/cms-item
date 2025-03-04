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
		assertEquals("@@@@@@@@@A-", l.getLabelSort());
		assertEquals(1, l.getSegments().size());
		assertEquals("A", l.getSegments().get(0));
		assertEquals("@@@@@@@@@A", l.getSegmentsSort().get(0));
		assertEquals(Long.valueOf(10), l.getVersionSegmentsNumeric().get(0));
		
		assertEquals("A", l.toString());
	}
	
	@Test
	public void testSimpleSort1() { // Semver 2.0.0 introduced prerelease strings.
		TreeSet<CmsLabelVersion> set = new TreeSet<CmsLabelVersion>();
		set.add(new CmsLabelVersion("10"));
		set.add(new CmsLabelVersion("2"));
		set.add(new CmsLabelVersion("20"));
		set.add(new CmsLabelVersion("a"));
		set.add(new CmsLabelVersion("b"));
		set.add(new CmsLabelVersion("aa"));
		set.add(new CmsLabelVersion("ba"));
		set.add(new CmsLabelVersion("A"));
		set.add(new CmsLabelVersion("B"));
		set.add(new CmsLabelVersion("AA"));
		set.add(new CmsLabelVersion("BA"));
		set.add(new CmsLabelVersion("9"));
		set.add(new CmsLabelVersion("99"));
		set.add(new CmsLabelVersion("999"));
		
		//assertEquals("AAAAAAAAAB-", new CmsLabelVersion("B").getLabelSort());
		//assertEquals("aaaaaaaaab-", new CmsLabelVersion("b").getLabelSort());
		
		assertEquals(14, set.size());
		Iterator<CmsLabelVersion> it = set.iterator();
		assertEquals("2", it.next().getLabel());
		assertEquals("9", it.next().getLabel());
		assertEquals("10", it.next().getLabel());
		assertEquals("20", it.next().getLabel());
		assertEquals("99", it.next().getLabel());
		assertEquals("999", it.next().getLabel());
		assertEquals("A", it.next().getLabel());
		assertEquals("B", it.next().getLabel());
		assertEquals("AA", it.next().getLabel());
		assertEquals("BA", it.next().getLabel());
		assertEquals("a", it.next().getLabel());
		assertEquals("b", it.next().getLabel());
		assertEquals("aa", it.next().getLabel());
		assertEquals("ba", it.next().getLabel());
	}

	@Test
	public void testSimple2() {
		CmsLabelVersion l = new CmsLabelVersion("A.2");
		assertEquals("A.2", l.getLabel());
		assertEquals("@@@@@@@@@A./////////2-", l.getLabelSort());
		assertEquals(2, l.getSegments().size());
		assertEquals("A", l.getSegments().get(0));
		assertEquals("2", l.getSegments().get(1));
		assertEquals("@@@@@@@@@A", l.getSegmentsSort().get(0));
		assertEquals("/////////2", l.getSegmentsSort().get(1));
		assertEquals(Long.valueOf(10), l.getVersionSegmentsNumeric().get(0));
		assertEquals(Long.valueOf(2), l.getVersionSegmentsNumeric().get(1));
	}
	
	@Test
	public void testSimple3() {
		CmsLabelVersion l = new CmsLabelVersion("A.2.1234");
		assertEquals("A.2.1234", l.getLabel());
		assertEquals("@@@@@@@@@A./////////2.//////1234-", l.getLabelSort());
		assertEquals(3, l.getSegments().size());
		assertEquals("A", l.getSegments().get(0));
		assertEquals("2", l.getSegments().get(1));
		assertEquals("1234", l.getSegments().get(2));
		assertEquals("@@@@@@@@@A", l.getSegmentsSort().get(0));
		assertEquals("/////////2", l.getSegmentsSort().get(1));
		assertEquals("//////1234", l.getSegmentsSort().get(2));
		assertEquals(Long.valueOf(10), l.getVersionSegmentsNumeric().get(0));
		assertEquals(Long.valueOf(2), l.getVersionSegmentsNumeric().get(1));
		assertEquals(Long.valueOf(1234), l.getVersionSegmentsNumeric().get(2));
	}
	
	@Test
	public void testSimple5() {
		CmsLabelVersion l = new CmsLabelVersion("A.2.1234.b-876.arm");
		assertEquals("A.2.1234.b-876.arm", l.getLabel());
		assertEquals("@@@@@@@@@A./////////2.//////1234.`````````b+///////876.arm", l.getLabelSort());
		assertEquals(6, l.getSegments().size());
		assertEquals("A", l.getSegments().get(0));
		assertEquals("2", l.getSegments().get(1));
		assertEquals("1234", l.getSegments().get(2));
		assertEquals("b", l.getSegments().get(3));
		assertEquals("876", l.getSegments().get(4));
		assertEquals("arm", l.getSegments().get(5));
		assertEquals("@@@@@@@@@A", l.getSegmentsSort().get(0));
		assertEquals("/////////2", l.getSegmentsSort().get(1));
		assertEquals("//////1234", l.getSegmentsSort().get(2));
		assertEquals("`````````b", l.getSegmentsSort().get(3));
		assertEquals("///////876", l.getSegmentsSort().get(4));
		assertEquals("arm", l.getSegmentsSort().get(5));
		
		assertEquals(4, l.getVersionSegments().size());
		assertEquals("A", l.getVersionSegments().get(0));
		assertEquals("2", l.getVersionSegments().get(1));
		assertEquals("1234", l.getVersionSegments().get(2));
		assertEquals("b", l.getVersionSegments().get(3));
		assertEquals("@@@@@@@@@A", l.getVersionSegmentsSort().get(0));
		assertEquals("/////////2", l.getVersionSegmentsSort().get(1));
		assertEquals("//////1234", l.getVersionSegmentsSort().get(2));
		assertEquals("`````````b", l.getVersionSegmentsSort().get(3));
		
		assertEquals(2, l.getPrereleaseSegments().size());
		assertEquals("876", l.getPrereleaseSegments().get(0));
		assertEquals("arm", l.getPrereleaseSegments().get(1));
		assertEquals("///////876", l.getPrereleaseSegmentsSort().get(0));
		assertEquals("arm", l.getPrereleaseSegmentsSort().get(1));
	}
	
	@Test
	public void testLongSegment() { // Long segments simply not padded.
		CmsLabelVersion l = new CmsLabelVersion("A.abcdeXfghijk.1234");
		assertEquals("A.abcdeXfghijk.1234", l.getLabel());
		assertEquals("@@@@@@@@@A.abcdeXfghijk.//////1234-", l.getLabelSort());
		assertEquals(3, l.getSegments().size());
		assertEquals("A", l.getSegments().get(0));
		assertEquals("abcdeXfghijk", l.getSegments().get(1));
		assertEquals("1234", l.getSegments().get(2));
		assertEquals("@@@@@@@@@A", l.getSegmentsSort().get(0));
		assertEquals("abcdeXfghijk", l.getSegmentsSort().get(1));
		assertEquals("//////1234", l.getSegmentsSort().get(2));
		
		try {
			l.getVersionSegmentsNumeric();
			fail("should fail, string too long");
		} catch (NumberFormatException e) {
		}
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
		assertEquals("@@@@@@@@@A./////////3.////////10+beta.////////11", l.getLabelSort());
		assertEquals(5, l.getSegments().size());
		assertEquals("A", l.getSegments().get(0));
		assertEquals("3", l.getSegments().get(1));
		assertEquals("10", l.getSegments().get(2));
		assertEquals("beta", l.getSegments().get(3));
		assertEquals("11", l.getSegments().get(4));
		assertEquals("@@@@@@@@@A", l.getSegmentsSort().get(0));
		assertEquals("/////////3", l.getSegmentsSort().get(1));
		assertEquals("////////10", l.getSegmentsSort().get(2));
		assertEquals("beta", l.getSegmentsSort().get(3));
		assertEquals("////////11", l.getSegmentsSort().get(4));
		
		assertEquals(3, l.getVersionSegments().size());
		assertEquals("A", l.getVersionSegments().get(0));
		assertEquals("3", l.getVersionSegments().get(1));
		assertEquals("10", l.getVersionSegments().get(2));
		assertEquals("@@@@@@@@@A", l.getVersionSegmentsSort().get(0));
		assertEquals("/////////3", l.getVersionSegmentsSort().get(1));
		assertEquals("////////10", l.getVersionSegmentsSort().get(2));
		
		
		assertEquals(2, l.getPrereleaseSegments().size());
		assertEquals("beta", l.getPrereleaseSegments().get(0));
		assertEquals("11", l.getPrereleaseSegments().get(1));
		assertEquals("beta", l.getPrereleaseSegmentsSort().get(0));
		assertEquals("////////11", l.getPrereleaseSegmentsSort().get(1));
		
		// Release of the same Beta
		CmsLabelVersion lR = new CmsLabelVersion("A.3.10");

		TreeSet<CmsLabelVersion> set = new TreeSet<CmsLabelVersion>();
		set.add(lR);
		set.add(l);
		Iterator<CmsLabelVersion> it = set.iterator();
		assertEquals("A.3.10-beta.11", it.next().getLabel());
		assertEquals("A.3.10", it.next().getLabel());
		
		
		assertFalse("not sortable label", l.getLabel().compareTo(lR.getLabel()) < 1);
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
	public void testSemverTrail1() { // Trailing hyphen preserved.
		CmsLabelVersion l = new CmsLabelVersion("A.3.10-");
		assertEquals("A.3.10-", l.getLabel());
		assertEquals("@@@@@@@@@A./////////3.////////10+", l.getLabelSort());
		
		assertEquals(4, l.getSegments().size());
		assertEquals(3, l.getVersionSegments().size());
		assertEquals(1, l.getPrereleaseSegments().size());
		assertEquals("A", l.getSegments().get(0));
		assertEquals("3", l.getSegments().get(1));
		assertEquals("10", l.getSegments().get(2));
		assertEquals("", l.getSegments().get(3));
		assertEquals("@@@@@@@@@A", l.getSegmentsSort().get(0));
		assertEquals("/////////3", l.getSegmentsSort().get(1));
		assertEquals("////////10", l.getSegmentsSort().get(2));
		assertEquals("", l.getSegmentsSort().get(3));
	}
	
	@Test
	public void testSemverTrailPre1() { // Trailing hyphen considered empty prerelease, makes no sense.
		CmsLabelVersion l = new CmsLabelVersion("A.3.10-beta-");
		assertEquals("A.3.10-beta-", l.getLabel());
		assertEquals("@@@@@@@@@A./////////3.```10-beta+", l.getLabelSort());
		assertEquals(4, l.getSegments().size());
		assertEquals(3, l.getVersionSegments().size());
		assertEquals(1, l.getPrereleaseSegments().size());
		
		// Will be sorted before same version without hyphen, considered prerelease despite empty.
		CmsLabelVersion lB = new CmsLabelVersion("A.3.10-beta");
		TreeSet<CmsLabelVersion> set = new TreeSet<CmsLabelVersion>();
		set.add(lB);
		set.add(l);
		set.add(new CmsLabelVersion("A.3.100"));
		Iterator<CmsLabelVersion> it = set.iterator();
		assertEquals("A.3.10-beta", it.next().getLabel());
		assertEquals("A.3.100", it.next().getLabel());
		assertEquals("A.3.10-beta-", it.next().getLabel()); // This is unexpected, but the trailing hyphen makes no sense.
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
		assertEquals(",", it.next());
		assertEquals("-", it.next());
		assertEquals(".", it.next());
		assertEquals(":", it.next());
		assertEquals(";", it.next());
		assertEquals("_", it.next());
	}
}
