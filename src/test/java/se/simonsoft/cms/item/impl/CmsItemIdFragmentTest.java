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
package se.simonsoft.cms.item.impl;

import static org.junit.Assert.*;

import org.junit.Test;

import se.simonsoft.cms.item.CmsItemPath;

public class CmsItemIdFragmentTest {

	
	@Test
	public void testConstructor() {
		CmsItemIdFragment id = new CmsItemIdFragment("x-svn:///svn/demo1^/vvab/xml/topic.dita#topic1/para1");
		assertEquals("verify toString",  "x-svn:///svn/demo1^/vvab/xml/topic.dita#topic1/para1", id.toString());
		assertEquals("Path should not include the fragment", new CmsItemPath("/vvab/xml/topic.dita"), id.getItemId().getRelPath());
		assertEquals("Fragment correctly extracted", "topic1/para1", id.getFragment());
		assertEquals("Fragment preserved", "x-svn:///svn/demo1^/vvab/xml/topic.dita#topic1/para1", id.getLogicalId());
		
		CmsItemIdFragment idNewPath = id.withRelPath(new CmsItemPath("/vvab/xml/demo.dita"));
		assertEquals("Path should have changed", new CmsItemPath("/vvab/xml/demo.dita"), idNewPath.getItemId().getRelPath());
		assertEquals("Fragment preserved when path changed", "topic1/para1", idNewPath.getFragment());	
	}
	
	
	@Test
	public void testConstructorRev() {
		CmsItemIdFragment id = new CmsItemIdFragment("x-svn:///svn/demo1^/vvab/xml/topic.dita?p=123#topic1/para1");
		assertEquals("Path should not include the fragment", new CmsItemPath("/vvab/xml/topic.dita"), id.getItemId().getRelPath());
		assertEquals("Revision",  new Long(123), id.getItemId().getPegRev());
		assertEquals("Fragment correctly extracted", "topic1/para1", id.getFragment());
		
		CmsItemIdFragment idNewPath = id.withRelPath(new CmsItemPath("/vvab/xml/demo.dita"));
		CmsItemIdFragment idNewRev = id.withPegRev(999L);
		id = null;
		assertEquals("Path should have changed", new CmsItemPath("/vvab/xml/demo.dita"), idNewPath.getItemId().getRelPath());
		assertEquals("Revision preserved",  new Long(123), idNewPath.getItemId().getPegRev());
		assertEquals("Fragment preserved", "topic1/para1", idNewPath.getFragment());
		
		assertEquals("Revision changed",  new Long(999), idNewRev.getItemId().getPegRev());
		assertEquals("Fragment preserved", "topic1/para1", idNewRev.getFragment());
	}
	
	@Test
	public void testConstructorHostname() {
		CmsItemIdFragment id = new CmsItemIdFragment("x-svn://hostname.example.com/svn/demo1^/vvab/xml/topic.dita#topic1/para1");
		assertEquals("verify toString",  "x-svn://hostname.example.com/svn/demo1^/vvab/xml/topic.dita#topic1/para1", id.toString());
		assertEquals("Path should not include the fragment", new CmsItemPath("/vvab/xml/topic.dita"), id.getItemId().getRelPath());
		assertEquals("Fragment correctly extracted", "topic1/para1", id.getFragment());
		assertEquals("Fragment preserved", "x-svn:///svn/demo1^/vvab/xml/topic.dita#topic1/para1", id.getLogicalId());
		assertEquals("Fragment preserved", "x-svn://hostname.example.com/svn/demo1^/vvab/xml/topic.dita#topic1/para1", id.getLogicalIdFull());	
	}
	
	@Test
	public void testConstructorNoFragment() {
		CmsItemIdFragment id = new CmsItemIdFragment("x-svn:///svn/demo1^/vvab/xml/topic.dita");
		assertEquals("Path", new CmsItemPath("/vvab/xml/topic.dita"), id.getItemId().getRelPath());
		assertNull("Fragment should be null", id.getFragment());
		assertEquals("LogicalId", "x-svn:///svn/demo1^/vvab/xml/topic.dita", id.getLogicalId());
		
		CmsItemIdFragment idNewPath = id.withRelPath(new CmsItemPath("/vvab/xml/demo.dita"));
		assertEquals("Path should have changed", new CmsItemPath("/vvab/xml/demo.dita"), idNewPath.getItemId().getRelPath());
		assertNull("Fragment should still be null", idNewPath.getFragment());	
	}
	
