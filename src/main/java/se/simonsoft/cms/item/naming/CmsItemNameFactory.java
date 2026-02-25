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
package se.simonsoft.cms.item.naming;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.MessageFormatter;

import se.simonsoft.cms.item.CmsItem;
import se.simonsoft.cms.item.CmsItemId;
import se.simonsoft.cms.item.CmsItemLock;
import se.simonsoft.cms.item.CmsItemPath;
import se.simonsoft.cms.item.CmsRepository;
import se.simonsoft.cms.item.info.CmsConnectionException;
import se.simonsoft.cms.item.info.CmsItemLookup;
import se.simonsoft.cms.item.info.CmsItemNotFoundException;

/**
 * Experimental
 */
@Singleton
public class CmsItemNameFactory {
	
	private final Map<CmsRepository, CmsItemLookup> lookup;
	
	private static final String CMS_CLASS_SHARDPARENT = "shardparent";
	private static final String PROPNAME_CONFIG_ITEMNAMEPATTERN = "cmsconfig:ItemNamePattern";
	
	private static final Logger logger = LoggerFactory.getLogger(CmsItemNameFactory.class);
	
	private final Map<CmsItemId, CmsItemPath> itemPathConsumed = new HashMap<>(); // Key: folderId (shard parent), value: item path most recently consumed for that folder
	
	@Inject
	public CmsItemNameFactory(Map<CmsRepository, CmsItemLookup> lookup) {
		this.lookup = lookup;
	}
	
	
	public synchronized CmsItemPath getItemPath(CmsItem folder, String extension) {
		if (!folder.isCmsClass(CMS_CLASS_SHARDPARENT)) {
        	throw new IllegalArgumentException(MessageFormatter.format("Not a shard parent folder: {}", folder).getMessage());
		}
		CmsItemNamePattern pattern = getItemNamePattern(folder);
		
		CmsItemLookup itemLookup = new CmsItemLookupNamingShim(lookup.get(folder.getId().getRepository()), itemPathConsumed.get(folder.getId()));
		CmsItemNaming itemNaming = new CmsItemNamingShard1K(folder.getId().getRepository(), itemLookup);
		CmsItemPath result = itemNaming.getItemPath(folder.getId().getRelPath(), pattern, extension);
		logger.trace("Generated item path: {} for folder: {} with pattern: {} and extension: {}", result, folder.getId(), pattern, extension);
		this.itemPathConsumed.put(folder.getId(), result);
		return result;
	}
	
	
    public static boolean isShardParent(CmsItem folder) {

        if (!folder.isCmsClass(CMS_CLASS_SHARDPARENT)) {
        	return false;
		}
        
        // Test the configured pattern, throws exception if invalid
        CmsItemNamePattern pattern = getItemNamePattern(folder);
        logger.trace("Shard parent folder: {} with pattern: {}", folder.getId(), pattern);

        return true;
    }
    
    public static CmsItemNamePattern getItemNamePattern(CmsItem folder) {

    	if (!folder.getProperties().containsProperty(PROPNAME_CONFIG_ITEMNAMEPATTERN)) {
        	throw new IllegalArgumentException(MessageFormatter.format("Location does not define a name pattern: {}", folder).getMessage());
        }

        // Test the configured pattern, throws exception if invalid
        return new CmsItemNamePattern(folder.getProperties().getString(PROPNAME_CONFIG_ITEMNAMEPATTERN));
    }
    
    
    private class CmsItemLookupNamingShim implements CmsItemLookup {
    	
    	private CmsItemLookup itemLookup;
    	private CmsItemPath previous;
    	
    	CmsItemLookupNamingShim(CmsItemLookup itemLookup, CmsItemPath previous) {
			this.itemLookup = itemLookup;
			this.previous = previous;
		}

		@Override
		public CmsItem getItem(CmsItemId id) throws CmsConnectionException, CmsItemNotFoundException {
			return itemLookup.getItem(id);
		}

		@Override
		public Set<CmsItemId> getImmediateFolders(CmsItemId parent) throws CmsConnectionException, CmsItemNotFoundException {
			Set<CmsItemId> result = itemLookup.getImmediateFolders(parent);
			if (previous != null && previous.getParent() != null) {
				result.add(parent.getRepository().getItemId(previous.getParent(), null));
			}
			return result;
		}

		@Override
		public Set<CmsItemId> getImmediateFiles(CmsItemId parent) throws CmsConnectionException, CmsItemNotFoundException {
			Set<CmsItemId> result = itemLookup.getImmediateFiles(parent);
			if (previous != null && previous.getParent() != null && previous.getParent().equals(parent.getRelPath())) {
				result.add(parent.getRepository().getItemId(previous, null));
			}
			return result;
		}

		@Override
		public Set<CmsItem> getImmediates(CmsItemId parent) throws CmsConnectionException, CmsItemNotFoundException {
			throw new UnsupportedOperationException("Not implemented");
		}

		@Override
		public Iterable<CmsItemId> getDescendants(CmsItemId parent) {
			throw new UnsupportedOperationException("Not implemented");
		}

		@Override
		public CmsItemLock getLocked(CmsItemId itemId) {
			throw new UnsupportedOperationException("Not implemented");
		}
    	
    	
    	
    }
}
