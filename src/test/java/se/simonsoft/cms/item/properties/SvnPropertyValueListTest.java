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
package se.simonsoft.cms.item.properties;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.junit.Test;

import se.simonsoft.cms.item.properties.SvnPropertyValue;
import se.simonsoft.cms.item.properties.SvnPropertyValueList;
import se.simonsoft.cms.item.properties.ValueParseException;

public class SvnPropertyValueListTest {	
	
	@Test
	public void testParseNotJson() {
		String svnPropertyValue = "[a]";
		try {
			new SvnPropertyValueList(svnPropertyValue);
			fail("String constructor should throw exception when the value is not a json array");
		} catch (ValueParseException e) {
			// expected
		}
	}
	
	@Test
	public void testGetSvnString() {
		List<String> values = Arrays.asList("a", "b");
		SvnPropertyValue<List<String>> v = new SvnPropertyValueList(values);
		assertEquals("[\"a\",\"b\"]", v.getSvnString());
	}
	
	@Test
	public void testEquals() {
		// two different list implementations
		SvnPropertyValue<List<String>> v1 = new SvnPropertyValueList(Arrays.asList("a", "b"));
		List<String> v2 = new LinkedList<String>();
		assertFalse(v1.equals(new SvnPropertyValueList(v2)));
		assertFalse(new SvnPropertyValueList(v2).equals(v1));
		v2.add("a");
		assertFalse(v1.equals(new SvnPropertyValueList(v2)));
		v2.add("b");
		assertTrue("Should be equal because they result in the same JSON",
				v1.equals(new SvnPropertyValueList(v2)));
		assertTrue(new SvnPropertyValueList(v2).equals(v1));
		assertTrue("different modified state - should still be equal because the json is the same",
				new SvnPropertyValueList(v2, false).equals(new SvnPropertyValueList(v2, true)));
		// Whenever a.equals(b), then a.hashCode() must be same as b.hashCode().
		assertEquals("equal objects should have the same hashCode",
				v1.hashCode(), new SvnPropertyValueList(v2).hashCode());
	}
	
	// We want immutability outside the package. While being more pragmatic inside.
	@Test
	public void testImmutableAtConstruction() {
		List<String> values = new LinkedList<String>();
		values.add("a");
		SvnPropertyValue<List<String>> v = new SvnPropertyValueList(values);
		values.add("b");
		assertEquals("The property value should not have changed", 1, v.getValue().size());
	}
	
	@Test
	public void testImmutableAtGet() {
		List<String> values = new LinkedList<String>();
		values.add("a");
		SvnPropertyValue<List<String>> v = new SvnPropertyValueList(values);
		v.getValue().add("b");
		assertEquals("The property value should not have changed", 1, v.getValue().size());
	}
	
	/**
	 * Let's use a static method for supporting the weird setAttribute API.
	 * <br>
	 * There is a special use case because of how arbortext handles attributes giving 
	 * them fixed indices that might not be continuous.
	 * 
	 * Note for SvnObject.setAttribute when getting index > 0:
	 * - Get current Value.
	 *  - If already a list, great.
	 *  - else if exists: Create List Value with one item.
	 *  - else if not exist: Create List Value with one null-item.
	 * - Use setItem to set the new value.
	 */
	@Test
	public void testSetItemImmutable() {

		List<String> values = Arrays.asList("a", "b");
		SvnPropertyValue<List<String>> v = new SvnPropertyValueList(values);
		assertEquals("[\"a\",\"b\"]", v.getSvnString());
		// So far was setup
		// Replacing the second item (0-based index)
		SvnPropertyValue<List<String>> v2 = new SvnPropertyValueList(SvnPropertyValueList.set(v.getValue(), 1, "bb"));
		assertEquals("[\"a\",\"bb\"]", v2.getSvnString());
		// Testing discontinuous index
		SvnPropertyValue<List<String>> v3 = new SvnPropertyValueList(SvnPropertyValueList.set(v.getValue(), 3, "d"));
		assertEquals("discontinuous index", "[\"a\",\"b\",null,\"d\"]", v3.getSvnString());
		// Testing shrinking the list
		SvnPropertyValue<List<String>> v4 = new SvnPropertyValueList(SvnPropertyValueList.set(v3.getValue(), 3, null));
		assertEquals("shrinking the list - JSON test", v.getSvnString(), v4.getSvnString());
		assertEquals("shrinking the list - equals test", v, v4);
		// Stable indices
		SvnPropertyValue<List<String>> v5 = new SvnPropertyValueList(SvnPropertyValueList.set(v.getValue(), 0, null));
		assertEquals("Stable indices", "[null,\"b\"]", v5.getSvnString());
	}

