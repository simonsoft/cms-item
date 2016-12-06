package se.simonsoft.cms.item.uuid;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import se.simonsoft.cms.item.encoding.Base32;

/**
 * Produces a sequence of unique IDs.
 * Users need to balance sequence length and new instances:
 * There's a maximum sequence length, but the random part of
 * the id after the timestamp is not very large so multiple
 * instances per millisecond might cause a significant risk of
 * collisions.
 */
public class UuidShortSortable implements UuidGenerator {

	private static final int LENGTH_SEQUENCE = 4;
	private static final int LENGTH_RANDOM = 3;
	private static final int DATESTAMP_LENGTH = 8;
	
	private static final int LENGTH_TOTAL = LENGTH_SEQUENCE + LENGTH_RANDOM + DATESTAMP_LENGTH;
	
	public static final String DATE_START_STR = "2010-01-01T00:00:00.000+0000";
	
	private final long dateStart; 
	
	private String start;
	private String middle;
	private int counter;
	
	private Base32 encoding;

	public UuidShortSortable() {
		this(System.currentTimeMillis());
	}
	
	public UuidShortSortable(long timestamp) {
		this(timestamp, new Base32());
	}
	
	public UuidShortSortable(String uuid) {
		this.encoding = new Base32();
		
		if (uuid == null) {
			throw new IllegalArgumentException("The uuid must not be null.");
		}
		
		if (uuid.length() != LENGTH_TOTAL) {
			throw new IllegalArgumentException("The uuid has invalid length: " + uuid);
		}
		
		this.encoding.decode(uuid); // Validate that we can decode, no invalid chars.
		
		this.dateStart = toTimestamp(DATE_START_STR);
		long millis = decodeTimestamp(uuid, encoding) - dateStart;
		this.start = this.encoding.encodePad(millis, DATESTAMP_LENGTH);

		this.middle = uuid.substring(DATESTAMP_LENGTH, DATESTAMP_LENGTH + LENGTH_RANDOM);
	}
	
	UuidShortSortable(long timestamp, Base32 encoding) {
		this.encoding = encoding;
		this.dateStart = toTimestamp(DATE_START_STR);
		if (timestamp < dateStart) {
			throw new IllegalArgumentException("Timestamps before " + DATE_START_STR + " not supported. Got " + timestamp);
		}
		long millis = timestamp - dateStart;
		this.start = this.encoding.encodePad(millis, DATESTAMP_LENGTH);
		do {
			this.middle = this.encoding.random(LENGTH_RANDOM);
		} while (this.middle.endsWith(encoding.encode(0)));
		this.counter = 0;
	}
	
	@Override
	public String getUuid() {
		return start + middle + getCounter();
	}
	
	public long getDateStart() {
		return dateStart;
	}
	
	protected String getCounter() {
		try {
			return encoding.encodePad(counter++, LENGTH_SEQUENCE);
		} catch (IllegalArgumentException e) {
			throw new IllegalStateException("Sequence length exceeded. New instance required for uniqueness.", e);
		}
	}
	
	static long toTimestamp(String isoDate) {
		try {
			return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").parse(isoDate).getTime();
		} catch (ParseException e) {
			throw new RuntimeException("Failed to parse start date for timestamped UUID: " + isoDate, e);
		}
	}
	
	long decodeTimestamp(String uuid) {
		
		return decodeTimestamp(uuid, encoding);
	}
	
	private static long decodeTimestamp(String uuid, Base32 encoding) {
		
		long timestamp = encoding.decode(uuid.substring(0, DATESTAMP_LENGTH));
		return timestamp + toTimestamp(DATE_START_STR);
	}
	

}
