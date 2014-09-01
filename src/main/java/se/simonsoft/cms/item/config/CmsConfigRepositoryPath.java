package se.simonsoft.cms.item.config;

import java.util.HashMap;
import java.util.Map;

public class CmsConfigRepositoryPath {

	private HashMap<String, CmsConfigOption> map;
	
	public CmsConfigRepositoryPath(Map<String, CmsConfigOption> options) {
		
		// Generate a new map to ensure it is not changed somewhere else.
		this.map = new HashMap<String, CmsConfigOption>(options);
	}
	
	public CmsConfigOption getConfigOption(String name) {
		
		return map.get(name);
	}
	
}
