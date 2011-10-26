package se.simonsoft.cms.item.properties;

import static org.junit.Assert.*;

import java.util.List;
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
	
	@Test
	public void testNewlineSeparated() {
		SvnPropertyValueString p = new SvnPropertyValueString("x-svn:///svn/r1/i1\nx-svn:///svn/r1/i2", false);
		List<String> v = p.getValueNewlineSeparated();
		assertNotNull("Should support newline separated string values", v);
		assertEquals("Should split on newline", 2, v.size());
		assertFalse(p.isModified());
		SvnPropertyValue<String> p2 = p.setString("x-svn:///svn/r1/i1\nx-svn:///svn/r1/i2\n");
		assertTrue("Any value changes, including those invisible after split, count as modifications", p2.isModified());
	}

	@Test
	public void testNewlineSeparatedTrim() {
		SvnPropertyValueString p = new SvnPropertyValueString("\t x x x\r\n z z\t z \n\n  \t \n yyy ");
		List<String> v = p.getValueNewlineSeparated();
		assertEquals("Should find 3 non-empty lines", 3, v.size());
		assertEquals("x x x", v.get(0));
		assertEquals("z z\t z", v.get(1));
		assertEquals("yyy", v.get(2));
	}
	
	@Test
	public void testNewlineSeparatedSingleValue() {
		SvnPropertyValueString p = new SvnPropertyValueString("x-svn:///svn/r1/i1");
		assertEquals(1, p.getValueNewlineSeparated().size());
		assertEquals("x-svn:///svn/r1/i1", p.getValueNewlineSeparated().get(0));
	}
	
	@Test
	public void testNewlineSeparatedEmpty() {
		SvnPropertyValueString p = new SvnPropertyValueString("  \n ");
		assertEquals("  \n ", p.getSvnString());
		assertEquals(0, p.getValueNewlineSeparated().size());
	}
	
}
