package se.simonsoft.cms.item.export;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CmsExportPrefix {

	private final String prefix;
	
	private static final Pattern NICE = Pattern.compile("[a-z0-9]+");
	
	public CmsExportPrefix(String prefix) {
		Matcher m = NICE.matcher(prefix);
		if (!m.matches()) {
			throw new IllegalArgumentException("Not a valid export prefix: " + prefix);
		}
		
		this.prefix = prefix;
	}
	
	
	@Override
	public boolean equals(Object anObject) {
		return this.prefix.equals(anObject);
	}
	
	@Override
	public int hashCode() {
		return this.prefix.hashCode();
	}
	
	@Override
	public String toString() {
		return this.prefix.toString();
	}

}