	@Test
	public void testSetItemImmutableMap() throws ValueParseException {
		List<Map<String, String>> values = new ArrayList<Map<String,String>>();
		Map<String,String> m1 = new TreeMap<String,String>();
		m1.put("a", "x");
		values.add(m1);
		Map<String,String> m2 = new TreeMap<String,String>();
		m2.put("b", "y");
		values.add(m2);
		
		SvnPropertyValueList v = new SvnPropertyValueList(values);
		assertEquals("[{\"a\":\"x\"},{\"b\":\"y\"}]", v.getSvnString());
		
		v.getValueMaps().get(0).put("new", "entry");
		assertEquals("changing the retuned map should not affect current value",
				"[{\"a\":\"x\"},{\"b\":\"y\"}]", v.getSvnString());
		
		// use the static helper method
		Map<String,String> m3 = new TreeMap<String,String>();
		m3.put("c", "z");
		values.add(m3);		
		SvnPropertyValueList v2 = new SvnPropertyValueList(
				SvnPropertyValueList.set(v.getValueMaps(), 0, m3));
		assertEquals("should overwrite existing value at index 0",
				"[{\"c\":\"z\"},{\"b\":\"y\"}]", v2.getSvnString());
	}
	
	@Test
	public void testStrictGetValueString() throws ValueParseException {
		SvnPropertyValue<List<String>> val = new SvnPropertyValueList("[\"\", \"a\", null, 1, false, {\"k\":\"v\"}]");
		List<String> list = val.getValue();
		assertEquals("plain stings should be trivial", "a", list.get(1));
		assertEquals("empty stings should be trivial", "", list.get(0));
		assertEquals("null should be preserved", null, list.get(2));
		assertEquals("integers should be strings", "1", list.get(3));
		assertEquals("booleans should be strings", "false", list.get(4));
		assertEquals("objects should be json", "{\"k\":\"v\"}", list.get(5)); // whitespaces are insignificant
		assertTrue("type must be string", list.get(5) instanceof String);
	}

	@Test
	public void testStrictGetValueMaps() throws ValueParseException {
		SvnPropertyValueList val = new SvnPropertyValueList(
				"[{\"k\":\"v\"}, null, {\"k2\":\"[1]\"}, {\"k\":[1]}]");
		List<Map<String, String>> list = val.getValueMaps();
		assertTrue("Should return java maps", list.get(0) instanceof Map);
		assertEquals("v", list.get(0).get("k"));
		assertNull("Should preserve null values", list.get(1));
		assertEquals("[1]", list.get(2).get("k2"));
		assertEquals("[1]", list.get(3).get("k"));
		assertTrue("should always be real strings, serialized json if necessary",
				list.get(3).get("k") instanceof String);
	}
	
	@Test
	public void testStrictGetValueMapsNotMatching() {
		SvnPropertyValueList val = null;
		try {
			val = new SvnPropertyValueList(
				"[{\"k\":\"v\"}, \"string\"]");
		} catch (ValueParseException e1) {
			fail("Should not throw exception at parsing unless the json is invalid");
		}
		try {
			val.getValueMaps();
			fail("Should throw error when a not-null item isn't json object");
		} catch (ValueParseException e) {
			assertTrue("Should say at what index the error occurred", e.getMessage().contains("index 1"));
		}
	}
	
	@Test
	public void testSerializeMap() {
		List<Map<String, String>> list = new ArrayList<Map<String,String>>();
		Map<String,String> m1 = new HashMap<String,String>();
		m1.put("a", "x");
		list.add(m1);
		list.add(null);
		Map<String,String> m2 = new TreeMap<String,String>(); // tree map to get keys in alphabetical order
		m2.put("a", "y");
		m2.put("b", "z");
		list.add(m2);
		
		SvnPropertyValueList val = new SvnPropertyValueList(list);
		assertEquals("[{\"a\":\"x\"},null,{\"a\":\"y\",\"b\":\"z\"}]", val.getSvnString());
	}
	
}