	@Test // Non-ASCII is actually allowed.
	public void testValidateFragmentNonAscii() {
		new CmsItemIdFragment("x-svn:///svn/demo1^/vvab/xml/topic.dita#topic1/böp");
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testValidateFragmentHash() {
		new CmsItemIdFragment("x-svn:///svn/demo1^/vvab/xml/topic.dita#topic1/para1#bogus");
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testValidateFragmentEmpty() {
		new CmsItemIdFragment("x-svn:///svn/demo1^/vvab/xml/topic.dita#");
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testValidateFragmentNumberFirst() {
		new CmsItemIdFragment("x-svn:///svn/demo1^/vvab/xml/topic.dita#1topic1/para1#bogus");
	}
	
	
	@Test(expected=IllegalArgumentException.class)
	public void testValidateFragmentAmpersand() {
		new CmsItemIdFragment("x-svn:///svn/demo1^/vvab/xml/topic.dita#topic1/p&ra1");
	}
	
	@Test
	public void testEquals() {
		CmsItemIdFragment id = new CmsItemIdFragment("x-svn:///svn/demo1^/vvab/xml/topic.dita#topic1/para1");
		assertTrue(id.equals(id));
		assertTrue(id.equals(new CmsItemIdFragment("x-svn:///svn/demo1^/vvab/xml/topic.dita#topic1/para1")));
		assertTrue(id.equals(new CmsItemIdFragment("x-svn://hostname.example.com/svn/demo1^/vvab/xml/topic.dita#topic1/para1")));
		assertFalse(id.equals(new CmsItemIdFragment("x-svn:///svn/demo1^/vvab/xml/topic.dita#different/fragment")));
		assertFalse(id.equals(new CmsItemIdFragment("x-svn:///svn/demo1^/vvab/xml/topic.dita?p=123#topic1/para1")));
		
		assertTrue(id.equals(new CmsItemIdFragment(new CmsItemIdArg("x-svn:///svn/demo1^/vvab/xml/topic.dita"), "topic1/para1")));
	}
	
	@Test
	public void testBaselineRev() {
		CmsItemIdFragment id = new CmsItemIdFragment("x-svn:///svn/demo1^/vvab/xml/topic.dita#topic1/para1");
		CmsItemIdFragment idRev = new CmsItemIdFragment("x-svn:///svn/demo1^/vvab/xml/topic.dita?p=123#topic1/para1");
		
		assertNull(id.getItemId().getPegRev());
		assertEquals(new Long(123), idRev.getItemId().getPegRev());
		
		assertEquals("baseline rev added to HEAD-id", new Long(11), id.withBaselineRev(11L).getItemId().getPegRev());
		assertEquals("baseline rev lowering existing rev", new Long(11), idRev.withBaselineRev(11L).getItemId().getPegRev());
		
		assertEquals("higher baseline rev not affecting and existing rev", new Long(123), idRev.withBaselineRev(999L).getItemId().getPegRev());
		
		assertEquals("preserve fragment after adding baseline", "x-svn:///svn/demo1^/vvab/xml/topic.dita?p=11#topic1/para1", id.withBaselineRev(11L).getLogicalId());
		assertEquals("equals another id after adding baselineRev identical to other IDs pegRev", idRev, id.withBaselineRev(123L));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testBaselineRevNull() {
		CmsItemIdFragment id = new CmsItemIdFragment("x-svn:///svn/demo1^/vvab/xml/topic.dita#topic1/para1");
		
		id.withBaselineRev(null);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testBaselineRevZero() {
		CmsItemIdFragment id = new CmsItemIdFragment("x-svn:///svn/demo1^/vvab/xml/topic.dita#topic1/para1");
		
		id.withBaselineRev(0L);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testBaselineRevNegative() {
		CmsItemIdFragment id = new CmsItemIdFragment("x-svn:///svn/demo1^/vvab/xml/topic.dita#topic1/para1");
		
		id.withBaselineRev(-1L);
	}

}
