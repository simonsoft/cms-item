package se.simonsoft.cms.item.uuid;

import static org.junit.Assert.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import se.simonsoft.cms.item.encoding.Base32;

public class UuidShortSortableTest {
	
	@Test
	public void testGetCounter() {
		UuidShortSortable u = new UuidShortSortable();
		String last = "";
		Base32 encoding = new Base32();
		for (int i = 0; i < 32 * 32 * 32 * 32; i++) {
			String c = u.getCounter();
			if (i < 32) {
				assertEquals("Should pad from left with lowest base32 char. Got " + c,
						encoding.getZero(), c.charAt(0));
			}
			assertEquals("Counter should be known lenght", 4, c.length());
			assertTrue("Sequence should be increasing", c.compareTo(last) > 0);
		}
		try {
			u.getCounter();
			fail("Should throw exception when maximum sequence length has been exceeded");
			// Should allow just over a million (1048576) UUIDs from one instance.
		} catch (IllegalStateException e) {
			// expected
		}
	}
	
	@Test
	public void testTimestampOutOfRange() {
		try {
			new UuidShortSortable();
		} catch (IllegalArgumentException e) {
			
		}
	}
	
	@Test
	public void testTimestampLength() {
		int expect = 15;
		List<Long> dates = new LinkedList<Long>();
		dates.add(UuidShortSortable.toTimestamp("2010-01-01T00:00:00.000+0000"));
		dates.add(UuidShortSortable.toTimestamp("2010-01-01T00:00:00.001+0000"));
		dates.add(UuidShortSortable.toTimestamp("2010-01-01T00:00:01.000+0000"));
		dates.add(UuidShortSortable.toTimestamp("2010-01-01T01:00:00.000+0000"));
		dates.add(System.currentTimeMillis()); // today
		dates.add(UuidShortSortable.toTimestamp("2020-01-01T00:00:00.000+0000"));
		dates.add(UuidShortSortable.toTimestamp("2030-01-01T00:00:00.000+0000"));
		dates.add(UuidShortSortable.toTimestamp("2034-11-03T00:00:00.000+0000"));
		dates.add(UuidShortSortable.toTimestamp("2044-11-03T00:00:00.000+0000"));
		//dates.add(UuidShortSortable.toTimestamp("2080-11-03T00:00:00.000+0000"));
		//dates.add(0L); // 1970-01-01
		Map<Long,String> ids = new HashMap<Long,String>();
		for (long d : dates) {
			String id = new UuidShortSortable(d).getUuid();
			ids.put(d, id);
			System.out.println(new SimpleDateFormat("yyyy-MM-dd").format(new Date(d))
					+ ": " + id.substring(0, 9));
		}
		for (int i = 0; i < dates.size(); i++) {
			long datestamp = dates.get(i);
			String id = ids.get(datestamp);	
			assertEquals("Unexpected length at " + i, expect, id.length());
			//System.out.println(datestamp);
			//System.out.println(UuidShortSortable.decodeTimestamp(id));
			assertEquals("decoded timestamp mismatch at " + i, datestamp, new UuidShortSortable().decodeTimestamp(id));
		}
	}
	
	// We could just tolerate a leading 0 for 30 years to avoid this problem
	@Test
	public void testTimestampBeforeNow() {
		Long date = UuidShortSortable.toTimestamp("2000-11-03T00:00:00.000+0000");
		try {
			new UuidShortSortable(date).getUuid();
			fail("Expected to have a start date for timestampls");
		} catch (IllegalArgumentException e) {
			assertTrue(e.getMessage(), e.getMessage().contains("before 2010-01-01"));
		}
	}

	@Test
	public void testTimestampTooLarge() {
		Long date = UuidShortSortable.toTimestamp("2044-11-03T20:00:00.000+0000");
		try {
			new UuidShortSortable(date).getUuid();
			fail("Expected to have an upper limit for timestamp encoding, no wrap");
		} catch (IllegalArgumentException e) {
			// OK
		}
	}
	
	@Test 
	public void testReproduceSequence() {
		
		UuidShortSortable uuidGen0 = new UuidShortSortable();
		String uuid00 = uuidGen0.getUuid();
		String uuid01 = uuidGen0.getUuid();
		
		UuidShortSortable uuidGen1 = new UuidShortSortable(uuid00);
		assertEquals(uuid00, uuidGen1.getUuid());
		assertEquals(uuid01, uuidGen1.getUuid());
	}
	
	/**
	 * When the first ID is used as abx:ReleaseId it is useful if the last group of zeroes defines the sequence length.
	 */
	@Test
	public void testNoZeroBeforeSequence() {
		String sample = new UuidShortSortable().getUuid();
		int pos = sample.length() - 1 - 4; // position before sequence of length 4
		for (int i = 0; i < 1000; i++) {
			assertFalse("Should avoid zero for last char in random", '0' == new UuidShortSortable().getUuid().charAt(pos));
		}
	}
	
}
