package se.simonsoft.cms.item.impl;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

public class CmsConfigOptionBaseTest {

	@Test
	public void test() {
		CmsConfigOptionBase<String> o = new CmsConfigOptionBase<String>("cmsconfig:bogus", "banana|mango ");
		assertEquals("cmsconfig", o.getNamespace());
		assertEquals("bogus", o.getKey());
		assertEquals("trim, always has done", "banana|mango", o.getValueString());
		assertEquals("trim, always has done", Arrays.asList("banana", "mango"), o.getValueList());
	}
	
	@Test
	public void testSplit() {
		// #1321 Adding support for comma and space.
		CmsConfigOptionBase<String> o = new CmsConfigOptionBase<String>("cmsconfig:bogus", "pear,banana, mango|grape, ");
		assertEquals("cmsconfig", o.getNamespace());
		assertEquals("bogus", o.getKey());
		assertEquals("trim, always has done", "pear,banana, mango|grape,", o.getValueString());
		assertEquals("trim, always has done", Arrays.asList("pear", "banana", "mango", "grape"), o.getValueList());
		assertEquals(4, o.getValueList().size());
	}

}
