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
package se.simonsoft.cms.item.export;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.OutputStream;
import java.util.*;


public class CmsExportJob extends CmsExportJobBase {

    private Logger logger = LoggerFactory.getLogger(CmsExportJob.class);
    private List<CmsExportItem> exportItems = new ArrayList<>();
    private boolean isPrepared = false;
    private Map<CmsExportMetaKey, String> metaMap = new HashMap<>();
    private Map<CmsExportTagKey, CmsExportTagValue> tagMap = new HashMap<>();


	public CmsExportJob(CmsExportPrefix jobPrefix, String jobName, String jobExtension) {
		super(jobPrefix, jobName, jobExtension);
	}

    
	public boolean isEmpty() {
		return exportItems.isEmpty();
	}
	
    /**
     * NOTE: Intended to be called by the CmsExportWriter (no longer called directly).
     * Starts the preparation of the CmsExportItem(s). Is only allowed to be called once.
     * @return Long with the total size of all items, null if one or more items have unknown size. 
     * Note that the size is without compression or archiving into a single stream.
     */
    public Long prepare() {

        if (isPrepared) {
            throw new IllegalStateException("The export job is already prepared.");
        }

        logger.info("Preparing: {} CmsExportItems for export.", getExportItems().size());
        
        if (getExportItems().isEmpty()) {
            throw new IllegalArgumentException("There are no items in the export job.");
        }
        
        Long totalSize = 0L;
        for (CmsExportItem item : getExportItems()) {
            Long size = item.prepare();
            if (size != null && totalSize != null) {
				totalSize += size;
			} else {
				totalSize = null; // If one item is unknown size, the total size is unknown.
			}
        }
        
        isPrepared = true;
        return totalSize;
    }

    /**
     * Will traverse through List<CmsExportItem> and ask if they are ready to be exported.
     * @return true if job is prepared and all items report isReady.
     */
    public Boolean isReady() {

        if (getExportItems().isEmpty()) {
            throw new IllegalArgumentException("There are no items in the export job.");
        }

        // The job prepare() must have been called.
        if (!isPrepared) {
            return false;
        }

        // Also verify that all items report isReady(). 
        // Currently no known reason why a prepared item would return false.
        for (CmsExportItem item: getExportItems()) {
            if (!item.isReady()) return false;
        }

        return true;
    }

    /**
     * Will add a CmsExportItem, dose not allow duplicates or modification
     * @param exportItem
     */
    public void addExportItem(CmsExportItem exportItem) {

    	if (exportItem == null) {
            throw new IllegalArgumentException("Export item must not be null.");
        }
        
        if (exportItem.getResultPath() == null) {
            throw new IllegalArgumentException("The export path must not be null");
        }
        
    	addExportItemInternal(exportItem);
    }
    
    protected void addExportItemInternal(CmsExportItem exportItem) {

    	if (exportItem == null) {
            throw new IllegalArgumentException("Export item must not be null.");
        }
    	
        if (isPrepared) {
            throw new ConcurrentModificationException("It's not allowed to modify the CmsExportItem list when preparation is started.");
        }

        if (this.getExportItems().contains(exportItem)) {
            throw new IllegalArgumentException("Duplicated CmsExportItem, this item is already in the list");
        }
        logger.debug("Adding: {} to the CmsExportJob", exportItem);
        getExportItems().add(exportItem);
    }

    public List<CmsExportItem> getExportItems() {
        return this.exportItems;
    }


    public Map<CmsExportMetaKey, String> getMeta() {
        return Collections.unmodifiableMap(this.metaMap);
    }

    public CmsExportJob withMeta(String key, String value) {
        CmsExportMetaKey cmsExportMetaKey = new CmsExportMetaKey(key);
        return withMeta(cmsExportMetaKey, value);
    }
    
    public CmsExportJob withMeta(CmsExportMetaKey key, String value) {

        if (value == null || value.isEmpty()) {
            throw new IllegalArgumentException("Can not add empty or null value as meta");
        }

        if (metaMap.containsKey(key)) {
            throw new IllegalArgumentException("The meta key already exist in map. Key: " + key);
        }

        metaMap.put(key, value);
        return this;
    }
    
    
    public Map<CmsExportTagKey, CmsExportTagValue> getTagging() {
        return Collections.unmodifiableMap(this.tagMap);
    }

    public CmsExportJob withTagging(String key, String value) {
        CmsExportTagKey cmsExportTagKey = new CmsExportTagKey(key);
        CmsExportTagValue cmsExportTagValue = new CmsExportTagValue(value);

        return withTagging(cmsExportTagKey, cmsExportTagValue);
    }
    
    public CmsExportJob withTagging(CmsExportTagKey key, CmsExportTagValue value) {

        if (tagMap.containsKey(key)) {
            throw new IllegalArgumentException("The tag already exist in map. Tag: " + key);
        }

        tagMap.put(key, value);
        return this;
    }

    
    public interface SingleItem {

        void getResultStream(OutputStream out);
    }



}
