package se.simonsoft.cms.item.properties;

import static org.junit.Assert.*;
//import static org.mockito.Mockito.*;

import org.junit.Test;

import se.simonsoft.cms.item.properties.SvnPropertyValue;
import se.simonsoft.cms.item.properties.SvnPropertyValueString;

public class SvnPropertyValueStringTest {

	
	@Test
	public void testConstructorString() {
		
		SvnPropertyValue<String> a = new SvnPropertyValueString("TheValue");
		assertEquals("TheValue", a.getValue());
		assertTrue(a.isModified());
		
		SvnPropertyValue<String> b = new SvnPropertyValueString("TheValue", false);
		assertEquals("TheValue", b.getValue());
		assertFalse(b.isModified());
	}
	
	@Test
	public void testConstructorEmpty() {
		
		SvnPropertyValueString a = new SvnPropertyValueString(null, false);
		assertNull(a.getValue());
		assertFalse(a.isModified());
		
		SvnPropertyValue<String> b = a.setString("TheValue");
		assertEquals("TheValue", b.getValue());
		assertTrue(b.isModified());
		
	}
	
	@Test
	public void testSetString() {
		
		SvnPropertyValueString a = new SvnPropertyValueString("TheValue", false);
		assertEquals("TheValue", a.getValue());
		assertFalse(a.isModified());
		
		SvnPropertyValue<String> b = a.setString("TheValue");
		assertEquals("TheValue", b.getValue());
		assertFalse(b.isModified());
		
		SvnPropertyValue<String> c = a.setString("Changed Value");
		assertEquals("Changed Value", c.getValue());
		assertTrue(c.isModified());
	}
	
	@Test
	public void testSetValue() {
		
		SvnPropertyValueString a = new SvnPropertyValueString("TheValue", false);
		String b = "TheValue";
		String c = "Changed Value";

		assertEquals("TheValue", a.getValue());
		assertFalse(a.isModified());
		
		SvnPropertyValue<String> bv = a.setString(b);
		assertEquals("TheValue", bv.getValue());
		assertFalse(bv.isModified());
		
		SvnPropertyValue<String> cv = a.setString(c);
		assertEquals("Changed Value", cv.getValue());
		assertTrue(cv.isModified());
	}
	
	@Test
	public void testGetSvnString() {
		
		// This test is a placeholder. 
		// It will be important when implementing StringList support.
		
		SvnPropertyValue<String> a = new SvnPropertyValueString("TheValue");
		assertEquals("TheValue", a.getSvnString());

	}
	
	@Test
	public void testEquals() {
		
		SvnPropertyValue<String> a = new SvnPropertyValueString("TheValue");
		SvnPropertyValueString b = new SvnPropertyValueString(null, false);
		SvnPropertyValue<String> c = b.setString("TheValue");
		
		assertEquals(a.toString(), c.toString());
		assertEquals(a.getValue(), c.getValue());
		assertEquals(a.getSvnString(), c.getSvnString());
		assertTrue(a.equals(c));

		assertTrue("should be Object equal", a.equals((Object) c));
		assertEquals("The hashCode must also be equal for correct hashmap behaviour.", a.hashCode(), c.hashCode());
	}
	
	
}
