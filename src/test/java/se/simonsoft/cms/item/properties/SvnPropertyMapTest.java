package se.simonsoft.cms.item.properties;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

import se.simonsoft.cms.item.properties.SvnPropertyMap;

public class SvnPropertyMapTest {
	
	@Test
	public void testPutPropertyNewString() {

		
			SvnPropertyMap map = new SvnPropertyMap();
			map.putProperty("theKey", "TheValue");
			
			assertTrue("should contain the prop", map.containsProperty("theKey"));
			assertEquals("TheValue", map.getProperty("theKey").getValue());
			assertTrue("should be considered modified since added with putProperty", map.getProperty("theKey")
					.isModified());
	}
	
	@Test
	@Ignore // Still evaluating API 
	public void testPutPropertyNewlineSeparated() {
		SvnPropertyMap map = new SvnPropertyMap();
		map.putProperty("abx:ReleaseMaster", "x-svn:///svn/r1/i3\n \n\t x-svn:///svn/r1/i2 ");
		
		assertTrue(map.containsProperty("abx:ReleaseMaster"));
		List<String> list = map.getList("abx:ReleaseMaster");
		assertNotNull("Should allow values as list", list);
		assertTrue("Should split on newline", 2 <= list.size());
		assertEquals("Should be same order as in value", "x-svn:///svn/r1/i3", list.get(0));
		assertEquals("Should ignore empty lines", 1, list.size());
		assertEquals("Should trim all values", "x-svn:///svn/r1/i2", list.get(1));
		
		map.putProperty("p1", "x-svn:///svn/r1/i2");
		assertEquals("Must allow any single value as list if we are to support newline split lists", 
				"x-svn:///svn/r1/i2", map.getList("p1").get(0));
		
		assertEquals("These values must be retrievable as String too, unlike json",
				"x-svn:///svn/r1/i3\n\t x-svn:///svn/r1/i2 ", map.getString("abx:ReleaseMaster"));
	}
	
	// How do we remodel these tests into something useful with the new APIs?
	/*
	@Test
	public void testObjectReferenceAfterSet() {
		SvnPropertyMap map = new SvnPropertyMap(null, null);
		// first service references a propety value
		SvnPropertyValueString v1 = new SvnPropertyValueString("value");
		map.putProperty("key1", v1);
		// second service changes the value
		map.putProperty("key1", new SvnPropertyValueString("value2"));
		// this behavior is a bit unexpected but it is a feature in 1.0 that existing code may rely on 
		assertEquals("all references to the value instant should have the new value", "value2", v1.getValue());
		// TODO same requirement for list values?
		// TODO what if values are committed and then refreshed and then changed somewhere else?
	}
	
	@Test
	public void testDifferentKeysSameValue() {
		SvnPropertyMap map = new SvnPropertyMap(null, null);
		SvnPropertyValueString s = new SvnPropertyValueString("In_Work");
		map.putProperty("cms:status", s);
		map.putProperty("custom:status", s);
		// ... authormaster and translationmaster would be another example
		
		map.putProperty("custom:status", new SvnPropertyValueString("On_Hold"));
		assertEquals("should have new value", "On_Hold", map.getString("custom:status"));
		// TODO is this the desired behavior?
		assertEquals("expected to affect the other key too", "On_Hold", map.getString("cms:status"));
		// TODO same for lists?
		// TODO what if values are committed and then refreshed?
	}
	*/
	
	@Test
	public void testGetPropertyWrongType() {
		SvnPropertyMap map = new SvnPropertyMap();
		assertNull("Should return null when the property does not exist", map.getString("key1"));
		map.putProperty("key1", new LinkedList<String>());
		String wrongType = map.getString("key1");
		assertNull("In the edit API, getString should return null if prop was set as list", wrongType);
		assertTrue("Callers can use containsPropety to see why they got null", map.containsProperty("key1"));
		// We could add API to set the original value too
		//map.putProperty("key1", new LinkedList<String>(), "[]");
	}

	@Test
	public void testGetPropertyValueString() {
		SvnPropertyMap map = new SvnPropertyMap();
		map.store("key1", "[]");
		map.store("key2", "val");
		map.store("key3", "{}");
		CmsItemProperties props = (CmsItemProperties) map;
		assertEquals("val", props.getString("key2"));
		assertEquals("When reading properties from svn, getString should always return the exact value", 
				"[]", props.getString("key1"));
		assertEquals("getList should still return null for non-lists", null, props.getList("key2"));
	}	
	
	@Test //add restrictions after 1.0 release?//(expected=SvnFatalException.class)
	public void testPutPropertyOverwriteDifferentType() {
		SvnPropertyMap map = new SvnPropertyMap();
		map.putProperty("theKey", "TheValue");
		map.putProperty("theKey", new ArrayList<String>(Arrays.asList("TheValue", "AnotherValue")));
	}
	
	@Test
	public void testHandlePropertyString() {
		SvnPropertyMap map = new SvnPropertyMap();
		map.store("p1", "value");
		map.store("p2", " value\n");
		assertEquals("value", map.getString("p1"));
		assertEquals("values should not be trimmed", " value\n", map.getString("p2"));
	}
	
	@Test
	public void testHandlePropertyStartsWithBracket() {
		SvnPropertyMap map = new SvnPropertyMap();
		map.store("p1", "[null, \"a\"]");
		assertEquals("Should parse json list to List and allow nulls", null, map.getList("p1").get(0));
		assertEquals("Should parse json list to List", "a", map.getList("p1").get(1));
		// Should we or should we not?
		assertEquals("Should allow a JSON value to be read as string",
				"[null, \"a\"]", map.getString("p1"));
	}
	
	@Test
	public void testHandleNonStrings() {
		SvnPropertyMap map = new SvnPropertyMap();
		map.store("array", "[\"a\", 1, false, {\"key\":\"v\"}]");
		List<String> list = map.getList("array");
		assertEquals("Should parse the list even if it contains non-strings", "a", list.get(0));
		/* there is no getter for the raw parsed value anymore
		assertEquals("In fact the JSON parsing is allowed to use real types", 1L, list.get(1));
		try {
			
			Long.class.equals(list.get(1).getClass());
		} catch (ClassCastException e) {
			// To treat types explicitly the list must be casted to unknown generic type 
		}
		List<? extends Object> listUnspecified = (List<?>) list;
		assertEquals("Should be possible to handle all elements as string", 
				"1", listUnspecified.get(1).toString());
		assertTrue("With unspecified list type it should be possible to check type per element", 
				listUnspecified.get(1) instanceof Long);
		assertTrue("should support json booleans", listUnspecified.get(2) instanceof Boolean);
		assertTrue("should support json objects", listUnspecified.get(3) instanceof Map<?,?>);
		*/
	}
	
	@Test
	public void testHandleInvalidArray() {
		SvnPropertyMap map = new SvnPropertyMap();		
		map.store("p1", "[\"a\"");
		assertEquals("should be detected as string if value doesn't end with bracket",
				"[\"a\"", map.getString("p1"));
		// the following should also log a warning
		map.store("p2", "[\"1]");
		assertEquals("should fall back to string if json is invalid",
				"[\"1]", map.getString("p2"));
	}
	
	@Test
	public void testStoreOverwrite() {
		SvnPropertyMap map = new SvnPropertyMap();		
		map.store("n", "v");
		map.store("n", "x");
		assertEquals("should not happen but if it does overwrite would be expected", "x", map.getProperty("n").getValue());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testHandlePropertyNull() {
		SvnPropertyMap map = new SvnPropertyMap();
		map.store("p1", null);
	}

}
