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
package se.simonsoft.cms.item.config;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.inject.Inject;

import se.simonsoft.cms.item.CmsItemId;

/**
 * Configuration that relates to a resource through where it is placed in the system.
 * 
 * Different resource containers, folders typically, might have different settings for translation etc.
 * 
 * For system level configuration that do not change at runtime or per path,
 * use instead {@link Inject} <code>@Named("config:com.example.cms.myconfig") String myconfig</code> injections.
 */
public class CmsResourceContext implements Iterable<CmsConfigOption> {

	private CmsItemId contextFor;
	
	private HashMap<String, CmsConfigOption> map;
	
	public CmsResourceContext(CmsItemId contextFor, Map<String, CmsConfigOption> options) {
		this.contextFor = contextFor;
		// Generate a new map to ensure it is not changed somewhere else.
		this.map = new HashMap<String, CmsConfigOption>(options);
		for (String key : options.keySet()) {
			this.map.put(key, options.get(key));
		}
	}
	
	public CmsConfigOption getConfigOption(String key) {
		return map.get(key);
	}
	
	public boolean hasConfigOption(String key) {
		return map.containsKey(key);
	}
	
	public CmsItemId getIsContextFor() {
		return contextFor;
	}

	@Override
	public Iterator<CmsConfigOption> iterator() {
		return map.values().iterator();
	}
	
}
