package se.simonsoft.cms.item.impl;

import static org.junit.Assert.*;

import org.junit.Test;

import se.simonsoft.cms.item.CmsItemPath;

public class CmsItemIdFragmentTest {

	
	@Test
	public void testConstructor() {
		CmsItemIdFragment id = new CmsItemIdFragment("x-svn:///svn/demo1^/vvab/xml/topic.dita#topic1/para1");
		assertEquals("Path should not include the fragment", new CmsItemPath("/vvab/xml/topic.dita"), id.getRelPath());
		assertEquals("Fragment correctly extracted", "topic1/para1", id.getFragment());
		assertEquals("Fragment preserved", "x-svn:///svn/demo1^/vvab/xml/topic.dita#topic1/para1", id.getLogicalId());
		
		CmsItemIdFragment idNewPath = id.withRelPath(new CmsItemPath("/vvab/xml/demo.dita"));
		assertEquals("Path should have changed", new CmsItemPath("/vvab/xml/demo.dita"), idNewPath.getRelPath());
		assertEquals("Fragment preserved when path changed", "topic1/para1", idNewPath.getFragment());	
	}
	
	
	@Test
	public void testConstructorRev() {
		CmsItemIdFragment id = new CmsItemIdFragment("x-svn:///svn/demo1^/vvab/xml/topic.dita?p=123#topic1/para1");
		assertEquals("Path should not include the fragment", new CmsItemPath("/vvab/xml/topic.dita"), id.getRelPath());
		assertEquals("Revision",  new Long(123), id.getPegRev());
		assertEquals("Fragment correctly extracted", "topic1/para1", id.getFragment());
		
		CmsItemIdFragment idNewPath = id.withRelPath(new CmsItemPath("/vvab/xml/demo.dita"));
		CmsItemIdFragment idNewRev = id.withPegRev(999L);
		id = null;
		assertEquals("Path should have changed", new CmsItemPath("/vvab/xml/demo.dita"), idNewPath.getRelPath());
		assertEquals("Revision preserved",  new Long(123), idNewPath.getPegRev());
		assertEquals("Fragment preserved", "topic1/para1", idNewPath.getFragment());
		
		assertEquals("Revision changed",  new Long(999), idNewRev.getPegRev());
		assertEquals("Fragment preserved", "topic1/para1", idNewRev.getFragment());
	}
	
	@Test
	public void testConstructorHostname() {
		CmsItemIdFragment id = new CmsItemIdFragment("x-svn://hostname.example.com/svn/demo1^/vvab/xml/topic.dita#topic1/para1");
		assertEquals("Path should not include the fragment", new CmsItemPath("/vvab/xml/topic.dita"), id.getRelPath());
		assertEquals("Fragment correctly extracted", "topic1/para1", id.getFragment());
		assertEquals("Fragment preserved", "x-svn:///svn/demo1^/vvab/xml/topic.dita#topic1/para1", id.getLogicalId());
		assertEquals("Fragment preserved", "x-svn://hostname.example.com/svn/demo1^/vvab/xml/topic.dita#topic1/para1", id.getLogicalIdFull());	
	}
	
	@Test
	public void testConstructorNoFragment() {
		CmsItemIdFragment id = new CmsItemIdFragment("x-svn:///svn/demo1^/vvab/xml/topic.dita");
		assertEquals("Path", new CmsItemPath("/vvab/xml/topic.dita"), id.getRelPath());
		assertNull("Fragment should be null", id.getFragment());
		assertEquals("LogicalId", "x-svn:///svn/demo1^/vvab/xml/topic.dita", id.getLogicalId());
		
		CmsItemIdFragment idNewPath = id.withRelPath(new CmsItemPath("/vvab/xml/demo.dita"));
		assertEquals("Path should have changed", new CmsItemPath("/vvab/xml/demo.dita"), idNewPath.getRelPath());
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

}
